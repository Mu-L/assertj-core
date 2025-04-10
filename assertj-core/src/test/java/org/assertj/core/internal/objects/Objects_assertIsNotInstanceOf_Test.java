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
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.core.error.ShouldNotBeInstance.shouldNotBeInstance;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.testkit.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Objects#assertIsNotInstanceOf(AssertionInfo, Object, Class)}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
class Objects_assertIsNotInstanceOf_Test extends ObjectsBaseTest {

  private static Person actual;

  @BeforeAll
  static void setUpOnce() {
    actual = new Person("Yoda");
  }

  @Test
  void should_pass_if_actual_is_not_instance_of_type() {
    objects.assertIsNotInstanceOf(someInfo(), actual, String.class);
  }

  @Test
  void should_throw_error_if_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> objects.assertIsNotInstanceOf(someInfo(), actual, null))
                                    .withMessage("The given type should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> objects.assertIsNotInstanceOf(someInfo(), null,
                                                                                                   Object.class))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_instance_of_type() {
    AssertionInfo info = someInfo();
    try {
      objects.assertIsNotInstanceOf(info, actual, Person.class);
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError err) {}
    verify(failures).failure(info, shouldNotBeInstance(actual, Person.class));
  }
}
