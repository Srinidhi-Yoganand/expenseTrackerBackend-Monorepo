FROM openjdk:17-jdk-slim
LABEL authors="srinidhi"

WORKDIR /app

COPY /build/libs/expenseService-0.0.1-SNAPSHOT.jar /app/expenseService.jar

EXPOSE 9820

ENTRYPOINT ["java", "-jar", "/app/expenseService.jar"]