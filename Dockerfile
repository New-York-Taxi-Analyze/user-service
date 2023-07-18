FROM eclipse-temurin:17-jdk-jammy

COPY ../build/libs/user-service-0.0.1-SNAPSHOT.jar user-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/user-service-0.0.1-SNAPSHOT.jar"]
