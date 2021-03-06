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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ElementsShouldBe.elementsShouldBe;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfy;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldContainEntry.shouldContainEntry;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainKey.shouldContainKey;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.error.ShouldContainValue.shouldContainValue;
import static org.assertj.core.error.ShouldContainValues.shouldContainValues;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainKey.shouldNotContainKey;
import static org.assertj.core.error.ShouldNotContainKeys.shouldNotContainKeys;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;
import static org.assertj.core.internal.Arrays.assertIsArray;
import static org.assertj.core.internal.CommonValidations.checkSizeBetween;
import static org.assertj.core.internal.CommonValidations.checkSizeGreaterThan;
import static org.assertj.core.internal.CommonValidations.checkSizeGreaterThanOrEqualTo;
import static org.assertj.core.internal.CommonValidations.checkSizeLessThan;
import static org.assertj.core.internal.CommonValidations.checkSizeLessThanOrEqualTo;
import static org.assertj.core.internal.CommonValidations.checkSizes;
import static org.assertj.core.internal.CommonValidations.hasSameSizeAsCheck;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsNull;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.error.UnsatisfiedRequirement;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Map}</code>s.
 *
 * @author Alex Ruiz
 * @author Nicolas François
 * @author dorzey
 */
public class Maps {

  private static final Maps INSTANCE = new Maps();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static Maps instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Conditions conditions = Conditions.instance();

  @VisibleForTesting
  Maps() {}

  public <K, V> void assertAllSatisfy(AssertionInfo info, Map<K, V> actual,
                                      BiConsumer<? super K, ? super V> entryRequirements) {
    requireNonNull(entryRequirements, "The BiConsumer<K, V> expressing the assertions requirements must not be null");
    assertNotNull(info, actual);

    List<UnsatisfiedRequirement> unsatisfiedRequirements = actual.entrySet().stream()
                                                                 .map(entry -> failsRequirements(entryRequirements, entry))
                                                                 .filter(Optional::isPresent)
                                                                 .map(Optional::get)
                                                                 .collect(toList());
    if (!unsatisfiedRequirements.isEmpty())
      throw failures.failure(info, elementsShouldSatisfy(actual, unsatisfiedRequirements, info));
  }

  private static <K, V> Optional<UnsatisfiedRequirement> failsRequirements(BiConsumer<? super K, ? super V> entryRequirements,
                                                                           Map.Entry<K, V> entry) {
    try {
      entryRequirements.accept(entry.getKey(), entry.getValue());
    } catch (AssertionError ex) {
      return Optional.of(new UnsatisfiedRequirement(entry, ex.getMessage()));
    }
    return Optional.empty();
  }

  public <K, V> void assertAnySatisfy(AssertionInfo info, Map<K, V> actual,
                                      BiConsumer<? super K, ? super V> entryRequirements) {
    requireNonNull(entryRequirements, "The BiConsumer<K, V> expressing the assertions requirements must not be null");
    assertNotNull(info, actual);

    List<UnsatisfiedRequirement> unsatisfiedRequirements = new ArrayList<>();
    for (Map.Entry<K, V> entry : actual.entrySet()) {
      Optional<UnsatisfiedRequirement> result = failsRequirements(entryRequirements, entry);
      if (!result.isPresent()) return; // entry satisfied the requirements
      unsatisfiedRequirements.add(result.get());
    }

    throw failures.failure(info, elementsShouldSatisfyAny(actual, unsatisfiedRequirements, info));
  }

  public <K, V> void assertNoneSatisfy(AssertionInfo info, Map<K, V> actual, BiConsumer<? super K, ? super V> entryRequirements) {
    requireNonNull(entryRequirements, "The BiConsumer<K, V> expressing the assertions requirements must not be null");
    assertNotNull(info, actual);

    List<Map.Entry<K, V>> erroneousEntries = actual.entrySet().stream()
                                                   .map(entry -> failsRestrictions(entry, entryRequirements))
                                                   .filter(Optional::isPresent)
                                                   .map(Optional::get)
                                                   .collect(toList());

    if (erroneousEntries.size() > 0) throw failures.failure(info, noElementsShouldSatisfy(actual, erroneousEntries));
  }

