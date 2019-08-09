FROM openjdk:12.0.3-jdk-slim
VOLUME /config
ENV SPRING_PROFILES_ACTIVE='local'
ENV SPRING_CONFIG_LOCATION='classpath:application-local.yaml,file:/config/yapam.yaml'
COPY build/libs/yapam.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
