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

package playground.postgres;

import com.github.pgasync.Db;
import org.reactivestreams.Publisher;
import playground.Person;
import playground.repository.ReactiveRepository;
import reactor.Flux;
import reactor.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Sebastien Deleuze
 */
@Repository
public class PostgresPersonRepository implements ReactiveRepository<Person> {

	private final Db db;

	@Autowired
	public PostgresPersonRepository(Db db) {
		this.db = db;
	}

	@Override
	public Mono<Void> insert(Publisher<Person> personStream) {
		return Flux.from(personStream).flatMap(p -> Flux.convert(
				db.querySet("insert into persons(id, firstname, lastname) values($1, $2, $3)",
				p.getId(), p.getFirstname(), p.getFirstname())
		)).after();
	}

	@Override
	public Flux<Person> list() {
		return Flux.convert(db.queryRows("select * from persons").map(row ->
				new Person(row.getString("id"), row.getString("firstname"), row.getString("lastname"))));
	}

	@Override
	public Mono<Person> findById(String id) {
		return Mono.convert(db.queryRows("select * from persons where id='" + id + "'").map(row ->
				new Person(row.getString("id"), row.getString("firstname"), row.getString("lastname"))));
	}

}
