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
package org.assertj.core.internal.characters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeUpperCase.shouldBeUpperCase;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Characters;
import org.assertj.core.internal.CharactersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Characters#assertUpperCase(AssertionInfo, Character)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class Characters_assertUpperCase_Test extends CharactersBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> characters.assertUpperCase(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_uppercase() {
    characters.assertUpperCase(someInfo(), 'A');
  }

  @Test
  void should_fail_if_actual_is_not_uppercase() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> characters.assertUpperCase(info, 'a'));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeUpperCase('a'));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> charactersWithCaseInsensitiveComparisonStrategy.assertUpperCase(someInfo(),
                                                                                                                                     null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_uppercase_whatever_custom_comparison_strategy_is() {
    charactersWithCaseInsensitiveComparisonStrategy.assertUpperCase(someInfo(), 'A');
  }

  @Test
  void should_fail_if_actual_is_not_uppercase_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> charactersWithCaseInsensitiveComparisonStrategy.assertUpperCase(info, 'a'));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeUpperCase('a'));
  }
}
