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

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.springframework.context.support.StaticApplicationContext;
import org.springframework.reactive.codec.encoder.JacksonJsonEncoder;
import org.springframework.reactive.codec.encoder.JsonObjectEncoder;
import org.springframework.reactive.codec.encoder.StringEncoder;
import org.springframework.reactive.web.dispatch.DispatcherHandler;
import org.springframework.reactive.web.dispatch.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.reactive.web.dispatch.method.annotation.RequestMappingHandlerMapping;
import org.springframework.reactive.web.dispatch.method.annotation.ResponseBodyResultHandler;
import org.springframework.reactive.web.http.HttpServer;
import org.springframework.reactive.web.http.reactor.ReactorHttpServer;

/**
 * @author Sebastien Deleuze
 */
public class Application {

	public static void main(String[] args) throws Exception {

		StaticApplicationContext context = new StaticApplicationContext();
		context.registerSingleton("handlerMapping", RequestMappingHandlerMapping.class);
		context.registerSingleton("handlerAdapter", RequestMappingHandlerAdapter.class);
		context.getDefaultListableBeanFactory().registerSingleton("responseBodyResultHandler",
				new ResponseBodyResultHandler(Arrays.asList(new StringEncoder(), new JacksonJsonEncoder()), Arrays.asList(new JsonObjectEncoder())));
		context.registerSingleton("playgroundController", PlaygroundController.class);
		context.refresh();
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

}
