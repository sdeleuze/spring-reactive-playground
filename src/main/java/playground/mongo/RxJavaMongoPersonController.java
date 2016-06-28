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

import playground.Person;
import rx.Observable;
import rx.Single;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sebastien Deleuze
 * @author Mark Paluch
 */
@Profile("mongo")
@RestController
public class RxJavaMongoPersonController {

	private final ReactiveMongoRxJavaPersonRepository repository;

	@Autowired
	public RxJavaMongoPersonController(ReactiveMongoRxJavaPersonRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(path = "/rxjava/mongo", method = RequestMethod.POST)
	public Observable<Person> create(@RequestBody Observable<Person> personStream) {
		return this.repository.save(personStream);
	}

	@RequestMapping(path = "/rxjava/mongo", method = RequestMethod.GET)
	public Observable<Person> list() {
		return this.repository.findAll();
	}

	// TODO Manage {@code @PathVariable}
	@RequestMapping(path = "/rxjava/mongo/1", method = RequestMethod.GET)
	public Single<Person> findById() {
		return this.repository.findOne("1");
	}

}
