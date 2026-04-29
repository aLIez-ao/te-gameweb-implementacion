package ico.gameweb.repositorio;

import ico.gameweb.modelo.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Juego.
 *
 * JpaRepository<Juego, Long> hereda automáticamente:
 *   - findAll()
 *   - findById(id)
 *   - save(juego)
 *   - deleteById(id)
 *   - count()
 *   ... y muchos más
 *
 * Solo declarando el método, Spring Data genera la consulta SQL:
 */
@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {

    // Spring Data genera: SELECT * FROM juegos WHERE genero = ?
    List<Juego> findByGenero(String genero);

    // Spring Data genera: SELECT * FROM juegos WHERE nombre LIKE %?%
    List<Juego> findByNombreContainingIgnoreCase(String nombre);
}
