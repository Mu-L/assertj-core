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
package org.assertj.tests.guava.api;

import static com.google.common.collect.Range.closed;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.tests.guava.testkit.AssertionErrors.expectAssertionError;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Ilya Koshaleu
 */
class RangeSetAssert_containsAnyRangesOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).containsAnyRangesOf(asList(1, 2)));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Iterable<Integer> values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).containsAnyRangesOf(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("values").create());
  }

  @Test
  void should_fail_if_values_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 1));
    Iterable<Integer> values = emptySet();
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).containsAnyRangesOf(values));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Expecting values not to be empty");
  }

  @Test
  void should_fail_if_actual_does_not_contain_values() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 3));
    List<Integer> values = List.of(4, 5);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).containsAnyRangesOf(values));
    // THEN
    then(error).hasMessage(shouldContainAnyOf(actual, values).create());
  }

  @Test
  void should_pass_if_both_actual_and_values_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Iterable<Integer> values = emptySet();
    // WHEN/THEN
    assertThat(actual).containsAnyRangesOf(values);
  }

  @Test
  void should_pass_if_actual_contains_any_values() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(1, 10));
    Iterable<Integer> values = List.of(0, 1, 2, 12, 13);
    // WHEN/THEN
    assertThat(actual).containsAnyRangesOf(values);
  }

}
