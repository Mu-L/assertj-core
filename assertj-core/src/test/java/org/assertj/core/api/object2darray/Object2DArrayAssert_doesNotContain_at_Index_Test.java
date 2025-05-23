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
package org.assertj.core.api.object2darray;

import static org.assertj.core.testkit.TestData.someIndex;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Object2DArrayAssert;
import org.assertj.core.api.Object2DArrayAssertBaseTest;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for <code>{@link Object2DArrayAssert#doesNotContain(Object[], Index)}</code>.
 * 
 * @author Maciej Wajcht
 */
@DisplayName("Object2DArrayAssert doesNotContain")
class Object2DArrayAssert_doesNotContain_at_Index_Test extends Object2DArrayAssertBaseTest {
  private final Index index = someIndex();

  @Override
  protected Object2DArrayAssert<Object> invoke_api_method() {
    return assertions.doesNotContain(new String[] { "8", "9" }, index);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoesNotContain(getInfo(assertions), getActual(assertions), new String[] { "8", "9" }, index);
  }
}
