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
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Collections;

import org.assertj.core.description.TextDescription;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldContainExactlyInAnyOrder#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class ShouldContainExactlyInAnyOrder_create_Test {

  private static final ComparatorBasedComparisonStrategy CASE_INSENSITIVE_COMPARISON_STRATEGY = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactlyInAnyOrder(list("Yoda", "Han"),
                                                                 list("Luke", "Yoda"),
                                                                 newLinkedHashSet("Luke"),
                                                                 newLinkedHashSet("Han"),
                                                                 CASE_INSENSITIVE_COMPARISON_STRATEGY);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual:%n"
                                   + "  [\"Yoda\", \"Han\"]%n"
                                   + "to contain exactly in any order:%n"
                                   + "  [\"Luke\", \"Yoda\"]%n"
                                   + "elements not found:%n"
                                   + "  [\"Luke\"]%n"
                                   + "and elements not expected:%n"
                                   + "  [\"Han\"]%n"
                                   + "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_not_display_unexpected_elements_when_there_are_none_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactlyInAnyOrder(list("Yoda"),
                                                                 list("Luke", "Yoda"),
                                                                 newLinkedHashSet("Luke"),
                                                                 Collections.emptySet(),
                                                                 CASE_INSENSITIVE_COMPARISON_STRATEGY);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual:%n"
                                   + "  [\"Yoda\"]%n"
                                   + "to contain exactly in any order:%n"
                                   + "  [\"Luke\", \"Yoda\"]%n"
                                   + "but could not find the following elements:%n"
                                   + "  [\"Luke\"]%n"
                                   + "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_not_display_elements_not_found_when_there_are_none_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactlyInAnyOrder(list("Yoda", "Leia"),
                                                                 list("Yoda"),
                                                                 Collections.emptySet(),
                                                                 newLinkedHashSet("Leia"),
                                                                 CASE_INSENSITIVE_COMPARISON_STRATEGY);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual:%n"
                                   + "  [\"Yoda\", \"Leia\"]%n"
                                   + "to contain exactly in any order:%n"
                                   + "  [\"Yoda\"]%n"
                                   + "but the following elements were unexpected:%n"
                                   + "  [\"Leia\"]%n"
                                   + "when comparing values using CaseInsensitiveStringComparator"));
  }
}
