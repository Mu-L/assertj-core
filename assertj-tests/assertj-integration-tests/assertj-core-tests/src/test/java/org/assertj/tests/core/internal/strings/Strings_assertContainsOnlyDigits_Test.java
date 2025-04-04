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
package org.assertj.tests.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldContainOnlyDigits.shouldContainOnlyDigits;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.tests.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertContainsOnlyDigits(AssertionInfo info, CharSequence actual)}</code>.
 */
class Strings_assertContainsOnlyDigits_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_contains_only_digits() {
    strings.assertContainsOnlyDigits(someInfo(), "10");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsOnlyDigits(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_any_non_digit_character() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsOnlyDigits(someInfo(), "10$"))
                                                   .withMessage(shouldContainOnlyDigits("10$", '$', 2).create());
  }

  // See https://github.com/assertj/assertj/pull/342 - discussion on failing the assertion for empty CharSequence
  @Test
  void should_fail_if_actual_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsOnlyDigits(someInfo(), ""))
                                                   .withMessage(shouldContainOnlyDigits("").create());
  }
}
