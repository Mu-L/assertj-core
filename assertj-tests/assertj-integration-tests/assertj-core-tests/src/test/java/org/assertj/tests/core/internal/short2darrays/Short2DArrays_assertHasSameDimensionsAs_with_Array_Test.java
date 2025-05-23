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
package org.assertj.tests.core.internal.short2darrays;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * @author Maciej Wajcht
 */
class Short2DArrays_assertHasSameDimensionsAs_with_Array_Test extends Short2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // GIVEN
    short[][] other = new short[][] { { 0, 4 }, { 8, 12 } };
    // WHEN
    short2DArrays.assertHasSameDimensionsAs(info, actual, other);
    // THEN
    verify(arrays2d).assertHasSameDimensionsAs(info, actual, other);
  }
}
