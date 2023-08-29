FROM openjdk:20

WORKDIR /app

COPY build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]