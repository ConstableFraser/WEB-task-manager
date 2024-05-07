FROM gradle:8.5-jdk21

WORKDIR /.

RUN ./gradlew --no-daemon build

CMD ./build/install/app/bin/app
