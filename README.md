Spring Reactive Playground is a showcase for experimenting on applications based on
[Spring Framework 5.0 Reactive support][], [Spring Data Reactive][] and [Reactive NoSQL drivers][].

## Getting started

Prerequisite:
 - [Install Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 - [Install MongoDB](https://www.mongodb.com/download-center) (optional)

For testing MongoDB support, run `mongod` and enable `mongo` profile in `application.properties`.

Run `Application#main()` in your IDE or `./gradlew bootRun` on command line, then open
`http://localhost:8080/` in your browser and use the various test pages.
 
## License
Spring Reactive Playground is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Spring Framework 5.0 Reactive support]: https://spring.io/blog/2016/07/28/reactive-programming-with-spring-5-0-m1
[Spring Data Reactive]: https://spring.io/blog/2016/11/28/going-reactive-with-spring-data
[Reactive NoSQL drivers]: https://mongodb.github.io/mongo-java-driver-reactivestreams/