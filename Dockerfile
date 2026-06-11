# Stage 1: Build the Maven project
FROM maven:3.8.5-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run Tomcat
FROM tomcat:9.0-jdk17-openjdk-slim
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Configure Tomcat to listen to Railway's dynamic PORT
CMD ["sh", "-c", "sed -i 's/port=\"8080\"/port=\"'\"$PORT\"'\"/g' /usr/local/tomcat/conf/server.xml && catalina.sh run"]
