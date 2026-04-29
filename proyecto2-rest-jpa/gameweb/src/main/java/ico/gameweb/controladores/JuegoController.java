package ico.gameweb.controladores;

import ico.gameweb.modelo.Juego;
import ico.gameweb.repositorio.JuegoRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para el recurso "Juego".
 *
 * @RestController = @Controller + @ResponseBody
 *   → cada método retorna JSON directamente (no vistas HTML)
 *
 * Base URL: /api/juegos
 *
 * ENDPOINTS DE PRUEBA:
 *   GET    /api/juegos                     → listar todos
 *   GET    /api/juegos/{id}                → buscar por ID
 *   GET    /api/juegos/buscar?nombre=mario → buscar por nombre
 *   GET    /api/juegos/genero/{genero}     → filtrar por género
 *   POST   /api/juegos                     → crear nuevo
 *   PUT    /api/juegos/{id}                → actualizar
 *   DELETE /api/juegos/{id}                → eliminar
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/juegos")
public class JuegoController {

    private static final Logger log = LoggerFactory.getLogger(JuegoController.class);

    // Inyección de dependencias (Spring inyecta el repositorio automáticamente)
    private final JuegoRepository juegoRepository;

    public JuegoController(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    // ── GET /api/juegos ────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Juego>> listarTodos() {
        log.info("GET /api/juegos - Listando todos los juegos");
        List<Juego> juegos = juegoRepository.findAll();
        return ResponseEntity.ok(juegos);   // HTTP 200 + JSON
    }

    // ── GET /api/juegos/{id} ───────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Juego> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/juegos/{}", id);
        Optional<Juego> juego = juegoRepository.findById(id);

        // Si existe → 200 OK con el juego; si no → 404 Not Found
        return juego.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // ── GET /api/juegos/buscar?nombre=mario ───────────────────────────────
    @GetMapping("/buscar")
    public ResponseEntity<List<Juego>> buscarPorNombre(
            @RequestParam String nombre) {
        log.info("GET /api/juegos/buscar?nombre={}", nombre);
        List<Juego> resultado = juegoRepository.findByNombreContainingIgnoreCase(nombre);
        return ResponseEntity.ok(resultado);
    }

    // ── GET /api/juegos/genero/{genero} ───────────────────────────────────
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<Juego>> buscarPorGenero(@PathVariable String genero) {
        log.info("GET /api/juegos/genero/{}", genero);
        return ResponseEntity.ok(juegoRepository.findByGenero(genero));
    }

    // ── POST /api/juegos ───────────────────────────────────────────────────
    // Body JSON ejemplo:
    // { "nombre":"Zelda", "descripcion":"RPG épico", "genero":"Aventura", "anioLanzamiento":2023 }
    @PostMapping
    public ResponseEntity<Juego> crear(@Valid @RequestBody Juego juego) {
        log.info("POST /api/juegos - Creando: {}", juego.getNombre());
        Juego guardado = juegoRepository.save(juego);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);  // HTTP 201
    }

    // ── PUT /api/juegos/{id} ───────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Juego> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Juego datosNuevos) {

        log.info("PUT /api/juegos/{}", id);

        return juegoRepository.findById(id)
            .map(juegoExistente -> {
                juegoExistente.setNombre(datosNuevos.getNombre());
                juegoExistente.setDescripcion(datosNuevos.getDescripcion());
                juegoExistente.setImagen(datosNuevos.getImagen());
                juegoExistente.setGenero(datosNuevos.getGenero());
                juegoExistente.setAnioLanzamiento(datosNuevos.getAnioLanzamiento());
                return ResponseEntity.ok(juegoRepository.save(juegoExistente));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/juegos/{id} ────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/juegos/{}", id);

        if (!juegoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        juegoRepository.deleteById(id);
        return ResponseEntity.noContent().build();  // HTTP 204
    }
}
