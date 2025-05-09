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
package org.assertj.core.internal.chararrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSorted;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.testkit.CharArrays.arrayOf;
import static org.assertj.core.testkit.CharArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.CharArrays;
import org.assertj.core.internal.CharArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link CharArrays#assertIsSorted(AssertionInfo, char[])}</code>.
 * 
 * @author Joel Costigliola
 */
class CharArrays_assertIsSorted_Test extends CharArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = arrayOf('a', 'b', 'c', 'd', 'd');
  }

  @Test
  void should_pass_if_actual_is_sorted_in_ascending_order() {
    arrays.assertIsSorted(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_empty() {
    arrays.assertIsSorted(someInfo(), emptyArray());
  }

  @Test
  void should_pass_if_actual_contains_only_one_element() {
    arrays.assertIsSorted(someInfo(), arrayOf('d'));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertIsSorted(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_sorted_in_ascending_order() {
    AssertionInfo info = someInfo();
    actual = arrayOf('a', 'c', 'b');

    Throwable error = catchThrowable(() -> arrays.assertIsSorted(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeSorted(1, actual));
  }

  @Test
  void should_pass_if_actual_is_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), emptyArray());
  }

  @Test
  void should_pass_if_actual_contains_only_one_element_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), arrayOf('d'));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(),
                                                                                                                       null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf('A', 'c', 'b');

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertIsSorted(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures)
                    .failure(info, shouldBeSortedAccordingToGivenComparator(1, actual, comparatorForCustomComparisonStrategy()));
  }

}
