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
package org.assertj.core.api.localdatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class LocalDateTimeAssert_hasYear_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDateTime actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasYear(2022));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_in_given_year() {
    // GIVEN
    LocalDateTime actual = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
    // WHEN/THEN
    then(actual).hasYear(2022);
  }

  @Test
  void should_fail_if_actual_is_not_in_given_year() {
    // GIVEN
    LocalDateTime actual = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
    int expectedYear = 2021;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasYear(expectedYear));
    // THEN
    then(assertionError).hasMessage(shouldHaveDateField(actual, "year", expectedYear).create());
  }

}
