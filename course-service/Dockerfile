FROM openjdk:21-jdk

WORKDIR /app

COPY target/course-service-0.0.1-SNAPSHOT.jar /app/course-service.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/course-service.jar"]
