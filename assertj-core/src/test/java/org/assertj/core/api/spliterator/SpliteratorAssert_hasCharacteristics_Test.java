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
package org.assertj.core.api.spliterator;

import static java.util.Spliterator.DISTINCT;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.SpliteratorAssert;
import org.assertj.core.api.SpliteratorAssertBaseTest;

class SpliteratorAssert_hasCharacteristics_Test extends SpliteratorAssertBaseTest {

  @Override
  protected SpliteratorAssert<String> invoke_api_method() {
    return assertions.hasCharacteristics(DISTINCT);
  }

  @Override
  protected void verify_internal_effects() {
    verify(spliterators).assertHasCharacteristics(getInfo(assertions), getActual(assertions), DISTINCT);
  }

}
