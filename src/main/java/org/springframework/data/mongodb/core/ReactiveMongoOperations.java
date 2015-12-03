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

import org.bson.Document;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;

// See https://github.com/spring-projects/spring-reactive/pull/51
import reactor.Flux;
import reactor.Mono;

/**
 * Draft of Reactive Streams based {@code MongoOperations} interface.
 *
 * @author Sebastien Deleuze
 */
public interface ReactiveMongoOperations {

	/**
	 * Execute a MongoDB command. Any errors that result from executing this command will be converted into Spring's DAO
	 * exception hierarchy and emitted throught the returned {@code Publisher}.
	 *
	 * @param command a MongoDB command
	 */
	Flux<Document> executeCommand(Bson command);

	/**
	 * Query for a stream of objects of type T from the collection used by the entity class.
	 * TODO This method should probably return a a wrapper around {@code com.mongodb.reactivestreams.client.FindPublisher} maybe a Spring Data MongoDB {@code FindComposablePublisher} specific type
	 *
	 * @param entityClass the parameterized type of the returned stream
	 * @return the converted stream
	 */
	<T> Flux<T> findAll(Class<T> entityClass);

	/**
	 * Insert the object into the collection for the entity type of the object to save.
	 * Insert is used to initially store the object into the database. To update an existing object use the save method.
	 *
	 * @param objectToSave the object to store in the collection.
	 * @return A {@code Publisher<Void>} that signal when the data has been effectively inserted into the database.
	 */
	Mono<Void> insert(Object objectToSave);

	/**
	 * Insert a stream of objects into a collection in a single batch write to the database.
	 *
	 * @param objectStream the stream of objects to insert.
	 * @param entityClass class that determines the collection to use
	 * @return A {@code Publisher<Void>} that signal when the data has been effectively inserted into the database.
	 */
	<T> Mono<Void> insert(Publisher<? extends Object> objectStream, Class<T> entityClass);

}
