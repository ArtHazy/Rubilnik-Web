FROM gradle:8.7-jdk21 as build
# work dir inside the container
WORKDIR /home/app
# copy the project files into the container
COPY --chown=gradle:gradle . /home/app/
RUN gradle build --no-daemon

FROM openjdk:21-jdk
WORKDIR /home/app
COPY --from=build /home/app/app/build/libs/app.jar app/app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app/app.jar" ]