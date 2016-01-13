Spring Reactive Playground is a sandbox for experimenting on applications based on
[Spring Reactive][] Web support and async NoSQL drivers. .

## Software design

[ReactiveRepository][] shows what Reactive Spring Data interfaces could look like.

You can have a look to the commented repository implementations for each supported database:
 - [CouchbasePersonRepository][]
 - [MongoPersonRepository][]
 - [PostgresPersonRepository][]

## Usage

Customize the configuration in `application.properties` (optionally use `profiles` to enable only the database you want to test), and run `Application#main()`.

 - Create a single person: ```curl -i -X POST -H "Content-Type:application/json" -H "Accept: application/json" -d '{"id":"1","firstname":"foo1","lastname":"bar1"}' http://localhost:8080/postgres```
 - Create a multiple persons: ```curl -i -X POST -H "Content-Type:application/json" -H "Accept: application/json" -d '[{"id":"2","firstname":"foo2","lastname":"bar2"},{"id":"3","firstname":"foo3","lastname":"bar3"}]' http://localhost:8080/postgres```
 - List all the persons: ```curl -i -H "Accept: application/json" http://localhost:8080/postgres```
 - Get one person: ```curl -i -H "Accept: application/json" http://localhost:8080/postgres/1```
 
Same for `http://localhost:8080/mongo` and `http://localhost:8080/couchbase`.
 
## License
Spring Reactive Playground is released under version 2.0 of the [Apache License][].

[ReactiveRepository]: https://github.com/sdeleuze/spring-reactive-playground/blob/master/src/main/java/playground/repository/ReactiveRepository.java
[CouchbasePersonRepository]: https://github.com/sdeleuze/spring-reactive-playground/blob/master/src/main/java/playground/couchbase/CouchbasePersonRepository.java
[MongoPersonRepository]: https://github.com/sdeleuze/spring-reactive-playground/blob/master/src/main/java/playground/mongo/MongoPersonRepository.java
[PostgresPersonRepository]: https://github.com/sdeleuze/spring-reactive-playground/blob/master/src/main/java/playground/postgres/PostgresPersonRepository.java
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Spring Reactive]: https://github.com/spring-projects/spring-reactive
