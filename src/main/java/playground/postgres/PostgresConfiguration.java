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

package playground.postgres;

import com.github.pgasync.ConnectionPoolBuilder;
import com.github.pgasync.Db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sebastien Deleuze
 */
@Configuration
public class PostgresConfiguration {

	// You should create this table with the following query before using it:
	// CREATE TABLE persons (firstname text, lastname text, address text, postalCode text, city text);
	// You should also customize database, username and eventually set the password
	@Bean
	Db postgreSqlDb() {
		return new ConnectionPoolBuilder().hostname("localhost").port(5432)
				.database("seb").username("seb").poolSize(20).build();
	}

}
