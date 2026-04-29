package ico.gameweb.modelo;

import lombok.*;

/**
 * Modelo simple de Juego (sin base de datos).
 * Lombok genera constructores, getters, setters y toString automáticamente.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Juego {
    private String nombre;
    private String descripcion;
    private String imagen;   // URL o nombre de imagen
    private String genero;
}
