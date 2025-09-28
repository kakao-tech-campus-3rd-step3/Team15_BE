# 1단계: 빌드
FROM gradle:8.7.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# 2단계: 실행
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
