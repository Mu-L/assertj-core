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
package org.assertj.core.testkit;

public class TolkienCharacter {

  public static TolkienCharacter of(String name, Integer age, Race race) {

    return new TolkienCharacter(name, age, race);
  }

  public enum Race {
    HOBBIT, MAIA, ELF, DWARF, MAN, DRAGON
  }

  public final String name;
  public final Integer age;
  public final Race race;

  private TolkienCharacter(String name, Integer age, Race race) {

    this.name = name;
    this.age = age;
    this.race = race;
  }

  public String getName() {

    return name;
  }

  public Integer getAge() {

    return age;
  }

  public Race getRace() {

    return race;
  }

  @Override
  public String toString() {
    return "TolkienCharacter [name=%s, age=%s, race=%s]".formatted(name, age, race);
  }

}
