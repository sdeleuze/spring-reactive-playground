/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package playground.mongo;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.reactivestreams.Publisher;
import playground.Person;
import playground.repository.ReactiveRepository;
import reactor.Flux;
import reactor.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * MongoDB async driver is available as Reactive Streams and RxJava.
 * Here, Reactive Streams one is used.
 * @author Sebastien Deleuze
 */
@Profile("mongo")
@Repository
public class MongoPersonRepository implements ReactiveRepository<Person> {

	private final ObjectMapper mapper;
	private final MongoCollection<Document> col;

	@Autowired
	public MongoPersonRepository(MongoDatabase db, ObjectMapper mapper) {
		this.mapper = mapper;
		this.col = db.getCollection("persons");
	}

	@Override
	public Mono<Void> insert(Publisher<Person> personStream) {
		return Flux.from(personStream).flatMap(p -> {
			try {
				Document doc = Document.parse(mapper.writeValueAsString(p));
				return Mono.from(col.insertOne(doc));
			}
			catch (JsonProcessingException ex) {
				return Mono.error(ex);
			}
		}).after();
	}

	@Override
	public Flux<Person> list() {
		return Flux.from(this.col.find()).flatMap(doc -> {
			try {
				return Flux.just(mapper.readValue(doc.toJson(), Person.class));
			}
			catch (IOException ex) {
				return Flux.error(ex);
			}
		});
	}

	@Override
	public Mono<Person> findById(String id) {
		return Mono.from(this.col.find(eq("id", id))).then(doc -> {
			try {
				return Mono.just(mapper.readValue(doc.toJson(), Person.class));
			}
			catch (IOException ex) {
				return Mono.error(ex);
			}
		});
	}
}
