FROM openjdk:11-jre-slim

EXPOSE 8080

VOLUME /tmp

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar","app.jar"]