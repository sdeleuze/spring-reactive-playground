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

import com.fasterxml.jackson.annotation.JsonView;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sebastien Deleuze
 */
@RestController
public class JsonViewController {

	private final JacksonViewBean jacksonViewBean =
			new JacksonViewBean("with", "with", "without");

	@GetMapping(path = "/jackson/view")
	@JsonView(MyJacksonView1.class)
	JacksonViewBean view() {
		return jacksonViewBean;
	}

	// TODO Does not work yet when wrapped in Flux or Mono because in that case the MethodParameter can't be retrieved with ResolvableType#getSource(), to be fixed in Spring Framework 5.0 M2
	@GetMapping(path = "/jackson/view-with-mono")
	@JsonView(MyJacksonView1.class)
	Mono<JacksonViewBean> viewWithMono() {
		return Mono.just(jacksonViewBean);
	}

	@GetMapping(path = "/jackson/full")
	JacksonViewBean full() {
		return jacksonViewBean;
	}


	private interface MyJacksonView1 {}

	private interface MyJacksonView2 {}

	private static class JacksonViewBean {

		public JacksonViewBean(String withView1, String withView2, String withoutView) {
			this.withView1 = withView1;
			this.withView2 = withView2;
			this.withoutView = withoutView;
		}

		@JsonView(MyJacksonView1.class)
		private String withView1;

		@JsonView(MyJacksonView2.class)
		private String withView2;

		private String withoutView;

		public String getWithView1() {
			return withView1;
		}

		public void setWithView1(String withView1) {
			this.withView1 = withView1;
		}

		public String getWithView2() {
			return withView2;
		}

		public void setWithView2(String withView2) {
			this.withView2 = withView2;
		}

		public String getWithoutView() {
			return withoutView;
		}

		public void setWithoutView(String withoutView) {
			this.withoutView = withoutView;
		}
	}

}
