FROM eclipse-temurin:17 as builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw package

FROM eclipse-temurin:17 as app

WORKDIR /app

COPY --from=builder /app/target/dai-labo-4-1.0-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "dai-labo-4-1.0-SNAPSHOT.jar"]

CMD ["--help"]
