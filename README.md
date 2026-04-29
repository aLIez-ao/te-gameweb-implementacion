# TE GameWeb — Implementación de ejemplos Docker Compose + Spring

---

## PROYECTO 1 — Docker Compose + Spring MVC (Thymeleaf)

### Concepto
Spring MVC usa el patrón **Modelo-Vista-Controlador**:
- **@Controller** retorna el *nombre de una vista* (p.ej. `"index"`)
- Spring busca `src/main/resources/templates/index.html`
- Thymeleaf renderiza el HTML inyectando los datos del **Model**

```
Navegador  →  GET /portal/
            ↓
         @Controller
         model.addAttribute("juegos", lista)
         return "index"
            ↓
         Thymeleaf: templates/index.html
         (sustituye th:each, th:text, etc.)
            ↓
         HTML final → Navegador
```

### Archivos clave
| Archivo | Descripción |
|---------|-------------|
| `MainController.java` | `@Controller` — pasa datos al modelo y retorna nombre de vista |
| `templates/index.html` | Plantilla Thymeleaf con `th:each`, `th:text`, `th:href` |
| `templates/detalle.html` | Vista de detalle de un juego |
| `Juego.java` | POJO simple (sin base de datos) |
| `docker-compose.yml` | Un solo servicio: la app Spring en puerto 9090 |

### Comandos
```bash
cd proyecto1-mvc/gameweb
./mvnw clean package -DskipTests    # Compilar → genera target/*.jar
cd ..
docker compose up --build           # Construir imagen y levantar
```
URL: http://localhost:9090/portal/

### Diferencia @Controller vs @RestController
```java
// Spring MVC: retorna vista HTML
@Controller
public String index(Model model) {
    model.addAttribute("juegos", lista);
    return "index";            // → templates/index.html
}

// REST: retorna JSON directamente
@RestController
public List<Juego> listar() {
    return lista;              // → [{"nombre":"Zelda",...}]
}
```

---

## PROYECTO 2 — Docker Compose + Spring REST + JPA + MySQL

### Arquitectura
```
Docker Compose
  ├── mysql (puerto 3306)          ← Base de datos
  └── spring-app (puerto 9090)     ← API REST
        ├── @RestController        → maneja peticiones HTTP
        ├── JuegoRepository        → Spring Data JPA
        └── Juego (@Entity)        → tabla "juegos" en MySQL
```

### Flujo JPA
```
HTTP POST /api/juegos  →  @RestController
                           ↓
                       juegoRepository.save(juego)
                           ↓
                       Hibernate (JPA)
                           ↓
                       INSERT INTO juegos (...) MySQL
```

### Archivos clave
| Archivo | Descripción |
|---------|-------------|
| `docker-compose.yml` | MySQL 8.4 + Spring App, con healthcheck |
| `Juego.java` | `@Entity` — mapeada a tabla `juegos` en MySQL |
| `JuegoRepository.java` | `JpaRepository` — CRUD gratis |
| `JuegoController.java` | REST CRUD: GET/POST/PUT/DELETE |
| `DataLoader.java` | Inserta 4 juegos de prueba al arrancar |
| `application.properties` | Configuración datasource + JPA |

### Comandos
```bash
cd proyecto2-rest-jpa/gameweb
./mvnw clean package -DskipTests    # Compilar
cd ..
docker compose up --build           # Levanta MySQL + App
```

### Endpoints REST de prueba (usar Postman, curl o browser)

```bash
# Listar todos los juegos
GET http://localhost:9090/api/juegos

# Buscar por ID
GET http://localhost:9090/api/juegos/1

# Buscar por nombre (parcial, sin importar mayúsculas)
GET http://localhost:9090/api/juegos/buscar?nombre=mario

# Filtrar por género
GET http://localhost:9090/api/juegos/genero/Aventura

# Crear nuevo juego (JSON body)
POST http://localhost:9090/api/juegos
Content-Type: application/json
{
  "nombre": "Elden Ring",
  "descripcion": "RPG de mundo abierto de FromSoftware",
  "genero": "RPG",
  "anioLanzamiento": 2022
}

# Actualizar
PUT http://localhost:9090/api/juegos/1
Content-Type: application/json
{
  "nombre": "Zelda Actualizado",
  "descripcion": "Nueva desc",
  "genero": "Aventura",
  "anioLanzamiento": 2023
}

# Eliminar
DELETE http://localhost:9090/api/juegos/2
```

### Códigos HTTP retornados
| Operación | Código |
|-----------|--------|
| GET exitoso | 200 OK |
| POST creación | 201 Created |
| PUT/DELETE exitoso | 200 / 204 No Content |
| No encontrado | 404 Not Found |
| Validación fallida | 400 Bad Request |

---

## Nota sobre Spring Boot 4.x (pom.xml original)
El `pom.xml` original usa `spring-boot-starter-webmvc` y
`spring-boot-starter-thymeleaf-test` que son artefactos de Spring Boot 4.x
(aún en desarrollo). Los proyectos aquí usan **3.2.5 (estable)** con los
artefactos estándar. Si quieres usar 4.x, cambia solo la versión del parent.
