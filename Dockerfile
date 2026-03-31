FROM eclipse-temurin:25
LABEL authors="mchm"

WORKDIR /app
COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]