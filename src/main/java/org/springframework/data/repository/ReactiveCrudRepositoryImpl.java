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

package org.springframework.data.repository;

import java.io.Serializable;

import org.reactivestreams.Publisher;
import reactor.Flux;
import reactor.Mono;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

// See https://github.com/spring-projects/spring-reactive/pull/51


/**
 * @author Sebastien Deleuze
 */
public class ReactiveCrudRepositoryImpl<T, ID extends Serializable> implements ReactiveCrudRepository<T, ID> {

	private final ReactiveMongoTemplate template;

	private final Class<T> entityClass;


	public ReactiveCrudRepositoryImpl(ReactiveMongoTemplate template, Class<T> entityClass) {
		this.template = template;
		this.entityClass = entityClass;
	}

	@Override
	public <S extends T> Flux<S> save(Publisher<S> entities) {
		return Flux.wrap(entities).flatMap(entity -> Flux.wrap(this.template.insert(entity)).map(v -> entity));
	}

	@Override
	public <S extends T> Mono<S> save(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Flux<T> findAll() {
		return this.template.findAll(this.entityClass);
	}

	@Override
	public Mono<T> findOne(ID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mono<Boolean> exists(ID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Flux<T> findAll(Publisher<ID> ids) {
		return Flux.error(new UnsupportedOperationException());
	}

	@Override
	public Mono<Long> count() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mono<Void> delete(ID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mono<Void> delete(T entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mono<Void> delete(Publisher<? extends T> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mono<Void> deleteAll() {
		throw new UnsupportedOperationException();
	}

}
