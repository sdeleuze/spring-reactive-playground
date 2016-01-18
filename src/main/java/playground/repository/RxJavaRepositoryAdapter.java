/*
 * Copyright 2002-2016 the original author or authors.
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

package playground.repository;

import org.reactivestreams.Publisher;
import rx.Observable;
import rx.Single;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.ReactiveStreamsToRxJava1Converter;

/**
 * @author Sebastien Deleuze
 */
public class RxJavaRepositoryAdapter<T> implements RxJavaRepository<T> {

	private ReactiveRepository<T> repository;

	private final GenericConverter converter = new ReactiveStreamsToRxJava1Converter();


	public RxJavaRepositoryAdapter(ReactiveRepository<T> repository) {
		this.repository = repository;
	}


	@Override
	public Single<Void> insert(Observable<T> elements) {
		return toSingle(this.repository.insert(toPublisher(elements)));
	}

	@Override
	public Observable<T> list() {
		return toObservable(this.repository.list());
	}

	@Override
	public Single<T> findById(String id) {
		return toSingle(this.repository.findById(id));
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
