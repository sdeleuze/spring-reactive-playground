/*
 * Copyright 2002-2016 the original author or authors.
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

import java.time.Duration;

import reactor.core.publisher.Flux;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SseController {

	@GetMapping("/sse/string")
	Flux<String> string() {
		return Flux
			.interval(Duration.ofSeconds(1))
			.map(l -> "foo " + l);
	}

	@GetMapping("/sse/person")
	Flux<Person> person() {
		return Flux
			.interval(Duration.ofSeconds(1))
			.map(l -> new Person(Long.toString(l), "foo", "bar"));
	}

	@GetMapping("/sse-raw")
	Flux<ServerSentEvent<String>> sse() {
		return Flux
			.interval(Duration.ofSeconds(1))
			.map(l -> ServerSentEvent
					.builder("foo\nbar")
					.comment("bar\nbaz")
					.id(Long.toString(l))
					.build());
	}

}
