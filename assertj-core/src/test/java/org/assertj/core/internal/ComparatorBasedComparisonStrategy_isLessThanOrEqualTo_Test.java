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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Locale;
import org.junit.jupiter.api.Test;

/**
 * @author Joel Costigliola
 */
class ComparatorBasedComparisonStrategy_isLessThanOrEqualTo_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  void should_pass() {
    String string = "string";
    String greaterString = "STRINGA";
    assertThat(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(string, greaterString)).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(string, "STRING")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(string, string)).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(greaterString, string)).isFalse();
    String lowerString = "stringA";
    assertThat(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(string, lowerString)).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(lowerString, string)).isFalse();
  }

  @Test
  void should_fail_if_a_parameter_is_not_comparable() {
    assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(Locale.ROOT,
                                                                                                                               Locale.US));
  }

}
