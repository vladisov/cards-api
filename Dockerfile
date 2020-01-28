FROM openjdk:alpine
ARG JASYPT_PASS_ARG
ENV env_jasypt_pass=$JASYPT_PASS_ARG
EXPOSE 8082
ADD ./target/cards-1.0-SNAPSHOT.jar cards-api.jar
ENTRYPOINT java -jar -Djasypt.encryptor.password=$env_jasypt_pass cards-api.jar
