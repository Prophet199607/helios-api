FROM openjdk:23-oraclelinux8
VOLUME /tmp
EXPOSE 8888
ARG JAR_FILE=target/*.jar
ADD ./target/helios-api-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]