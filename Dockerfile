FROM hirokimatsumoto/alpine-openjdk-11
EXPOSE 8082
ADD /target/cards-1.0-SNAPSHOT.jar cards-api.jar
ENTRYPOINT ["java", "-jar", "cards-api.jar"]