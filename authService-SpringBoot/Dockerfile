FROM openjdk:21-jdk-slim
LABEL authors="srinidhi"

WORKDIR /app

COPY app/build/libs/app.jar /app/authService.jar

EXPOSE 9898

ENTRYPOINT ["java", "-jar", "/app/authService.jar"]