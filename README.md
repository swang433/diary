Store Spring Boot app

This repository contains a minimal Spring Boot application.

Quick status (from this session)
- Java: 26.0.1 (checked with ./mvnw -v)
- Build: success (./mvnw -DskipTests package)
- Run: confirmed (java -jar target/store-0.0.1-SNAPSHOT.jar started successfully)

How to run locally

1) Build with the included Maven wrapper:

```bash
./mvnw -DskipTests package
```

2) Run the built JAR:

```bash
java -jar target/store-0.0.1-SNAPSHOT.jar
```

Run in background (macOS / zsh):

```bash
nohup java -jar target/store-0.0.1-SNAPSHOT.jar > store.log 2>&1 &
# then monitor with:
tail -f store.log
```

Alternative: run with the Maven plugin (dev):

```bash
./mvnw spring-boot:run
```

Ports and config
- By default Spring Boot listens on port 8080. You can change it in `src/main/resources/application.yaml` or with an env var: `SPRING_BOOT_SERVER_PORT=9090` or `--server.port=9090`.

Docker (build and run)

Below is a minimal Dockerfile you can use once you've built the JAR locally (safer than trying to compile inside Docker unless you need a full CI build).

Dockerfile (save as `Dockerfile` at repo root):

```dockerfile
# Use a Java 26 runtime image
FROM eclipse-temurin:26-jre AS runtime

WORKDIR /app
COPY target/store-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Build and run the image:

```bash
docker build -t yourname/store:latest .
docker run -p 8080:8080 yourname/store:latest
```

Notes: pick a base image that supports Java 26. If your environment or Docker Hub base image doesn't have Java 26, you can either build a multi-stage image that runs `./mvnw package` inside a build image (with Maven + JDK 26) or build the JAR locally and use the runtime-only image above.

Deploying to cloud

- AWS Elastic Beanstalk: create a Java platform and upload the JAR (or use Docker). EB accepts a runnable jar as the application artifact.
- AWS ECS / ECR: build/push Docker image to ECR and deploy using ECS Fargate or EC2.
- Heroku: deploy using a Procfile that runs `java -jar target/...jar` or deploy a Docker image.
- Google Cloud Run: push a container image and create a Cloud Run service.

CI/CD (GitHub Actions) example (build + package + optional push to container registry)

```yaml
# .github/workflows/ci.yml - build JAR
name: CI
on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          java-version: '26'
          cache: 'maven'
      - name: Build with Maven
        run: ./mvnw -DskipTests package
      - name: Upload artifact
        uses: actions/upload-artifact@v4
        with:
          name: store-jar
          path: target/store-0.0.1-SNAPSHOT.jar
```

Next steps I can do for you
- Add a Dockerfile and a GitHub Actions workflow in the repo and test the Docker build here (I can add them on request).
- Provide step-by-step instructions for a specific cloud provider (ECS, Elastic Beanstalk, Cloud Run) including exact commands.

Completion summary
- I verified Java and Maven, built the project, and confirmed the application starts locally.
- This README has copy-paste commands to build, run, Dockerize, and an example CI workflow.

If you want, I can now add a `Dockerfile` and a GitHub Actions workflow file to this repository and/or prepare a deployable Docker image and test it locally.