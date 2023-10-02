
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /theater

COPY . /theater
#ADD src /buchi/src
#RUN apk update
RUN apk add maven
##
RUN ["mvn", "clean"]
RUN ["mvn", "package"]
RUN ["mvn", "verify"]

#target/*.jar app.jar
ENTRYPOINT ["java","-jar","/theater/target/movieReview-0.0.1-SNAPSHOT.jar"]