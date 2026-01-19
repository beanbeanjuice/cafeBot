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
FROM eclipse-temurin:25-jre AS runtime

ARG UID=1001
ARG GID=1001
RUN groupadd -g ${GID} appuser \
    && useradd -l -u ${UID} -g ${GID} -m appuser

# Set the working directory
WORKDIR /bot

# Copy the built JAR file dynamically
COPY --from=build "/app/libs/cafeBot-[0-9]*.jar" /bot/cafeBot.jar
COPY ai.json /bot/ai.json

RUN mkdir -p /bot/logs \
    && chown -R appuser:appuser /bot

USER appuser

# Expose environment variables for customization
ENV JAVA_OPTS="-Xmx3G"

# Default command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar cafeBot.jar"]
