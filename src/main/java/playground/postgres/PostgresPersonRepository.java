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

import java.util.Arrays;
import java.util.function.Consumer;

import com.github.pgasync.Db;
import com.github.pgasync.ResultSet;
import com.github.pgasync.Row;
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

	public Observable<Void> insert(Observable<Person> personStream) {
		return personStream.flatMap(p ->
			Observable.create(subscriber -> {
				Consumer<ResultSet> onSuccess = result -> {
					subscriber.onNext(result);
					subscriber.onCompleted();
				};
				Consumer<Throwable> onError = throwable -> subscriber.onError(throwable);
				db.query("insert into persons(firstname, lastname, address, postalCode, city) values($1, $2, $3, $4, $5)",
					Arrays.asList(p.getFirstname(), p.getFirstname(), p.getAddress(), p.getPostalCode(), p.getCity()), onSuccess, onError);
			})).flatMap(document -> Observable.empty());
	}

	public Observable<Person> list() {
		return Observable.create(subscriber -> {
			Consumer<ResultSet> onSuccess = resultSet -> {
					// We can only get the multiple rows at the same time, more details on this feature request
					// https://github.com/alaisi/postgres-async-driver/issues/4#issuecomment-160980796
					for (Row row : resultSet) {
						subscriber.onNext(new Person(row.getString("firstname"), row.getString("lastname"),
								row.getString("address"), row.getString("postalCode"), row.getString("city")));
					}
					subscriber.onCompleted();
				};
				Consumer<Throwable> onError = throwable -> subscriber.onError(throwable);
				db.query("select * from persons", onSuccess, onError);
			});
	}
}
