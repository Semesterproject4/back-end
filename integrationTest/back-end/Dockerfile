FROM openjdk:15
workdir /home/backend
ARG JAR_FILE=target/*.jar
COPY ./demo.jar /home/backend/app.jar
CMD ["java","-jar","app.jar"]