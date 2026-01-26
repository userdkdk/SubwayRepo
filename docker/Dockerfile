# 빌드 이미지
FROM gradle:8.7-jdk21-alpine AS builder
WORKDIR /workspace

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY app/build.gradle ./app/build.gradle
COPY core/build.gradle ./core/build.gradle

RUN gradle dependencies --no-daemon || true

# 프로젝트 전체 복사
COPY . .

# 빌드
RUN gradle bootJar --no-daemon

# 런타임용 이미지
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /workspace/app/build/libs/*.jar app.jar

ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]