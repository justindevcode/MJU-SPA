FROM openjdk:11-jdk
LABEL maintainer="email"
ARG JAR_FILE=build/libs/spaserver-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${PROFILE} -jar /app.jar"]