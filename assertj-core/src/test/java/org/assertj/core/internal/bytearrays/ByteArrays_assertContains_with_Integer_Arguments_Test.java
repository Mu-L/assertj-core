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
package org.assertj.core.internal.bytearrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ByteArraysBaseTest;
import org.assertj.core.testkit.ByteArrays;
import org.assertj.core.testkit.IntArrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ByteArrays#assertContains(AssertionInfo, byte[], int[])}</code>.
 */
class ByteArrays_assertContains_with_Integer_Arguments_Test extends ByteArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values() {
    arrays.assertContains(someInfo(), actual, IntArrays.arrayOf(6));
  }

  @Test
  void should_pass_if_actual_contains_given_values_in_different_order() {
    arrays.assertContains(someInfo(), actual, IntArrays.arrayOf(8, 10));
  }

  @Test
  void should_pass_if_actual_contains_all_given_values() {
    arrays.assertContains(someInfo(), actual, IntArrays.arrayOf(6, 8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_more_than_once() {
    actual = ByteArrays.arrayOf(6, 8, 10, 10, 8);
    arrays.assertContains(someInfo(), actual, IntArrays.arrayOf(8));
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    arrays.assertContains(someInfo(), actual, IntArrays.arrayOf(6, 6));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual = ByteArrays.emptyArray();
    arrays.assertContains(someInfo(), actual, IntArrays.emptyArray());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContains(someInfo(), actual,
                                                                                           IntArrays.emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContains(someInfo(), actual, (int[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContains(someInfo(), null,
                                                                                           IntArrays.arrayOf(8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_values() {
    AssertionInfo info = someInfo();
    byte[] expected = { 6, 8, 9 };

    Throwable error = catchThrowable(() -> arrays.assertContains(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet((byte) 9)));
  }

  @Test
  void should_pass_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, IntArrays.arrayOf(6));
  }

  @Test
  void should_pass_if_actual_contains_given_values_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, IntArrays.arrayOf(-8, 10));
  }

  @Test
  void should_pass_if_actual_contains_all_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, IntArrays.arrayOf(6, -8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    actual = ByteArrays.arrayOf(6, -8, 10, 10, -8);
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, IntArrays.arrayOf(-8));
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, IntArrays.arrayOf(6, 6));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(),
                                                                                                                       actual,
                                                                                                                       IntArrays.emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(),
                                                                                                        actual,
                                                                                                        (int[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(),
                                                                                                                       null,
                                                                                                                       IntArrays.arrayOf(-8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContains(info, actual,
                                                                                             IntArrays.arrayOf(6, -8, 9)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContain(actual, ByteArrays.arrayOf(6, -8, 9), newLinkedHashSet((byte) 9),
                                                 absValueComparisonStrategy));
  }
}