  private <V, K> Optional<Map.Entry<K, V>> failsRestrictions(Map.Entry<K, V> entry,
                                                             BiConsumer<? super K, ? super V> entryRequirements) {
    try {
      entryRequirements.accept(entry.getKey(), entry.getValue());
    } catch (AssertionError e) {
      // element is supposed not to meet the given restrictions
      return Optional.empty();
    }
    // element meets the given restrictions!
    return Optional.of(entry);
  }

  /**
   * Asserts that the given {@code Map} is {@code null} or empty.
   *
   * @param info contains information about the assertion.
   * @param actual the given map.
   * @throws AssertionError if the given {@code Map} is not {@code null} *and* contains one or more entries.
   */
  public void assertNullOrEmpty(AssertionInfo info, Map<?, ?> actual) {
    if (actual != null && !actual.isEmpty()) throw failures.failure(info, shouldBeNullOrEmpty(actual));
  }

  /**
   * Asserts that the given {@code Map} is empty.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} is not empty.
   */
  public void assertEmpty(AssertionInfo info, Map<?, ?> actual) {
    assertNotNull(info, actual);
    if (!actual.isEmpty()) throw failures.failure(info, shouldBeEmpty(actual));
  }

  /**
   * Asserts that the given {@code Map} is not empty.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} is empty.
   */
  public void assertNotEmpty(AssertionInfo info, Map<?, ?> actual) {
    assertNotNull(info, actual);
    if (actual.isEmpty()) throw failures.failure(info, shouldNotBeEmpty());
  }

  /**
   * Asserts that the number of entries in the given {@code Map} is equal to the expected one.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param expectedSize the expected size of {@code actual}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} is different than the expected one.
   */
  public void assertHasSize(AssertionInfo info, Map<?, ?> actual, int expectedSize) {
    assertNotNull(info, actual);
    checkSizes(actual, actual.size(), expectedSize, info);
  }

  /**
   * Asserts that the number of entries in the given {@code Map} is greater than the boundary.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param boundary  the given value to compare the size of {@code actual} to.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} is greater than the boundary.
   */
  public void assertHasSizeGreaterThan(AssertionInfo info, Map<?, ?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeGreaterThan(actual, boundary, actual.size(), info);
  }

  /**
   * Asserts that the number of entries in the given {@code Map} is greater than or equal to the boundary.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param boundary  the given value to compare the size of {@code actual} to.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} is greater than or equal to the boundary.
   */
  public void assertHasSizeGreaterThanOrEqualTo(AssertionInfo info, Map<?, ?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeGreaterThanOrEqualTo(actual, boundary, actual.size(), info);
  }

  /**
   * Asserts that the number of entries in the given {@code Map} is less than the boundary.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param boundary  the given value to compare the size of {@code actual} to.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} is less than the expected one.
   */
  public void assertHasSizeLessThan(AssertionInfo info, Map<?, ?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeLessThan(actual, boundary, actual.size(), info);
  }

  /**
   * Asserts that the number of entries in the given {@code Map} is less than or equal to the boundary.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param boundary  the given value to compare the size of {@code actual} to.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} is less than or equal to the boundary.
   */
  public void assertHasSizeLessThanOrEqualTo(AssertionInfo info, Map<?, ?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeLessThanOrEqualTo(actual, boundary, actual.size(), info);
  }

  /**
   * Asserts that the number of entries in the given {@code Map} is between the given lower and higher boundary (inclusive).
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of elements in the given array is not between the boundaries.
   */
  public void assertHasSizeBetween(AssertionInfo info, Map<?, ?> actual, int lowerBoundary, int higherBoundary) {
    assertNotNull(info, actual);
    checkSizeBetween(actual, lowerBoundary, higherBoundary, actual.size(), info);
  }

