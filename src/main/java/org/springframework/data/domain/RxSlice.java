/*
 * Copyright 2014-2015 the original author or authors.
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
package org.springframework.data.domain;

import rx.Observable;
import rx.Single;

import org.springframework.core.convert.converter.Converter;

/**
 * A Reactive Streams based slice of data that indicates whether there's a next or previous slice available.
 * Allows to obtain a {@link ReactivePageable} to request a previous or next {@link RxSlice}.
 *
 * TODO cant extend Observable since this is not an interface, maybe it could extend another interface?
 * 
 * @author Sebastien Deleuze
 */
public interface RxSlice<T> {

	int getNumber();

	int getSize();

	int getNumberOfElements();

	Observable<T> getContent();

	boolean hasContent();

	Sort getSort();

	boolean isFirst();

	boolean isLast();

	boolean hasNext();

	boolean hasPrevious();

	Single<ReactivePageable> nextPageable();

	Single<ReactivePageable> previousPageable();

	<S> RxSlice<S> map(Converter<? super T, ? extends S> converter);

}
