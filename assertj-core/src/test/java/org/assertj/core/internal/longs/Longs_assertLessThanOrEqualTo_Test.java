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
package org.assertj.core.internal.longs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Longs#assertLessThanOrEqualTo(AssertionInfo, Long, long)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Longs_assertLessThanOrEqualTo_Test extends LongsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertLessThanOrEqualTo(someInfo(), null, 8L))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_less_than_other() {
    longs.assertLessThanOrEqualTo(someInfo(), 6L, 8L);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other() {
    longs.assertLessThanOrEqualTo(someInfo(), 6L, 6L);
  }

  @Test
  void should_fail_if_actual_is_greater_than_other() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> longs.assertLessThanOrEqualTo(info, 8L, 6L));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeLessOrEqual(8L, 6L));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(),
                                                                                                                                 null,
                                                                                                                                 8L))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    longsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), 6L, -8L);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    longsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), 6L, -6L);
  }

  @Test
  void should_fail_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> longsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(info, -8L, 6L));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeLessOrEqual(-8L, 6L, absValueComparisonStrategy));
  }
}
