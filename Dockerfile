FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} hbc.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/hbc.jar"]
