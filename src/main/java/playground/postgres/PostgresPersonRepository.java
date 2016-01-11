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
import playground.Person;
import rx.Observable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Sebastien Deleuze
 */
@Repository
public class PostgresPersonRepository {

	private final Db db;

	@Autowired
	public PostgresPersonRepository(Db db) {
		this.db = db;
	}

	// TODO Use Completable when RxJava 1.1.1 will be released, see https://github.com/ReactiveX/RxJava/issues/3037
	public Observable<Void> insert(Observable<Person> personStream) {
		return personStream.flatMap(p ->
				db.querySet("insert into persons(firstname, lastname, address, postalCode, city) values($1, $2, $3, $4, $5)",
				p.getFirstname(), p.getFirstname(), p.getAddress(), p.getPostalCode(), p.getCity())
		).flatMap(document -> Observable.empty());
	}

	public Observable<Person> list() {
		return db.queryRows("select * from persons").map(row ->
				new Person(row.getString("firstname"), row.getString("lastname"),
						row.getString("address"), row.getString("postalCode"), row.getString("city"))
		);
	}
}
