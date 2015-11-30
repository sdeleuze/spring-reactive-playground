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
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.reactivestreams.Publisher;
import playground.Person;
import reactor.Publishers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Sebastien Deleuze
 */
@Repository
public class MongoPersonRepository {

	private final ObjectMapper mapper;
	private final MongoCollection<Document> col;

	@Autowired
	public MongoPersonRepository(MongoDatabase db, ObjectMapper mapper) {
		this.mapper = mapper;
		this.col = db.getCollection("persons");
	}

	public Publisher<Void> insert(Publisher<Person> personStream) {
		return Publishers.flatMap(personStream, p -> {
			try {
				Document doc = Document.parse(mapper.writeValueAsString(p));
				return Publishers.completable(col.insertOne(doc));
			}
			catch (JsonProcessingException ex) {
				return Publishers.error(ex);
			}
		});
	}

	public Publisher<Person> list() {
		return Publishers.map(this.col.find(), doc -> {
			try {
				return mapper.readValue(doc.toJson(), Person.class);
			}
			catch (IOException ex) {
				throw new IllegalStateException(ex);
			}
		});
	}

}
