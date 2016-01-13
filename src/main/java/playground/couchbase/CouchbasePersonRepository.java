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

package playground.couchbase;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.repository.AsyncRepository;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import playground.Person;
import playground.repository.ReactiveRepository;
import reactor.Flux;
import reactor.Mono;
import rx.Observable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * Couchbase 2.x driver is RxJava1 based and use only Observable, even for async single values
 * Couchbase 3.x driver may be based on RxJava 2.0, with usage of Single instead of Observable where it is relevant
 *
 * @author Sebastien Deleuze
 */
@Profile("couchbase")
@Repository
public class CouchbasePersonRepository implements ReactiveRepository<Person> {

	private static final Logger logger = LoggerFactory.getLogger(CouchbasePersonRepository.class);

	// Couchbase repositories are like a simplified experimental Spring Data
	private final AsyncRepository repository;
	private final AsyncBucket bucket;

	@Autowired
	public CouchbasePersonRepository(AsyncBucket bucket) {
		this.bucket = bucket;
		this.repository = bucket.repository().toBlocking().first();
	}

	@Override
	public Mono<Void> insert(Publisher<Person> personStream) {
		return Flux.from(personStream).flatMap(person -> {
			String id = (person.getId() == null ? person.getFirstname() + "_" + person.getLastname() : person.getId());
			EntityDocument doc = EntityDocument.create(id, person);
			return Flux.convert(this.repository.insert(doc));
		}).after();
	}

	@Override
	public Flux<Person> list() {
		return Flux.convert(this.bucket.query(N1qlQuery.simple("SELECT META(default).id FROM default"))
				.flatMap(result -> {
					// Already discussed with Simon Baslé: AsyncN1qlQueryResult API does not make very easy to deal with errors
					// since they are not emitted as errors in the main Observable<AsyncN1qlQueryRow>
					// They will maybe provide an adapter to make our job easier in 2.x drivers
					// API may be improved in 3.x driver
					if (result.parseSuccess()) {
						return result.rows().flatMap(row -> {
							String id = row.value().getString("id");
							return this.repository.get(id, Person.class).map(d -> d.content());
						});
					}
					else {
						return result.errors().flatMap(jsonErrors -> Observable.error(new IllegalStateException(jsonErrors.getInt("code") + ": " + jsonErrors.getString("msg"))));
					}
				}));
	}

	@Override
	public Mono<Person> findById(String id) {
		return Mono.convert(this.repository.get(id, Person.class).map(d -> d.content()));
	}
}
