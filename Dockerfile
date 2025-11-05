
# 1단계: 빌드 (Gradle)
FROM gradle:8.7.0-jdk21 AS builder
WORKDIR /app

# Gradle 캐시 유지용 볼륨
VOLUME ["/root/.gradle"]

# 빌드 캐시를 살리기 위해 의존성 파일만 먼저 복사
COPY build.gradle settings.gradle gradlew gradlew.bat /app/
COPY gradle /app/gradle

# gradlew 실행권한 및 의존성 미리 받기
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# 소스 복사
COPY src /app/src

# 빌드 (테스트 제외)
RUN ./gradlew clean build -x test --no-daemon



# 2단계: 실행 (경량 이미지)
FROM eclipse-temurin:21-jdk
WORKDIR /app

# 시간대 및 로그 설정
ENV TZ=Asia/Seoul
ENV JAVA_TOOL_OPTIONS="-Duser.timezone=Asia/Seoul"

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
