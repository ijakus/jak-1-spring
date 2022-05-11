# jak-1-spring

This project was made for [JAK#1 Meetup - CloudNative Deathmatch](https://www.meetup.com/IBM-Developer-Croatia/events/285589345/).

### Prerequisites
Docker should be installed and configured on your machine prior to creating the image, see [the Getting Started section of the reference guide](https://docs.spring.io/spring-native/docs/0.12.0-SNAPSHOT/reference/htmlsingle/#getting-started-buildpacks).

#### Creating the docker image
Image can be created by running the following command from the project root:
```
$ ./gradlew bootBuildImage
```

#### Start the container
The created image can then be started with the following command.
```
$ docker run --rm -p 8080:8080 jak-1-spring:0.0.1-SNAPSHOT -Dtarget.services=http://service1:8080,http://service2:8080
```


### Configuring the application

When using delegated mode list of sorting service URL endpoints  can be passed in environment variable target.services. URLs should be comma separated i.e.

`-Dtarget.services=http://service1:8080,http://service2:8080`
