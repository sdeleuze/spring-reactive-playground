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

import org.springframework.data.domain.ReactivePage;
import org.springframework.data.domain.ReactivePageable;
import org.springframework.data.domain.Sort;

// See https://github.com/spring-projects/spring-reactive/pull/51
import reactor.Flux;


/**
 * Extension of {@link ReactiveCrudRepository} to provide additional methods to retrieve entities using the pagination and
 * sorting abstraction.
 *
 * @author Sebastien Deleuze
 */
@NoRepositoryBean
public interface ReactivePagingAndSortingRepository<T, ID extends Serializable> extends ReactiveCrudRepository<T, ID> {

	// TODO We should see how to expose MongoDB {@code FindPublisher} specific features
	Flux<T> findAll(Sort sort);

	ReactivePage<T> findAll(ReactivePageable pageable);

}
