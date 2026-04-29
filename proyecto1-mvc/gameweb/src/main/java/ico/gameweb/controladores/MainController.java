package ico.gameweb.controladores;

import ico.gameweb.modelo.Juego;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;          // <-- @Controller, NO @RestController
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controlador Spring MVC.
 *
 * DIFERENCIA CLAVE:
 *   @RestController  → retorna el NOMBRE de una vista Thymeleaf (p.ej. "index")
 *   @RestController → retorna datos en JSON/texto directamente al cliente
 *
 * Este controlador usa @Controller + Model para pasar datos a las plantillas HTML.
 */
@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/portal")
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    /**
     * GET /portal/
     * Carga una lista de juegos en el modelo y renderiza la vista "index.html"
     */
    @GetMapping("/")
    public String index(Model model) {
        log.info("Accediendo a /portal/");

        // Datos de ejemplo (en producción vendrían de un servicio/repositorio)
        List<Juego> juegos = List.of(
            new Juego("The Legend of Zelda",
                      "Aventura épica en el reino de Hyrule",
                      "https://upload.wikimedia.org/wikipedia/en/4/4e/The_Legend_of_Zelda_TOTK.jpg",
                      "Aventura"),
            new Juego("Super Mario Bros",
                      "El fontanero más famoso del mundo",
                      "https://upload.wikimedia.org/wikipedia/en/0/03/Super_Mario_Bros._box.png",
                      "Plataformas"),
            new Juego("Minecraft",
                      "Construye y sobrevive en un mundo de bloques",
                      "https://upload.wikimedia.org/wikipedia/en/5/51/Minecraft_cover.png",
                      "Sandbox")
        );

        // Se añaden los datos al modelo → Thymeleaf los leerá en la vista
        model.addAttribute("juegos", juegos);
        model.addAttribute("titulo", "Catálogo de Juegos");

        return "index";   // Resuelve a src/main/resources/templates/index.html
    }

    /**
     * GET /portal/detalle
     * Ejemplo de ruta con parámetro de consulta: /portal/detalle?nombre=Minecraft
     */
    @GetMapping("/detalle")
    public String detalle(String nombre, Model model) {
        log.info("Detalle del juego: {}", nombre);

        Juego juego = new Juego(nombre,
                "Descripción detallada de " + nombre,
                "",
                "Varios");
        model.addAttribute("juego", juego);

        return "detalle";  // Resuelve a src/main/resources/templates/detalle.html
    }
}
