FROM amazoncorretto:17-alpine-jdk
MAINTAINER boladodacopa.tk
COPY accounts.jar accounts.jar
ENTRYPOINT ["java","-jar","/accounts.jar"]