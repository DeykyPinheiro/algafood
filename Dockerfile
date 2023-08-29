FROM openjdk:17-jdk-alpine

WORKDIR /app

copy . .

RUN apk add --no-cache maven && \
    mvn clean install -DskipTests

#COPY /app/target/*.jar ./api.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/target/algafood-0.0.1-SNAPSHOT.jar"]