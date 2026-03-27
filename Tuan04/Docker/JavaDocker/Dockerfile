# Dùng JDK để compile và chạy
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy toàn bộ file .java vào container
COPY src/ ./src/

# Compile file .java ra .class
RUN javac src/main/java/iuh/fit/Main.java -d out/

# Chạy chương trình (tên class chính)
CMD ["java", "-cp", "out", "iuh.fit.Main"]