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
package org.assertj.tests.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

class OptionalAssert_flatMap_Test {

  private static final Function<String, Optional<String>> UPPER_CASE_OPTIONAL_STRING = s -> (s == null)
      ? Optional.empty()
      : Optional.of(s.toUpperCase());

  @Test
  void should_fail_when_optional_is_null() {
    // GIVEN
    @SuppressWarnings("OptionalAssignedToNull")
    Optional<String> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).flatMap(UPPER_CASE_OPTIONAL_STRING));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_when_optional_is_empty() {
    assertThat(Optional.<String> empty()).flatMap(UPPER_CASE_OPTIONAL_STRING).isEmpty();
  }

  @Test
  void should_pass_when_optional_contains_a_value() {
    assertThat(Optional.of("present")).contains("present")
                                      .flatMap(UPPER_CASE_OPTIONAL_STRING)
                                      .contains("PRESENT");
  }

}
