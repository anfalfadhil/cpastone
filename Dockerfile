FROM openjdk:11
RUN mkdir /docEnv
VOLUME /docEnv
ADD ./target/mock-twitter-0.0.1-SNAPSHOT.jar /docEnv
ENTRYPOINT ["java", "-jar", "./docEnv/mock-twitter-0.0.1-SNAPSHOT.jar"]