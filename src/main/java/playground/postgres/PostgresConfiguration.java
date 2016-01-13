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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Sebastien Deleuze
 */
@Profile("postgres")
@Configuration
public class PostgresConfiguration {

	@Value("${postgres.hostname}")
	private String hostname;

	@Value("${postgres.port}")
	private int port;

	@Value("${postgres.database}")
	private String database;

	@Value("${postgres.username}")
	private String username;

	@Value("${postgres.password}")
	private String password;

	@Value("${postgres.poolsize}")
	private int poolSize;


	@Bean
	Db postgreSqlDb() {
		Db db = new ConnectionPoolBuilder().hostname(hostname).port(port)
				.database(database).username("seb").password(password).poolSize(poolSize).build();
		db.query("CREATE TABLE IF NOT EXISTS persons (id text PRIMARY KEY, firstname text, lastname text)", resultSet -> {}, Throwable::printStackTrace);
		return db;
	}

}