  /**
   * Asserts that the number of entries in the given {@code Map} has the same size as the other {@code Iterable}.
   *
   * @param info contains information about the assertion.
   * @param map the given {@code Map}.
   * @param other the group to compare
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, Map<?, ?> map, Iterable<?> other) {
    assertNotNull(info, map);
    hasSameSizeAsCheck(info, map, other, map.size());
  }

  /**
   * Asserts that the number of entries in the given {@code Map} has the same size as the other array.
   *
   * @param info contains information about the assertion.
   * @param map the given {@code Map}.
   * @param other the group to compare
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, Map<?, ?> map, Object other) {
    assertNotNull(info, map);
    assertIsArray(info, other);
    hasSameSizeAsCheck(info, map, other, map.size());
  }

  /**
   * Asserts that the size of the given {@code Map} is equal to the size of the other {@code Map}.
   *
   * @param info contains information about the assertion.
   * @param map the given {@code Map}.
   * @param other the other {@code Map} to compare
   * @throws NullPointerException if the other {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the size of the given {@code Map} is not equal to the other {@code Map} size
   */
  public void assertHasSameSizeAs(AssertionInfo info, Map<?, ?> map, Map<?, ?> other) {
    assertNotNull(info, map);
    hasSameSizeAsCheck(info, map, other, map.size());
  }

  /**
   * Asserts that the given {@code Map} contains the given entries, in any order.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param entries the entries that are expected to be in the given {@code Map}.
   * @throws NullPointerException if the array of entries is {@code null}.
   * @throws IllegalArgumentException if the array of entries is empty.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} does not contain the given entries.
   */
  public <K, V> void assertContains(AssertionInfo info, Map<K, V> actual,
                                    Map.Entry<? extends K, ? extends V>[] entries) {
    failIfNull(entries);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (actual.isEmpty() && entries.length == 0) return;
    failIfEntriesIsEmptyEmptySinceActualIsNotEmpty(info, actual, entries);
    failIfAnyEntryNotFoundInActualMap(info, actual, entries);
  }

  /**
   * Asserts that the given {@code Map} contains the entries of the other {@code Map}, in any order.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param other the {@code Map} of entries that are expected to be in the given {@code Map}.
   * @throws NullPointerException if the other map is {@code null}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} does not contain the entries of the other {@code Map}.
   */
  public <K, V> void assertContainsAllEntriesOf(AssertionInfo info, Map<K, V> actual,
                                                Map<? extends K, ? extends V> other) {
    failIfNull(other);
    assertNotNull(info, actual);
    // assertion passes if other is empty since actual contains all other entries.
    if (other.isEmpty()) return;
    failIfAnyEntryNotFoundInActualMap(info, actual, other.entrySet().toArray(new Map.Entry[0]));
  }

  public <K, V> void assertContainsAnyOf(AssertionInfo info, Map<K, V> actual,
                                         Map.Entry<? extends K, ? extends V>[] entries) {
    failIfNull(entries);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (actual.isEmpty() && entries.length == 0) return;
    failIfEntriesIsEmptyEmptySinceActualIsNotEmpty(info, actual, entries);
    for (Map.Entry<? extends K, ? extends V> entry : entries) {
      if (containsEntry(actual, entry)) return;
    }
    throw failures.failure(info, shouldContainAnyOf(actual, entries));
  }

  /**
   * Verifies that the given {@code Map} contains the value for given {@code key} that satisfy given {@code valueCondition}.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param key he given key to check.
   * @param valueCondition the given condition for check value.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given {@code key}.
   * @throws AssertionError if the actual map contains the given key, but value not match the given {@code valueCondition}.
   * @since 2.6.0 / 3.6.0
   */
  @SuppressWarnings("unchecked")
  public <K, V> void assertHasEntrySatisfying(AssertionInfo info, Map<K, V> actual, K key,
                                              Condition<? super V> valueCondition) {
    assertContainsKeys(info, actual, key);
    conditions.assertIsNotNull(valueCondition);
    V value = actual.get(key);
    if (!valueCondition.matches(value)) throw failures.failure(info, elementsShouldBe(actual, value, valueCondition));
  }

  /**
   * Verifies that the {@code Map} contains the value for given {@code key} that satisfy given {@code valueRequirements}.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param key he given key to check.
   * @param valueRequirements the given requirements for check value.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given {@code key}.
   * @throws AssertionError if the actual map contains the given key, but value not pass the given {@code valueRequirements}.
   */
  @SuppressWarnings("unchecked")
  public <K, V> void assertHasEntrySatisfying(AssertionInfo info, Map<K, V> actual, K key,
                                              Consumer<? super V> valueRequirements) {
    assertContainsKeys(info, actual, key);
    requireNonNull(valueRequirements, "The Consumer<V> expressing the assertions requirements must not be null");
    V value = actual.get(key);
    valueRequirements.accept(value);
  }

