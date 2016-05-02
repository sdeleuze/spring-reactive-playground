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
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.codec.support.ByteBufferDecoder;
import org.springframework.core.codec.support.ByteBufferEncoder;
import org.springframework.core.codec.support.JacksonJsonDecoder;
import org.springframework.core.codec.support.JacksonJsonEncoder;
import org.springframework.core.codec.support.StringDecoder;
import org.springframework.core.codec.support.StringEncoder;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.convert.support.ReactiveStreamsToCompletableFutureConverter;
import org.springframework.core.convert.support.ReactiveStreamsToRxJava1Converter;
import org.springframework.http.converter.reactive.CodecHttpMessageConverter;
import org.springframework.http.converter.reactive.HttpMessageConverter;
import org.springframework.http.converter.reactive.ResourceHttpMessageConverter;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.boot.HttpServer;
import org.springframework.http.server.reactive.boot.ReactorHttpServer;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.ResponseStatusExceptionHandler;
import org.springframework.web.reactive.result.SimpleResultHandler;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

/**
 * @author Sebastien Deleuze
 */
@Configuration
@PropertySource("classpath:application.properties")
public class Application {

	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();
		prop.load(Application.class.getClassLoader().getResourceAsStream("application.properties"));
		System.setProperty("spring.profiles.active", prop.getProperty("profiles"));

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("playground");
		DispatcherHandler dispatcherHandler = new DispatcherHandler();
		dispatcherHandler.setApplicationContext(context);

		HttpHandler httpHandler = WebHttpHandlerBuilder.webHandler(dispatcherHandler)
				.exceptionHandlers(new ResponseStatusExceptionHandler())
				.build();

		HttpServer server = new ReactorHttpServer();
		server.setPort(8080);
		server.setHandler(httpHandler);
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
		RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
		handlerAdapter.setConversionService(conversionService());
		return handlerAdapter;
	}

	@Bean
	ConversionService conversionService() {
		GenericConversionService service = new GenericConversionService();
		service.addConverter(new ReactiveStreamsToCompletableFutureConverter());
		service.addConverter(new ReactiveStreamsToRxJava1Converter());
		return service;
	}

	@Bean
	ResponseBodyResultHandler responseBodyResultHandler() {
		List<HttpMessageConverter<?>> converters =
					Arrays.asList(new ResourceHttpMessageConverter(),
							new CodecHttpMessageConverter<String>(new StringEncoder(), new StringDecoder()),
							new CodecHttpMessageConverter<Object>(new JacksonJsonEncoder(), new JacksonJsonDecoder()));

		return new ResponseBodyResultHandler(converters, conversionService());
	}

	@Bean
	SimpleResultHandler simpleHandlerResultHandler() {
		return new SimpleResultHandler(conversionService());
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
