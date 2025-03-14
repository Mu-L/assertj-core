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
package org.assertj.tests.core.api.optionalint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.OptionalInt;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class OptionalIntAssert_hasValue_Test {

  @SuppressWarnings("OptionalAssignedToNull")
  @Test
  void should_fail_when_OptionalInt_is_null() {
    // GIVEN
    OptionalInt nullActual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(nullActual).hasValue(10));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_OptionalInt_has_expected_value() {
    assertThat(OptionalInt.of(10)).hasValue(10);
  }

  @Test
  void should_fail_if_OptionalInt_does_not_have_expected_value() {
    // GIVEN
    OptionalInt actual = OptionalInt.of(5);
    int expectedValue = 10;
    // WHEN
    AssertionFailedError error = catchThrowableOfType(AssertionFailedError.class,
                                                      () -> assertThat(actual).hasValue(expectedValue));
    // THEN
    then(error).hasMessage(shouldContain(actual, expectedValue).create());
    then(error.getActual().getStringRepresentation()).isEqualTo(String.valueOf(actual.getAsInt()));
    then(error.getExpected().getStringRepresentation()).isEqualTo(String.valueOf(expectedValue));
  }

  @Test
  void should_fail_if_OptionalInt_is_empty() {
    // GIVEN
    int expectedValue = 10;
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(OptionalInt.empty()).hasValue(expectedValue));
    // THEN
    then(error).hasMessage(shouldContain(expectedValue).create());
  }
}
