package ico.gameweb;

import ico.gameweb.modelo.Juego;
import ico.gameweb.repositorio.JuegoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Se ejecuta UNA VEZ al iniciar la aplicación.
 * Inserta datos de prueba si la tabla está vacía.
 *
 * CommandLineRunner → Spring Boot lo ejecuta automáticamente al arrancar.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final JuegoRepository repo;

    public DataLoader(JuegoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            log.info("Insertando datos iniciales de prueba...");

            repo.save(new Juego(null, "The Legend of Zelda: TOTK",
                "Aventura épica en el reino de Hyrule",
                "https://example.com/zelda.jpg",
                "Aventura", 2023));

            repo.save(new Juego(null, "Super Mario Bros Wonder",
                "Plataformas con nuevas mecánicas de flor maravilla",
                "https://example.com/mario.jpg",
                "Plataformas", 2023));

            repo.save(new Juego(null, "Minecraft",
                "Construye y sobrevive en un mundo de bloques",
                "https://example.com/minecraft.jpg",
                "Sandbox", 2011));

            repo.save(new Juego(null, "God of War Ragnarök",
                "Kratos y Atreus enfrentan el Ragnarök",
                "https://example.com/gow.jpg",
                "Acción", 2022));

            log.info("Datos iniciales cargados: {} juegos", repo.count());
        } else {
            log.info("La tabla ya tiene {} juegos, omitiendo carga inicial.", repo.count());
        }
    }
}
