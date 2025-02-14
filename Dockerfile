FROM amazoncorretto:21
LABEL authors="srinidhi"

WORKDIR /app

COPY app/build/libs/app.jar /app/app.jar

EXPOSE 9898

ENTRYPOINT ["java", "-jar", "/app/app.jar"]