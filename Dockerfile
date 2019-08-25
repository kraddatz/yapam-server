FROM openjdk:11.0.3-jdk-slim
COPY build/libs/yapam.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
