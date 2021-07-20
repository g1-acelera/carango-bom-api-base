FROM openjdk:11-jre-slim

WORKDIR /carango-bom-api-base

COPY target/*.jar /carango-bom-api-base/app.jar

EXPOSE 8080

CMD java -XX:+UseContainerSupport -Xmx512m -jar app.jar --server.port=$PORT