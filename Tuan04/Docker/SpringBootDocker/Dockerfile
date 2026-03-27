# Stage 1: Build bằng Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# ✅ Copy pom.xml TRƯỚC để cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# ✅ Copy src sau
COPY src ./src

RUN mvn package -DskipTests

# ──────────────────────────────────────
# Stage 2: Chạy bằng JRE
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]