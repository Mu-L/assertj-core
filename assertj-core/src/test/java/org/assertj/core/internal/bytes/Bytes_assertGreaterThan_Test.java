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
package org.assertj.core.internal.bytes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeGreater.shouldBeGreater;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Bytes#assertGreaterThan(AssertionInfo, Byte, byte)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Bytes_assertGreaterThan_Test extends BytesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertGreaterThan(someInfo(), null, (byte) 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other() {
    bytes.assertGreaterThan(someInfo(), (byte) 8, (byte) 6);
  }

  @Test
  void should_fail_if_actual_is_equal_to_other() {
    Throwable error = catchThrowable(() -> bytes.assertGreaterThan(someInfo(), (byte) 6, (byte) 6));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(someInfo(), shouldBeGreater((byte) 6, (byte) 6));
  }

  @Test
  void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> bytes.assertGreaterThan(info, (byte) 6, (byte) 8));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeGreater((byte) 6, (byte) 8));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    bytesWithAbsValueComparisonStrategy.assertGreaterThan(someInfo(), (byte) -8, (byte) 6);
  }

  @Test
  void should_fail_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    Throwable error = catchThrowable(() -> bytesWithAbsValueComparisonStrategy.assertGreaterThan(someInfo(), (byte) -6,
                                                                                                 (byte) 6));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(someInfo(), shouldBeGreater((byte) -6, (byte) 6, absValueComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> bytesWithAbsValueComparisonStrategy.assertGreaterThan(info, (byte) -6, (byte) 8));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeGreater((byte) -6, (byte) 8, absValueComparisonStrategy));
  }

}
