FROM openjdk:17-oracle

WORKDIR /app

COPY /target/*.jar ./api.jar

EXPOSE 8080

CMD ["java", "-jar", "api.jar"]