  /**
   * Verifies that the given {@code Map} contains an entry satisfying given {@code entryCondition}.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param entryCondition the condition for searching entry.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no entry matching given {@code entryCondition}.
   * @since 2.7.0 / 3.7.0
   */
  public <K, V> void assertHasEntrySatisfying(AssertionInfo info, Map<K, V> actual,
                                              Condition<? super Map.Entry<K, V>> entryCondition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(entryCondition);
    for (Map.Entry<K, V> entry : actual.entrySet()) {
      if (entryCondition.matches(entry)) return;
    }

    throw failures.failure(info, shouldContainEntry(actual, entryCondition));
  }

  /**
   * Verifies that the given {@code Map} contains an entry with key satisfying {@code keyCondition}
   * and value satisfying {@code valueCondition}.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param keyCondition the condition for entry key.
   * @param valueCondition the condition for entry value.
   * @throws NullPointerException if any of the given conditions is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no entry matching given {@code keyCondition} and {@code valueCondition}.
   * @since 2.7.0 / 3.7.0
   */
  public <K, V> void assertHasEntrySatisfyingConditions(AssertionInfo info, Map<K, V> actual,
                                                        Condition<? super K> keyCondition,
                                                        Condition<? super V> valueCondition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(keyCondition, "The condition to evaluate for entries key should not be null");
    conditions.assertIsNotNull(valueCondition, "The condition to evaluate for entries value should not be null");

    for (Map.Entry<K, V> entry : actual.entrySet()) {
      if (keyCondition.matches(entry.getKey()) && valueCondition.matches(entry.getValue())) return;
    }

    throw failures.failure(info, shouldContainEntry(actual, keyCondition, valueCondition));
  }

  /**
   * Verifies that the given {@code Map} contains an entry with key satisfying {@code keyCondition}.
   *
   * @param <K> key type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param keyCondition the condition for key search.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no key matching given {@code keyCondition}.
   * @since 2.7.0 / 3.7.0
   */
  public <K> void assertHasKeySatisfying(AssertionInfo info, Map<K, ?> actual, Condition<? super K> keyCondition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(keyCondition);

    for (K key : actual.keySet()) {
      if (keyCondition.matches(key)) return;
    }

    throw failures.failure(info, shouldContainKey(actual, keyCondition));
  }

  /**
   * Verifies that the given {@code Map} contains an entry with value satisfying {@code valueCondition}.
   *
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param valueCondition the condition for value search.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no value matching given {@code valueCondition}.
   * @since 2.7.0 / 3.7.0
   */
  public <V> void assertHasValueSatisfying(AssertionInfo info, Map<?, V> actual, Condition<? super V> valueCondition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(valueCondition);

    for (V value : actual.values()) {
      if (valueCondition.matches(value)) return;
    }

    throw failures.failure(info, shouldContainValue(actual, valueCondition));
  }

  /**
   * Asserts that the given {@code Map} does not contain the given entries.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param entries the entries that are expected to be in the given {@code Map}.
   * @throws NullPointerException if the array of entries is {@code null}.
   * @throws IllegalArgumentException if the array of entries is empty.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} contains any of the given entries.
   */
  public <K, V> void assertDoesNotContain(AssertionInfo info, Map<K, V> actual,
                                          Map.Entry<? extends K, ? extends V>[] entries) {
    failIfNullOrEmpty(entries);
    assertNotNull(info, actual);
    Set<Map.Entry<? extends K, ? extends V>> found = new LinkedHashSet<>();
    for (Map.Entry<? extends K, ? extends V> entry : entries) {
      if (containsEntry(actual, entry)) {
        found.add(entry);
      }
    }
    if (found.isEmpty()) return;
    throw failures.failure(info, shouldNotContain(actual, entries, found));
  }

