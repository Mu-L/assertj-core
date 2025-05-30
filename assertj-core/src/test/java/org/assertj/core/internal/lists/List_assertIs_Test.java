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
package org.assertj.core.internal.lists;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldBeAtIndex.shouldBeAtIndex;
import static org.assertj.core.testkit.TestData.someIndex;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.TestCondition;
import org.assertj.core.data.Index;
import org.assertj.core.internal.Lists;
import org.assertj.core.internal.ListsBaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Lists#assertIs(AssertionInfo, List, org.assertj.core.api.Condition, Index)}</code> .
 * 
 * @author Bo Gotthardt
 */
class List_assertIs_Test extends ListsBaseTest {
  private static TestCondition<String> condition;
  private static List<String> actual = newArrayList("Yoda", "Luke", "Leia");

  @BeforeAll
  static void setUpOnce() {
    condition = new TestCondition<>();
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> lists.assertIs(someInfo(), null, condition, someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      List<String> empty = emptyList();
      lists.assertIs(someInfo(), empty, condition, someIndex());
    }).withMessage(actualIsEmpty());
  }

  @Test
  void should_throw_error_if_Index_is_null() {
    assertThatNullPointerException().isThrownBy(() -> lists.assertIs(someInfo(), actual, condition, null))
                                    .withMessage("Index should not be null");
  }

  @Test
  void should_throw_error_if_Index_is_out_of_bounds() {
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> lists.assertIs(someInfo(), actual,
                                                                                               condition, atIndex(6)))
                                                              .withMessageContaining("Index should be between <0> and <2> (inclusive) but was:%n <6>".formatted());
  }

  @Test
  void should_throw_error_if_Condition_is_null() {
    assertThatNullPointerException().isThrownBy(() -> lists.assertIs(someInfo(), actual, null, someIndex()))
                                    .withMessage("The condition to evaluate should not be null");
  }

  @Test
  void should_fail_if_actual_does_not_satisfy_condition_at_index() {
    condition.shouldMatch(false);
    AssertionInfo info = someInfo();
    Index index = atIndex(1);

    Throwable error = catchThrowable(() -> lists.assertIs(info, actual, condition, index));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeAtIndex(actual, condition, index, "Luke"));
  }

  @Test
  void should_pass_if_actual_satisfies_condition_at_index() {
    condition.shouldMatch(true);
    lists.assertIs(someInfo(), actual, condition, someIndex());
  }
}
