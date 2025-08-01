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

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainSequenceOfCharSequence.shouldNotContainSequence;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Arrays.array;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.Representation;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ShouldNotContainSequenceOfCharSequence#create(Description, Representation)}.
 */
class ShouldNotContainSequenceOfCharSequence_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    var factory = shouldNotContainSequence("Yoda", array("Yo", "da"), 0);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to not contain sequence:%n" +
                                   "  [\"Yo\", \"da\"]%n" +
                                   "but was found at index 0%n"));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    var factory = shouldNotContainSequence("Yoda", array("Yo", "da"), 0,
                                           new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to not contain sequence:%n" +
                                   "  [\"Yo\", \"da\"]%n" +
                                   "but was found at index 0%n" +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }
}