  /**
   * Verifies that the actual map contain the given key.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param keys the given keys
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given key.
   */
  public <K, V> void assertContainsKeys(AssertionInfo info, Map<K, V> actual,
                                        @SuppressWarnings("unchecked") K... keys) {
    assertNotNull(info, actual);
    Set<K> notFound = new LinkedHashSet<>();
    for (K key : keys) {
      if (!actual.containsKey(key)) {
        notFound.add(key);
      }
    }
    if (notFound.isEmpty()) return;
    throw failures.failure(info, shouldContainKeys(actual, notFound));
  }

  /**
   * Verifies that the actual map not contains the given key.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param key the given key
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given key.
   */
  public <K, V> void assertDoesNotContainKey(AssertionInfo info, Map<K, V> actual, K key) {
    assertNotNull(info, actual);
    if (actual.containsKey(key)) throw failures.failure(info, shouldNotContainKey(actual, key));
  }

  /**
   * Verifies that the actual map not contains all the given keys.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param keys the given keys
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains all the given keys.
   */
  public <K, V> void assertDoesNotContainKeys(AssertionInfo info, Map<K, V> actual,
                                              @SuppressWarnings("unchecked") K... keys) {
    assertNotNull(info, actual);
    Set<K> found = new LinkedHashSet<>();
    for (K key : keys) {
      if (key != null && actual.containsKey(key)) {
        found.add(key);
      }
    }
    if (!found.isEmpty()) throw failures.failure(info, shouldNotContainKeys(actual, found));
  }

  /**
   * Verifies that the actual map contains only the given keys and nothing else, in any order.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param keys the keys that are expected to be in the given {@code Map}.
   * @throws NullPointerException if the array of keys is {@code null}.
   * @throws IllegalArgumentException if the array of keys is empty.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} does not contain the given keys or if the given {@code Map}
   *           contains keys that are not in the given array.
   */
  public <K, V> void assertContainsOnlyKeys(AssertionInfo info, Map<K, V> actual,
                                            @SuppressWarnings("unchecked") K... keys) {
    assertContainsOnlyKeys(info, actual, "array of keys", keys);
  }

  /**
   * Verifies that the actual map contains only the given keys and nothing else, in any order.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param keys the keys that are expected to be in the given {@code Map}.
   * @throws NullPointerException if the array of keys is {@code null}.
   * @throws IllegalArgumentException if the array of keys is empty.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} does not contain the given keys or if the given {@code Map}
   *           contains keys that are not in the given array.
   */
  public <K, V> void assertContainsOnlyKeys(AssertionInfo info, Map<K, V> actual,
                                            Iterable<? extends K> keys) {
    final K[] keysAsArray = toArray(keys);
    assertContainsOnlyKeys(info, actual, "keys iterable", keysAsArray);
  }

  private <K, V> void assertContainsOnlyKeys(AssertionInfo info, Map<K, V> actual, String placeholderForErrorMessages, K[] keys) {
    assertNotNull(info, actual);
    failIfNull(keys, keysToLookForIsNull(placeholderForErrorMessages));
    if (actual.isEmpty() && keys.length == 0) {
      return;
    }
    failIfEmpty(keys, keysToLookForIsEmpty(placeholderForErrorMessages));

    Set<K> notFound = new LinkedHashSet<>();
    Set<K> notExpected = new LinkedHashSet<>();

    compareActualMapAndExpectedKeys(actual, keys, notExpected, notFound);

    if (!notFound.isEmpty() || !notExpected.isEmpty())
      throw failures.failure(info, shouldContainOnlyKeys(actual, keys, notFound, notExpected));
  }

  /**
   * Verifies that the actual map contain the given value.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param value the given value
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given value.
   */
  public <K, V> void assertContainsValue(AssertionInfo info, Map<K, V> actual, V value) {
    assertNotNull(info, actual);
    if (!actual.containsValue(value)) throw failures.failure(info, shouldContainValue(actual, value));
  }

