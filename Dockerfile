# 1단계: 빌드
FROM gradle:8.7.0-jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradlew gradlew.bat /app/
COPY gradle /app/gradle
COPY src /app/src

# gradlew 실행 권한 추가
RUN chmod +x gradlew

RUN ./gradlew clean build -x test

# 2단계: 실행
FROM eclipse-temurin:21-jdk
WORKDIR /app

# 로그 버퍼링 방지 및 stdout 출력 통합
ENV PYTHONUNBUFFERED=1
ENV JAVA_TOOL_OPTIONS="-Djava.util.logging.ConsoleHandler.level=FINE -Dorg.slf4j.simpleLogger.log.org.springframework=debug"

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
