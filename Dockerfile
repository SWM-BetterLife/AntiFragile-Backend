# FROM openjdk:17-jdk
FROM openjdk:17-jdk

# Docker 빌드 시점에 환경 변수 JAR_FILE 생성, gradle로 빌드했을 때 jar 파일이 생성되는 경로
ARG JAR_FILE=build/libs/*SNAPSHOT.jar

# COPY build/libs/*.jar antifragile.jar
COPY ${JAR_FILE} antifragile.jar

#RUN wget -O dd-java-agent.jar 'https://dtdg.co/latest-java-tracer'

# 환경 변수 설정 (기본값: develop)
ENV SPRING_PROFILES_ACTIVE=develop

# 운영 및 개발에서 사용되는 환경 설정을 분리
ENTRYPOINT ["java", "javaagent:/dd-java-agent.jar", "-Ddd.logs.injection=true", "-Ddd.service=backend", "-Ddd.env=dev", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/antifragile.jar"]