  /**
   * Verifies that the actual map contain the given values.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param values the given values
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given values.
   * @throws NullPointerException if values vararg is {@code null}.
   */
  public <K, V> void assertContainsValues(AssertionInfo info, Map<K, V> actual,
                                          @SuppressWarnings("unchecked") V... values) {
    assertNotNull(info, actual);
    requireNonNull(values, "The array of values to look for should not be null");
    if (actual.isEmpty() && values.length == 0) return;
    //
    Set<V> valuesNotFound = new LinkedHashSet<>();
    for (V valueToLookFor : values) {
      if (!actual.containsValue(valueToLookFor)) valuesNotFound.add(valueToLookFor);
    }
    if (!valuesNotFound.isEmpty()) throw failures.failure(info, shouldContainValues(actual, valuesNotFound));
  }

  /**
   * Verifies that the actual map not contains the given value.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param value the given value
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given value.
   */
  public <K, V> void assertDoesNotContainValue(AssertionInfo info, Map<K, V> actual, V value) {
    assertNotNull(info, actual);
    if (actual.containsValue(value)) throw failures.failure(info, shouldNotContainValue(actual, value));
  }

  /**
   * Verifies that the actual map contains only the given entries and nothing else, in any order.
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param entries the entries that should be in the actual map.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws NullPointerException if the given entries array is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries, i.e. the actual map contains some or
   *           none of the given entries, or the actual map contains more entries than the given ones, or if entries is
   *           empty.
   */
  public <K, V> void assertContainsOnly(AssertionInfo info, Map<K, V> actual,
                                        @SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
    doCommonContainsCheck(info, actual, entries);
    if (actual.isEmpty() && entries.length == 0) return;
    failIfEntriesIsEmptyEmptySinceActualIsNotEmpty(info, actual, entries);

    Set<Map.Entry<? extends K, ? extends V>> notFound = new LinkedHashSet<>();
    Set<Map.Entry<? extends K, ? extends V>> notExpected = new LinkedHashSet<>();
    compareActualMapAndExpectedEntries(actual, entries, notExpected, notFound);
    if (!notFound.isEmpty() || !notExpected.isEmpty())
      throw failures.failure(info, shouldContainOnly(actual, entries, notFound, notExpected));
  }

  /**
   * Verifies that the actual map contains only the given entries and nothing else, <b>in order</b>.<br>
   * This assertion should only be used with map that have a consistent iteration order (i.e. don't use it with
   * {@link java.util.HashMap}).
   *
   * @param <K> key type
   * @param <V> value type
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param entries the given entries.
   * @throws NullPointerException if the given entries array is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries with same order, i.e. the actual map
   *           contains some or none of the given entries, or the actual map contains more entries than the given ones
   *           or entries are the same but the order is not.
   */
  public <K, V> void assertContainsExactly(AssertionInfo info, Map<K, V> actual,
                                           @SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
    doCommonContainsCheck(info, actual, entries);
    if (actual.isEmpty() && entries.length == 0) return;
    failIfEntriesIsEmptyEmptySinceActualIsNotEmpty(info, actual, entries);
    assertHasSameSizeAs(info, actual, entries);

    Set<Map.Entry<? extends K, ? extends V>> notFound = new LinkedHashSet<>();
    Set<Map.Entry<? extends K, ? extends V>> notExpected = new LinkedHashSet<>();

    compareActualMapAndExpectedEntries(actual, entries, notExpected, notFound);

    if (notExpected.isEmpty() && notFound.isEmpty()) {
      // check entries order
      int index = 0;
      for (K keyFromActual : actual.keySet()) {
        if (!areEqual(keyFromActual, entries[index].getKey())) {
          Map.Entry<K, V> actualEntry = entry(keyFromActual, actual.get(keyFromActual));
          throw failures.failure(info, elementsDifferAtIndex(actualEntry, entries[index], index));
        }
        index++;
      }
      // all entries are in the same order.
      return;
    }

    throw failures.failure(info, shouldContainExactly(actual, asList(entries), notFound, notExpected));
  }

  private <K, V> void compareActualMapAndExpectedKeys(Map<K, V> actual, K[] keys, Set<K> notExpected,
                                                      Set<K> notFound) {

    Map<K, V> actualEntries = new LinkedHashMap<>(actual);
    for (K key : keys) {
      if (actualEntries.containsKey(key)) {
        // this is an expected key
        actualEntries.remove(key);
      } else {
        // this is a not found key
        notFound.add(key);
      }
    }
    // All remaining keys from actual copy are not expected entries.
    notExpected.addAll(actualEntries.keySet());
  }

