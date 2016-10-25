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

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Sebastien Deleuze
 */
@Document
public class Person {

	private String id;

	private String firstname;

	private String lastname;


	public Person() {
	}

	public Person(String id, String firstname, String lastname) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Person person = (Person) o;

		if (id != null ? !id.equals(person.id) : person.id != null) {
			return false;
		}
		if (firstname != null ? !firstname.equals(person.firstname) :
				person.firstname != null) {
			return false;
		}
		return lastname != null ? lastname.equals(person.lastname) :
				person.lastname == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
		result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id='" + id + '\'' +
				", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				'}';
	}
}
