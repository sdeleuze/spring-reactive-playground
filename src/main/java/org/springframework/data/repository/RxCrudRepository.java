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

import rx.Observable;
import rx.Single;

/**
 * Interface for generic CRUD operations on a repository for a specific type exposed
 * thanks to RxJava 1 {@code Observable} and {@code Single} types.
 *
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
 * @see CrudRepository
 */
@NoRepositoryBean
public interface RxCrudRepository<T, ID extends Serializable> extends Repository<T, ID> {

	<S extends T> Single<S> save(S entity);

	<S extends T> Observable<S> save(Observable<S> entities);

	Single<T> findOne(ID id);

	Single<Boolean> exists(ID id);

	// TODO We should see how to expose MongoDB {@code FindObservable} specific features
	Observable<T> findAll();

	// TODO We should see how to expose MongoDB {@code FindObservable} specific features
	Observable<T> findAll(Observable<ID> ids);

	Single<Long> count();

	// TODO See https://github.com/ReactiveX/RxJava/issues/3037 about {@code Observable<Void>}
	Observable<Void> delete(ID id);

	// TODO See https://github.com/ReactiveX/RxJava/issues/3037 about {@code Observable<Void>}
	Observable<Void> delete(T entity);

	// TODO See https://github.com/ReactiveX/RxJava/issues/3037 about {@code Observable<Void>}
	Observable<Void> delete(Observable<? extends T> entities);

	// TODO See https://github.com/ReactiveX/RxJava/issues/3037 about {@code Observable<Void>}
	Observable<Void> deleteAll();

}
