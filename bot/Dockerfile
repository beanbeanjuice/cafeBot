# Use an official Gradle image with JDK 21 for building
FROM gradle:jdk21 AS build

# Set the working directory
WORKDIR /app

# Copy the entire project (ensuring gradlew is included)
COPY . ./

# Ensure the Gradle wrapper has execution permissions
RUN chmod +x ./gradlew

# Run Gradle build (explicit shell format to ensure correct execution)
RUN gradle shadowJar

# Use a minimal OpenJDK runtime for the final image
FROM openjdk:21-jdk-slim AS runtime

# Set the working directory
WORKDIR /bot

# Copy the built JAR file dynamically
COPY --from=build /app/libs/cafeBot-*.jar /bot/cafeBot.jar

# Expose environment variables for customization
ENV JAVA_OPTS="-Xmx3G"

# Default command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar cafeBot.jar"]
