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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Map;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldContainKeys#create(Description)}</code>.
 *
 * @author Nicolas François
 * @author Willima Telloue
 * @author Joel Costigliola
 */
class ShouldContainKeys_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainKeys(map, newLinkedHashSet("name"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo("[Test] %nExpecting actual:%n  {\"color\"=\"green\", \"name\"=\"Yoda\"}%nto contain key:%n  \"name\"".formatted());
  }

  @Test
  void should_create_error_message_with_multiple_keys() {
    // GIVEN
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainKeys(map, newLinkedHashSet("name", "color"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo("[Test] %nExpecting actual:%n  {\"color\"=\"green\", \"name\"=\"Yoda\"}%nto contain keys:%n  [\"name\", \"color\"]".formatted());
  }
}
