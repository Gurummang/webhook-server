# Step 1: Build the application
FROM gradle:8.5-jdk21 as builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Step 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]


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
