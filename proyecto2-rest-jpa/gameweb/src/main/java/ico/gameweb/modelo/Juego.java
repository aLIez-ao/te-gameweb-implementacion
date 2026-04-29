package ico.gameweb.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Entidad JPA "Juego".
 *
 * Anotaciones importantes:
 *   @Entity  → Hibernate mapea esta clase a una tabla en MySQL
 *   @Table   → nombre de la tabla (opcional, por defecto usa el nombre de la clase)
 *   @Id      → clave primaria
 *   @GeneratedValue → autoincremento gestionado por la BD
 */
@Entity
@Table(name = "juegos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 255)
    private String imagen;

    @Column(length = 50)
    private String genero;

    @Column(name = "anio_lanzamiento")
    private Integer anioLanzamiento;
}
