FROM eclipse-temurin:17-jdk
LABEL org.opencontainers.image.authors="ICO"
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
