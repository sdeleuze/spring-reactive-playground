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

package playground;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.reactive.codec.encoder.JacksonJsonEncoder;
import org.springframework.reactive.codec.encoder.JsonObjectEncoder;
import org.springframework.reactive.codec.encoder.MessageToByteEncoder;
import org.springframework.reactive.codec.encoder.StringEncoder;
import org.springframework.reactive.web.dispatch.DispatcherHandler;
import org.springframework.reactive.web.dispatch.SimpleHandlerResultHandler;
import org.springframework.reactive.web.dispatch.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.reactive.web.dispatch.method.annotation.RequestMappingHandlerMapping;
import org.springframework.reactive.web.dispatch.method.annotation.ResponseBodyResultHandler;
import org.springframework.reactive.web.http.HttpServer;
import org.springframework.reactive.web.http.reactor.ReactorHttpServer;

/**
 * @author Sebastien Deleuze
 */
@Configuration
public class Application {

	public static void main(String[] args) throws Exception {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("playground");
		DispatcherHandler dispatcherHandler = new DispatcherHandler();
		dispatcherHandler.setApplicationContext(context);

		HttpServer server = new ReactorHttpServer();
		server.setPort(8080);
		server.setHandler(dispatcherHandler);
		server.afterPropertiesSet();
		server.start();

		CompletableFuture<Void> stop = new CompletableFuture<>();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			stop.complete(null);
		}));

		synchronized (stop) {
			stop.wait();
		}

	}

	@Bean
	RequestMappingHandlerMapping handlerMapping() {
		return new RequestMappingHandlerMapping();
	}

	@Bean
	RequestMappingHandlerAdapter handlerAdapter() {
		return new RequestMappingHandlerAdapter();
	}

	@Bean
	ResponseBodyResultHandler responseBodyResultHandler() {
		List<MessageToByteEncoder<?>> serializers = Arrays.asList(new StringEncoder(), new JacksonJsonEncoder());
		List<MessageToByteEncoder<ByteBuffer>> preProcessors = Arrays.asList(new JsonObjectEncoder());
		return new ResponseBodyResultHandler(serializers, preProcessors);
	}

	@Bean
	SimpleHandlerResultHandler simpleResultHandler() {
		return new SimpleHandlerResultHandler();
	}

	@Bean
	ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json().build();
	}

	@Bean
	MongoDatabase mongoDatabase() {
		return MongoClients.create().getDatabase("reactive-playground");
	}

}
