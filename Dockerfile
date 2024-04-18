# Use a base image (e.g., Debian)
FROM debian:bullseye-slim

# Install required dependencies
RUN apt-get update && \
    apt-get install -y --no-install-recommends wget ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Download and install JDK 23
RUN wget -qO- https://download.java.net/java/early_access/jdk23/18/GPL/openjdk-23-ea+18_linux-x64_bin.tar.gz | tar xvz -C /opt && \
    mv /opt/jdk-23 /opt/openjdk-23 && \
    ln -s /opt/openjdk-23/bin/* /usr/local/bin/ && \
    rm -rf /opt/openjdk-23/*src.zip \
           /opt/openjdk-23/lib/missioncontrol \
           /opt/openjdk-23/lib/visualvm

# Set JAVA_HOME environment variable
ENV JAVA_HOME=/opt/openjdk-23

EXPOSE 80
ARG JAR_FILE=target/*.jar
COPY ./target/helios-api-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]