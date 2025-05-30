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
package org.assertj.tests.core.internal.throwables;

import static org.assertj.core.error.ShouldHaveNoSuppressedExceptions.shouldHaveNoSuppressedExceptions;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class Throwables_assertHasNoSuppressedExceptions_Test extends ThrowablesBaseTest {

  @Test
  void should_pass_if_throwable_has_no_suppressed_exceptions() {
    throwables.assertHasNoSuppressedExceptions(INFO, new Throwable());
  }

  @Test
  void should_fail_if_throwable_has_suppressed_exceptions() {
    // GIVEN
    Throwable actual = new Throwable();
    actual.addSuppressed(new IllegalArgumentException("Suppressed Message"));
    // WHEN
    expectAssertionError(() -> throwables.assertHasNoSuppressedExceptions(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldHaveNoSuppressedExceptions(actual));
  }
}
