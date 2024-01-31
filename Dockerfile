FROM maven:3.9.3

WORKDIR /calculadora-financeira
COPY . .

RUN mvn clean install -DskipTests
ENTRYPOINT mvn spring-boot:run