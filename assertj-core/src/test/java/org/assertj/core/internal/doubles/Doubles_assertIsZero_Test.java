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
package org.assertj.core.internal.doubles;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.testkit.TestData.someInfo;

import org.assertj.core.internal.DoublesBaseTest;
import org.junit.jupiter.api.Test;

class Doubles_assertIsZero_Test extends DoublesBaseTest {

  @Test
  void should_succeed_since_actual_is_zero() {
    doubles.assertIsZero(someInfo(), 0.0d);
  }

  @Test
  void should_fail_since_actual_is_not_zero() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsZero(someInfo(), 2.0d))
                                                   .withMessage(shouldBeEqualMessage("2.0", "0.0"));

  }

  @Test
  void should_fail_since_actual_is_negative_zero_and_not_primitive() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsZero(someInfo(), -0.0d))
                                                   .withMessage(shouldBeEqualMessage("-0.0", "0.0"));
  }

  @Test
  void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertIsZero(someInfo(), 0.0d);
  }

  @Test
  void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doublesWithAbsValueComparisonStrategy.assertIsZero(someInfo(),
                                                                                                                        2.0d))
                                                   .withMessage(shouldBeEqualMessage("2.0", "0.0"));
  }

}
