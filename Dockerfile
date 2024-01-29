FROM openjdk:17
FROM maven:3.9.3

WORKDIR /calculadora-financeira-core
COPY . .

RUN mvn clean install
ENTRYPOINT mvn spring-boot:run