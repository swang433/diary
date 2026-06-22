# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy wrapper and pom first so dependency resolution is cached
# across rebuilds when only source files change.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Now copy the rest of the source and build the jar.
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ---- Run stage ----
FROM eclipse-temurin:21-jre AS run
WORKDIR /app

# Copy only the built jar out of the build stage - keeps the final
# image small and free of build tooling (Maven, full JDK, source).
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]