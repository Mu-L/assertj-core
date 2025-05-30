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
package org.assertj.core.error;

import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a value is greater than or equal to another one
 * failed.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldBeGreaterOrEqual extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeGreaterOrEqual}</code>.
   * @param <T> guarantees that the values used in this factory have the same type.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T> ErrorMessageFactory shouldBeGreaterOrEqual(Comparable<? super T> actual, Comparable<? super T> other) {
    return new ShouldBeGreaterOrEqual(actual, other, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldBeGreaterOrEqual}</code>.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeGreaterOrEqual(Object actual, Object other,
                                                           ComparisonStrategy comparisonStrategy) {
    return new ShouldBeGreaterOrEqual(actual, other, comparisonStrategy);
  }

  private <T> ShouldBeGreaterOrEqual(T actual, T other, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto be greater than or equal to:%n  %s%n%s", actual, other, comparisonStrategy);
  }

}
