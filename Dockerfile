FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY soapService.wsdl /home/app/
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11

ENV ws.endpoint=http://localhost:9080/ws
ENV rs.endpoint=http://localhost:9080
ENV server.port=9081

RUN mkdir /app
WORKDIR /app
COPY --from=build /home/app/target/java-cup.jar /app/java-cup.jar
EXPOSE 9081
ENTRYPOINT ["java", "-jar", "/app/java-cup.jar"]