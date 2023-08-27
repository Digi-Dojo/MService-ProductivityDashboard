FROM ubuntu:latest AS build
RUN apt update && apt install -qq -y openjdk-17-jdk
COPY dashboard .
RUN ./gradlew --no-daemon bootJar


FROM openjdk:17-jdk
EXPOSE 8080
COPY --from=build /build/libs/dashboard-0.0.1-SNAPSHOT.jar app.jar
COPY start.sh .
ENTRYPOINT ["./start.sh"]
