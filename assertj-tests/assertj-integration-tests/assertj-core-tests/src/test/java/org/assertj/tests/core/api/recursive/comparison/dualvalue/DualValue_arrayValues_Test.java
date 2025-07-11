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
package org.assertj.tests.core.api.recursive.comparison.dualvalue;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class DualValue_arrayValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @Test
  void isActualAnArray_should_return_true_when_actual_is_an_array() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, array("a", "b"), "");
    // WHEN
    boolean actualIsAnArray = dualValue.isActualAnArray();
    // THEN
    then(actualIsAnArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualAnArray_should_return_false_when_actual_is_not_an_array(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean actualIsAnArray = dualValue.isActualAnArray();
    // THEN
    then(actualIsAnArray).isFalse();
  }

  @Test
  void isExpectedAnArray_should_return_true_when_expected_is_an_array() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", array("a", "b"));
    // WHEN
    boolean expectedIsAnArray = dualValue.isExpectedAnArray();
    // THEN
    then(expectedIsAnArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedAnArray_should_return_false_when_expected_is_not_an_array(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean expectedIsAnArray = dualValue.isExpectedAnArray();
    // THEN
    then(expectedIsAnArray).isFalse();
  }

}
