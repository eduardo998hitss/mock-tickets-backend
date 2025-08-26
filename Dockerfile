# ===== Build =====
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src

# repackage garante MANIFEST e launcher do Spring Boot
RUN mvn -q -DskipTests clean package spring-boot:repackage

# ===== Runtime =====
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/mock-tickets-backend-*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=mock JAVA_OPTS=""
EXPOSE 8080
RUN addgroup -S app && adduser -S app -G app
USER app
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
