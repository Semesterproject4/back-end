FROM maven:3.8.1-openjdk-15 AS build-env

workdir /home/backend/BrewMesModulesParent
COPY ./BrewMesModulesParent .

RUN mvn install

workdir /home/backend/BrewMesModulesParent/core
RUN mvn clean package spring-boot:repackage

FROM openjdk:15
workdir /home/backend
COPY --from=build-env /home/backend/BrewMesModulesParent/core/target/brewmes.jar ./brewmes.jar
CMD ["java","-jar","brewmes.jar"]