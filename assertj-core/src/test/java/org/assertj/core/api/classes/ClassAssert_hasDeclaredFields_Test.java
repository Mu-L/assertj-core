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
package org.assertj.core.api.classes;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.ClassAssert#hasDeclaredFields(String...)}</code>.
 * 
 * @author William Delanoue
 */
class ClassAssert_hasDeclaredFields_Test extends ClassAssertBaseTest {

  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.hasDeclaredFields("field");
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertHasDeclaredFields(getInfo(assertions), getActual(assertions), "field");
  }

}
