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
package org.assertj.core.api;

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.Longs;
import org.assertj.core.util.CheckReturnValue;

public class AtomicLongAssert extends AbstractAssertWithComparator<AtomicLongAssert, AtomicLong> {

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Comparables comparables = new Comparables();

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Longs longs = Longs.instance();

  public AtomicLongAssert(AtomicLong actual) {
    super(actual, AtomicLongAssert.class);
  }

  /**
   * Verifies that the actual atomic has a value in [start, end] range (start included, end included).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLong actual =  new AtomicLong(5);
   *  
   * // assertions succeed
   * assertThat(actual).hasValueBetween(4, 6)
   *                   .hasValueBetween(4, 5)
   *                   .hasValueBetween(5, 6);
   * 
   * // assertions fail
   * assertThat(actual).hasValueBetween(6, 8)
   *                   .hasValueBetween(0, 4);</code></pre>
   * 
   * @param startInclusive the start value (inclusive).
   * @param endInclusive the end value (inclusive).
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not in [start, end] range.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueBetween(long startInclusive, long endInclusive) {
    isNotNull();
    longs.assertIsBetween(info, actual.get(), startInclusive, endInclusive);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(1)).hasValueLessThan(2);
   * assertThat(new AtomicLong(-2)).hasValueLessThan(-1);
   * 
   * // assertions will fail:
   * assertThat(new AtomicLong(1)).hasValueLessThan(0)
   *                              .hasValueLessThan(1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueLessThan(long other) {
    isNotNull();
    longs.assertLessThan(info, actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(1)).hasValueLessThanOrEqualTo(1)
   *                              .hasValueLessThanOrEqualTo(2);
   * assertThat(new AtomicLong(-2)).hasValueLessThanOrEqualTo(-1);
   * 
   * // assertion will fail:
   * assertThat(new AtomicLong(1)).hasValueLessThanOrEqualTo(0);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is greater than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueLessThanOrEqualTo(long other) {
    isNotNull();
    longs.assertLessThanOrEqualTo(info, actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(1)).hasValueGreaterThan(0);
   * assertThat(new AtomicLong(-1)).hasValueGreaterThan(-2);
   * 
   * // assertions will fail:
   * assertThat(new AtomicLong(1)).hasValueGreaterThan(2)
   *                              .hasValueGreaterThan(1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual atomic value is equal to or less than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueGreaterThan(long other) {
    isNotNull();
    longs.assertGreaterThan(info, actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(1)).hasValueGreaterThanOrEqualTo(0)     
   *                              .hasValueGreaterThanOrEqualTo(1);
   * assertThat(new AtomicLong(-1)).hasValueGreaterThanOrEqualTo(-2);
   * 
   * // assertion will fail:
   * assertThat(new AtomicLong(1)).hasValueGreaterThanOrEqualTo(2);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is less than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueGreaterThanOrEqualTo(long other) {
    isNotNull();
    longs.assertGreaterThanOrEqualTo(info, actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a positive value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLong(42)).hasPositiveValue();
   *
   * // assertions will fail
   * assertThat(new AtomicLong(0)).hasPositiveValue();
   * assertThat(new AtomicLong(-1)).hasPositiveValue();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not positive.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasPositiveValue() {
    isNotNull();
    longs.assertIsPositive(info, actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a non positive value (negative or equal zero).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicLong(-42)).hasNonPositiveValue();
   * assertThat(new AtomicLong(0)).hasNonPositiveValue();
   *
   * // assertion will fail
   * assertThat(new AtomicLong(42)).hasNonPositiveValue();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non positive.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasNonPositiveValue() {
    isNotNull();
    longs.assertIsNotPositive(info, actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a negative value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLong(-42)).hasNegativeValue();
   *
   * // assertions will fail
   * assertThat(new AtomicLong(0)).hasNegativeValue();
   * assertThat(new AtomicLong(42)).hasNegativeValue();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasNegativeValue() {
    isNotNull();
    longs.assertIsNegative(info, actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a non negative value (positive or equal zero).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicLong(42)).hasNonNegativeValue();
   * assertThat(new AtomicLong(0)).hasNonNegativeValue();
   *
   * // assertion will fail
   * assertThat(new AtomicLong(-42)).hasNonNegativeValue();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasNonNegativeValue() {
    isNotNull();
    longs.assertIsNotNegative(info, actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with Long:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(11)).hasValueCloseTo(10, withinPercentage(20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat(new AtomicLong(11)).hasValueCloseTo(10, withinPercentage(10));
   *
   * // assertion will fail
   * assertThat(new AtomicLong(11)).hasValueCloseTo(10, withinPercentage(5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@link Percentage} is {@code null}.
   * @throws AssertionError if the actual atomic value is not close enough to the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueCloseTo(long expected, Percentage percentage) {
    isNotNull();
    longs.assertIsCloseToPercentage(info, actual.get(), expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value close to the given one within the given offset.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion: 
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Long)} or {@link Offset#offset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Long)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Long)} implies a <b>strict</b> comparison, 
   * use {@link Assertions#within(Long)} to get the old behavior. 
   * <p>
   * Example with Long:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(5)).hasValueCloseTo(7L, within(3L))
   *                              .hasValueCloseTo(7L, byLessThan(3L));
   *
   * // if the difference is exactly equals to the offset, it's ok ...
   * assertThat(new AtomicLong(5)).hasValueCloseTo(7L, within(2L));
   * // ... but not with byLessThan which implies a strict comparison
   * assertThat(new AtomicLong(5)).hasValueCloseTo(7L, byLessThan(2L)); // FAIL
   *
   * // assertion will fail
   * assertThat(new AtomicLong(5)).hasValueCloseTo(7L, within(1L));
   * assertThat(new AtomicLong(5)).hasValueCloseTo(7L, byLessThan(1L));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given allowed {@link Offset}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@link Offset} is {@code null}.
   * @throws AssertionError if the actual atomic value is not close enough to the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueCloseTo(long expected, Offset<Long> offset) {
    isNotNull();
    longs.assertIsCloseTo(info, actual.get(), expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual atomic has the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLong(42)).hasValue(42);
   *
   * // assertion will fail
   * assertThat(new AtomicLong(42)).hasValue(0);</code></pre>
   * 
   * @param expectedValue the value not expected .
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValue(long expectedValue) {
    isNotNull();
    long actualValue = actual.get();
    if (!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldHaveValue(actual, expectedValue));
    }
    return myself;
  }

  /**
   * Verifies that the actual atomic has not the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLong(42)).doesNotHaveValue(0);
   *
   * // assertion will fail
   * assertThat(new AtomicLong(42)).doesNotHaveValue(42);</code></pre>
   * 
   * @param expectedValue the value not expected .
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert doesNotHaveValue(long expectedValue) {
    isNotNull();
    long actualValue = actual.get();
    if (objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldNotContainValue(actual, expectedValue));
    }
    return myself;
  }

  @Override
  @CheckReturnValue
  public AtomicLongAssert usingComparator(Comparator<? super AtomicLong> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public AtomicLongAssert usingComparator(Comparator<? super AtomicLong> customComparator, String customComparatorDescription) {
    longs = new Longs(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public AtomicLongAssert usingDefaultComparator() {
    longs = Longs.instance();
    return super.usingDefaultComparator();
  }

}
