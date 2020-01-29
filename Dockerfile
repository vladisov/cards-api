FROM openjdk:alpine
EXPOSE 8082
ADD ./target/cards-1.0-SNAPSHOT.jar cards-api.jar
