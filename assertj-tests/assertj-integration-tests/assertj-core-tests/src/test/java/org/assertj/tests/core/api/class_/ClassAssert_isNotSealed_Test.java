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
package org.assertj.tests.core.api.class_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeSealed.shouldNotBeSealed;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ClassAssert_isNotSealed_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isNotSealed());
    // THEN
    then(error).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_sealed() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(SealedClass.class).isNotSealed());
    // THEN
    then(assertionError).hasMessage(shouldNotBeSealed(SealedClass.class).create());
  }

  @ParameterizedTest
  @MethodSource("nonSealed")
  void should_pass_if_actual_is_not_sealed(Class<?> actual) {
    // WHEN/THEN
    assertThat(actual).isNotSealed();
  }

  private static Stream<Class<?>> nonSealed() {
    return Stream.of(NonSealedClass.class,
                     Object.class,
                     List.class,
                     Object[].class,
                     Boolean.TYPE,
                     Byte.TYPE,
                     Character.TYPE,
                     Double.TYPE,
                     Float.TYPE,
                     Integer.TYPE,
                     Long.TYPE,
                     Short.TYPE,
                     Void.TYPE);
  }

  private static sealed class SealedClass permits NonSealedClass {
  }

  private non-sealed static class NonSealedClass extends SealedClass {
  }

}
