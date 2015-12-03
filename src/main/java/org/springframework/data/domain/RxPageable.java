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

package org.springframework.data.domain;

import rx.Single;

/**
 * Abstract interface for RxJava 1 based pagination information.
 *
 * @author Sebastien Deleuze
 * @see Pageable
 */
public interface RxPageable {

		/**
	 * Returns the page to be returned.
	 *
	 * @return the page to be returned.
	 */
	int getPageNumber();

	/**
	 * Returns the number of items to be returned.
	 *
	 * @return the number of items of that page
	 */
	int getPageSize();

	/**
	 * Returns the offset to be taken according to the underlying page and page size.
	 *
	 * @return the offset to be taken
	 */
	int getOffset();

	/**
	 * Returns the sorting parameters.
	 *
	 * @return
	 */
	Sort getSort();

	/**
	 * Returns the {@link RxPageable} requesting the next {@link Page}.
	 *
	 * @return
	 */
	Single<RxPageable> next();

	/**
	 * Returns the previous {@link RxPageable} or the first {@link RxPageable} if the current one already is the first one.
	 *
	 * @return
	 */
	Single<RxPageable> previousOrFirst();

	/**
	 * Returns the {@link RxPageable} requesting the first page.
	 *
	 * @return
	 */
	Single<RxPageable> first();

	/**
	 * Returns whether there's a previous {@link RxPageable} we can access from the current one. Will return
	 * {@literal false} in case the current {@link RxPageable} already refers to the first page.
	 *
	 * @return
	 */
	boolean hasPrevious();

}
