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
package org.assertj.tests.core.api.recursive.comparison.legacy;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.BDDAssertions.thenCode;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssumptionNotMetException;

import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_assumptions_Test extends WithLegacyIntrospectionStrategyBaseTest {

  @Test
  void should_ignore_test_when_one_of_the_assumption_fails() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("John");
    expected.home.address.number = 1;
    Person unexpected = new Person("John");
    unexpected.home.address.number = 2;
    // THEN
    assumeThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
    expectAssumptionNotMetException(() -> assumeThat(actual).usingRecursiveComparison().isEqualTo(unexpected));
  }

  @Test
  void should_run_test_when_all_assumptions_are_met() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("John");
    expected.home.address.number = 1;
    // THEN
    thenCode(() -> {
      assumeThat("foo").isNotNull()
                       .isNotEmpty()
                       .isEqualTo("foo");
      assumeThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
      assumeThat(expected).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(actual);
      assumeThat(actual).as("test description")
                        .withFailMessage("error message")
                        .withRepresentation(UNICODE_REPRESENTATION)
                        .usingRecursiveComparison()
                        .isEqualTo(expected);
    }).doesNotThrowAnyException();
  }

}
