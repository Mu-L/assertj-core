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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;

import java.util.Comparator;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.PersonDto;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_for_object_arrays_Test {

  private Person[] actual = { new Person("Sheldon"), new Person("Leonard") };

  // verify we don't need to cast actual to an Object as before when only Object assertions provided usingRecursiveComparison()
  @Test
  void should_be_directly_usable_with_iterables() {
    // GIVEN
    PersonDto[] expected = { new PersonDto("Sheldon"), new PersonDto("Leonard") };
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .isEqualTo(expected);
  }

  @Test
  void should_propagate_comparator_by_type() {
    // GIVEN
    Comparator<String> alwayEqualsString = ALWAYS_EQUALS_STRING;
    // WHEN
    RecursiveComparisonConfiguration assertion = assertThat(actual).usingComparatorForType(alwayEqualsString, String.class)
                                                                   .usingRecursiveComparison()
                                                                   .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(assertion.getTypeComparators().comparatorByTypes()).contains(entry(String.class, alwayEqualsString));
  }

}
