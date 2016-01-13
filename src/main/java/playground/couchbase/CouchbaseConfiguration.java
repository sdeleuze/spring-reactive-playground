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

package playground.couchbase;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.query.N1qlQuery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Sebastien Deleuze
 */
@Profile("couchbase")
@Configuration
public class CouchbaseConfiguration {

	@Value("${couchbase.node}")
	private String node;

	@Value("${couchbase.bucket}")
	private String bucket;

	@Bean
	AsyncBucket couchbaseDefaultBucket() {
		CouchbaseCluster cluster = CouchbaseCluster.create(node);
		Bucket b = cluster.openBucket(bucket);
		b.query(N1qlQuery.simple("CREATE PRIMARY INDEX ON `default`"));
		return b.async();
	}

}
