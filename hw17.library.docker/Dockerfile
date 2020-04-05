#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml -Dmaven.test.skip=true clean package

#
# Package stage
#
FROM java:8
EXPOSE 8080
COPY --from=build /home/app/target/hw17.library.docker-0.0.1-SNAPSHOT.jar /usr/local/lib/hw17.library.docker-0.0.1-SNAPSHOT.jar
#ADD hw17.library.docker-0.0.1-SNAPSHOT.jar hw17.library.docker-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/hw17.library.docker-0.0.1-SNAPSHOT.jar"]