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

import reactor.Mono;

import org.springframework.core.convert.converter.Converter;

// See https://github.com/spring-projects/spring-reactive/pull/51
import org.springframework.rx.Flux;

/**
 * A Reactive Streams based slice of data that indicates whether there's a next or previous slice available.
 * Allows to obtain a {@link ReactivePageable} to request a previous or next {@link ReactiveSlice}.
 * 
 * @author Sebastien Deleuze
 * @see Slice
 */
public interface ReactiveSlice<T> {

	int getNumber();

	int getSize();

	int getNumberOfElements();

	Flux<T> getContent();

	boolean hasContent();

	Sort getSort();

	boolean isFirst();

	boolean isLast();

	boolean hasNext();

	boolean hasPrevious();

	Mono<ReactivePageable> nextPageable();

	Mono<ReactivePageable> previousPageable();

	<S> ReactiveSlice<S> map(Converter<? super T, ? extends S> converter);

}
