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
package org.assertj.core.api.fail;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Fail#fail(String)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
class Fail_fail_withMessage_Test {

  @Test
  void shouldIncludeMessageWhenFailing() {
    String message = "Failed :(";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Fail.fail(message))
                                                   .withMessage(message);
  }
}
