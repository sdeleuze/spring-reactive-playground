Spring Reactive Playground is a sandbox for experimenting on applications based on
[Spring Reactive][] Web support and async NoSQL drivers. .

## Implementations

You can have a look to the commented repository implementations for each supported database:
 - [CouchbasePersonRepository](https://github.com/sdeleuze/spring-reactive-playground/blob/master/src/main/java/playground/couchbase/CouchbasePersonRepository.java)
 - [MongoPersonRepository](https://github.com/sdeleuze/spring-reactive-playground/blob/master/src/main/java/playground/mongo/MongoPersonRepository.java)
 - [PostgresPersonRepository](https://github.com/sdeleuze/spring-reactive-playground/blob/master/src/main/java/playground/postgres/PostgresPersonRepository.java)

## Usage

URL to use are:
 - `http://localhost:8080/couchbase`
 - `http://localhost:8080/mongo` 
 - `http://localhost:8080/postgres`

Supported methods are:

`GET` request with `Accept: application/json` header:
 
`POST`request with `Content-Type: application/json` header and the following body:
```json
{
  "firstname": "foo",
  "lastname": "bar"
}
```

JSON arrays are also supported
```json
[
  {
    "firstname": "foo1",
    "lastname": "bar1"
  },
  {
    "firstname": "foo2",
    "lastname": "bar2"
  }
]
```

## License
Spring Reactive Playground is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Spring Reactive]: https://github.com/spring-projects/spring-reactive
