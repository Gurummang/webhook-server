# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=target/webhook-server-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]


## Start with a lightweight base image
#FROM openjdk:17-jdk-slim
#
## Set the working directory
#WORKDIR /app
#
## Copy the executable JAR file into the container
#COPY target/your-application-name.jar app.jar
#
## Expose the port that the application runs on
#EXPOSE 8081
#
## Run the JAR file
#ENTRYPOINT ["java", "-jar", "app.jar"]
