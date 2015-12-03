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
import rx.Observable;
import rx.Single;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.ReactiveStreamsToRxJava1Converter;

/**
 * @author Sebastien Deleuze
 */
public class RxCrudRepositoryAdapter<T, ID extends Serializable> implements RxCrudRepository<T, ID> {

	private final ReactiveCrudRepository<T, ID> repository;
	private final GenericConverter converter = new ReactiveStreamsToRxJava1Converter();


	public RxCrudRepositoryAdapter(ReactiveCrudRepository<T, ID> repository) {
		this.repository = repository;
	}


	@Override
	public <S extends T> Single<S> save(S entity) {
		return toSingle(this.repository.save(entity));
	}

	@Override
	public <S extends T> Observable<S> save(Observable<S> entities) {
		return toObservable(this.repository.save(toPublisher(entities)));
	}

	@Override
	public Single<T> findOne(ID id) {
		return toSingle(this.repository.findOne(id));
	}

	@Override
	public Single<Boolean> exists(ID id) {
		return toSingle(this.repository.exists(id));
	}

	@Override
	public Observable<T> findAll() {
		return toObservable(this.repository.findAll());
	}

	@Override
	public Observable<T> findAll(Observable<ID> ids) {
		return toObservable(this.repository.findAll(toPublisher(ids)));
	}

	@Override
	public Single<Long> count() {
		return toSingle(this.repository.count());
	}

	@Override
	public Observable<Void> delete(ID id) {
		return toObservable(this.repository.delete(id));
	}

	@Override
	public Observable<Void> delete(T entity) {
		return toObservable(this.repository.delete(entity));
	}

	@Override
	public Observable<Void> delete(Observable<? extends T> entities) {
		return toObservable(this.repository.delete(toPublisher(entities)));
	}

	@Override
	public Observable<Void> deleteAll() {
		return toObservable(this.repository.deleteAll());
	}

	private <S> Single<S> toSingle(Publisher<S> publisher) {
		return (Single<S>)this.converter.convert(publisher, TypeDescriptor.valueOf(Publisher.class), TypeDescriptor.valueOf(Single.class));
	}

	private <S> Observable<S> toObservable(Publisher<S> publisher) {
		return (Observable<S>)this.converter.convert(publisher, TypeDescriptor.valueOf(Publisher.class), TypeDescriptor.valueOf(Observable.class));
	}

	private <S> Publisher<S> toPublisher(Single<S> publisher) {
		return (Publisher<S>)this.converter.convert(publisher, TypeDescriptor.valueOf(Single.class), TypeDescriptor.valueOf(Publisher.class));
	}

	private <S> Publisher<S> toPublisher(Observable<S> publisher) {
		return (Publisher<S>)this.converter.convert(publisher, TypeDescriptor.valueOf(Observable.class), TypeDescriptor.valueOf(Publisher.class));
	}

}
