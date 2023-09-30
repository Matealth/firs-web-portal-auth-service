FROM openjdk:17-jdk-slim
COPY target/*.jar firs-auth-svc.jar
ENTRYPOINT ["java","-jar","/auth.svc-0.0.1-SNAPSHOT.jar"]