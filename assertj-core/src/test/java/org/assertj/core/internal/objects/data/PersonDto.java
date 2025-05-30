/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal.objects.data;

import java.util.Date;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class PersonDto {
  public Date dateOfBirth;
  public String name;
  public Optional<String> phone;
  public OptionalInt age;
  public OptionalLong id;
  public OptionalDouble weight;
  public HomeDto home = new HomeDto();
  public PersonDto neighbour;

  public PersonDto(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "PersonDto [name=" + name + ", home=" + home + "]";
  }
}