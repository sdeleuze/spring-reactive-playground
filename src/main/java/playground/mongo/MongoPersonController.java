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

import org.reactivestreams.Publisher;
import playground.Person;
import playground.repository.ReactiveRepository;
import reactor.Flux;
import reactor.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sebastien Deleuze
 */
@Profile("mongo")
@RestController
public class MongoPersonController {

	private final ReactiveRepository<Person> repository;

	@Autowired
	public MongoPersonController(MongoPersonRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(path = "/mongo", method = RequestMethod.POST)
	public Mono<Void> create(@RequestBody Publisher<Person> personStream) {
		return this.repository.insert(personStream);
	}

	@RequestMapping(path = "/mongo", method = RequestMethod.GET)
	public Flux<Person> list() {
		return this.repository.list();
	}

	// TODO Manage {@code @PathVariable}
	@RequestMapping(path = "/mongo/1", method = RequestMethod.GET)
	public Mono<Person> findById() {
		return this.repository.findById("1");
	}

}
