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
package org.assertj.core.internal.doublearrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.DoubleArrays.arrayOf;
import static org.assertj.core.testkit.DoubleArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoubleArrays;
import org.assertj.core.internal.DoubleArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link DoubleArrays#assertEndsWith(AssertionInfo, double[], double[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Florent Biville
 */
class DoubleArrays_assertEndsWith_Test extends DoubleArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = arrayOf(6d, 8d, 10d, 12d);
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertEndsWith(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual = emptyArray();
    arrays.assertEndsWith(someInfo(), actual, emptyArray());
  }

  @Test
  void should_pass_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    arrays.assertEndsWith(someInfo(), actual, emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertEndsWith(someInfo(), null, arrayOf(8d)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    double[] sequence = { 6d, 8d, 10d, 12d, 20d, 22d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertEndsWith(someInfo(), actual, sequence))
                                                   .withMessage(shouldEndWith(actual, sequence).create());
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence() {
    double[] sequence = { 20d, 22d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertEndsWith(someInfo(), actual, sequence))
                                                   .withMessage(shouldEndWith(actual, sequence).create());
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only() {
    double[] sequence = { 6d, 20d, 22d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertEndsWith(someInfo(), actual, sequence))
                                                   .withMessage(shouldEndWith(actual, sequence).create());
  }

  @Test
  void should_pass_if_actual_ends_with_sequence() {
    arrays.assertEndsWith(someInfo(), actual, arrayOf(8d, 10d, 12d));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertEndsWith(someInfo(), actual, arrayOf(6d, 8d, 10d, 12d));
  }

  @Test
  void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(),
                                                                                                        actual,
                                                                                                        null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(),
                                                                                                                       null,
                                                                                                                       arrayOf(-8d)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    double[] sequence = { 6d, -8d, 10d, 12d, 20d, 22d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(),
                                                                                                                       actual,
                                                                                                                       sequence))
                                                   .withMessage(shouldEndWith(actual, sequence,
                                                                              absValueComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence_according_to_custom_comparison_strategy() {
    double[] sequence = { 20d, 22d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(),
                                                                                                                       actual,
                                                                                                                       sequence))
                                                   .withMessage(shouldEndWith(actual, sequence,
                                                                              absValueComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    double[] sequence = { 6d, 20d, 22d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(),
                                                                                                                       actual,
                                                                                                                       sequence))
                                                   .withMessage(shouldEndWith(actual, sequence,
                                                                              absValueComparisonStrategy).create());
  }

  @Test
  void should_pass_if_actual_ends_with_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, arrayOf(-8d, 10d, 12d));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, arrayOf(6d, -8d, 10d, 12d));
  }
}
