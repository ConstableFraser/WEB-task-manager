FROM gradle:8.5-jdk21

RUN ./gradlew installDist

CMD ./build/install/app/bin/app