  private <K, V> void compareActualMapAndExpectedEntries(Map<K, V> actual,
                                                         Map.Entry<? extends K, ? extends V>[] entries,
                                                         Set<Map.Entry<? extends K, ? extends V>> notExpected,
                                                         Set<Map.Entry<? extends K, ? extends V>> notFound) {
    Map<K, V> expectedEntries = entriesToMap(entries);
    Map<K, V> actualEntries = new LinkedHashMap<>(actual);
    for (Map.Entry<K, V> entry : expectedEntries.entrySet()) {
      if (containsEntry(actualEntries, entry(entry.getKey(), entry.getValue()))) {
        // this is an expected entry
        actualEntries.remove(entry.getKey());
      } else {
        // this is a not found entry
        notFound.add(entry(entry.getKey(), entry.getValue()));
      }
    }
    // All remaining entries from actual copy are not expected entries.
    for (Map.Entry<K, V> entry : actualEntries.entrySet()) {
      notExpected.add(entry(entry.getKey(), entry.getValue()));
    }
  }

  private <K, V> void doCommonContainsCheck(AssertionInfo info, Map<K, V> actual,
                                            Map.Entry<? extends K, ? extends V>[] entries) {
    assertNotNull(info, actual);
    failIfNull(entries);
  }

  private <K, V> void failIfAnyEntryNotFoundInActualMap(AssertionInfo info, Map<K, V> actual,
                                                        Map.Entry<? extends K, ? extends V>[] entries) {
    Set<Map.Entry<? extends K, ? extends V>> notFound = new LinkedHashSet<>();
    for (Map.Entry<? extends K, ? extends V> entry : entries) {
      if (!containsEntry(actual, entry)) notFound.add(entry);
    }
    if (!notFound.isEmpty()) throw failures.failure(info, shouldContain(actual, entries, notFound));
  }

  private static <K, V> Map<K, V> entriesToMap(Map.Entry<? extends K, ? extends V>[] entries) {
    Map<K, V> expectedEntries = new LinkedHashMap<>();
    for (Map.Entry<? extends K, ? extends V> entry : entries) {
      expectedEntries.put(entry.getKey(), entry.getValue());
    }
    return expectedEntries;
  }

  private static <K> void failIfEmpty(K[] keys, String errorMessage) {
    checkArgument(keys.length > 0, errorMessage);
  }

  private static <K, V> void failIfEmpty(Map.Entry<? extends K, ? extends V>[] entries) {
    checkArgument(entries.length > 0, "The array of entries to look for should not be empty");
  }

  private static <K, V> void failIfNullOrEmpty(Map.Entry<? extends K, ? extends V>[] entries) {
    failIfNull(entries);
    failIfEmpty(entries);
  }

  private static <K> void failIfNull(K[] keys, String errorMessage) {
    requireNonNull(keys, errorMessage);
  }

  private static <K, V> void failIfNull(Map.Entry<? extends K, ? extends V>[] entries) {
    requireNonNull(entries, ErrorMessages.entriesToLookForIsNull());
  }

  private static <K, V> void failIfNull(Map<? extends K, ? extends V> map) {
    requireNonNull(map, ErrorMessages.mapOfEntriesToLookForIsNull());
  }

  private <K, V> boolean containsEntry(Map<K, V> actual, Map.Entry<? extends K, ? extends V> entry) {
    requireNonNull(entry, ErrorMessages.entryToLookForIsNull());
    return actual.containsKey(entry.getKey()) && areEqual(actual.get(entry.getKey()), entry.getValue());
  }

  private void assertNotNull(AssertionInfo info, Map<?, ?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  // this should be only called when actual is not empty
  private <K, V> void failIfEntriesIsEmptyEmptySinceActualIsNotEmpty(AssertionInfo info, Map<K, V> actual,
                                                                     Map.Entry<? extends K, ? extends V>[] entries) {
    if (entries.length == 0) throw failures.failure(info, shouldBeEmpty(actual));
  }

}
