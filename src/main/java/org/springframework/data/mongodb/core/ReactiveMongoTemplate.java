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

package org.springframework.data.mongodb.core;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;

// See https://github.com/spring-projects/spring-reactive/pull/51
import reactor.Flux;
import reactor.Mono;

/**
 * MongoDB provides 2 Reactive Drivers: one base one Reactive Streams, one base RxJava.
 * In Spring 5, our Strategy is to build around a Reactive Streams core, and provides eventually
 * some adapters to composition libraries (RxJava at least, since it is the most used), that's why
 * we are using the MongoDB Reactive Streams drivers.
 *
 * It may be possible to build {@code ReactiveMongoTemplate} and {@code ReactiveMongoOperations}
 * based directly on MongoDB RxJava driver, but since Reactive Streams to RxJava conversion is easy
 * I am not sure it is worth to do.
 *
 * @author Sebastien Deleuze
 */
public class ReactiveMongoTemplate implements ReactiveMongoOperations {

	private final MongoDatabase database;


	public ReactiveMongoTemplate(MongoDatabase database) {
		this.database = database;
	}

	@Override
	public Flux<Document> executeCommand(Bson command) {
		return Flux.wrap(this.database.runCommand(command));
	}

	@Override
	public <T> Flux<T> findAll(Class<T> entityClass) {
		return Flux.wrap(getCollection(entityClass).find());
	}

	@Override
	public Mono<Void> insert(Object objectToSave) {
		return Mono.wrap(getCollection((Class<Object>)objectToSave.getClass()).insertOne(objectToSave)).after();
	}

	@Override
	public <T> Mono<Void> insert(Publisher<? extends Object> objectStream, Class<T> entityClass) {
		// TODO See https://jira.mongodb.org/browse/JAVARS-17
		return Mono.wrap(objectStream).flatMap( p -> getCollection(entityClass).insertOne((T)p)).after();
	}

	private <T> MongoCollection<T> getCollection(Class<T> entityClass) {
		return this.database.getCollection(entityClass.getSimpleName().toLowerCase(), entityClass);
	}
}
