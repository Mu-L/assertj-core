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
package org.assertj.core.api.zoneddatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;

import java.time.format.DateTimeParseException;

import org.assertj.core.api.AbstractZonedDateTimeAssertBaseTest;
import org.assertj.core.api.ZonedDateTimeAssert;
import org.junit.jupiter.api.Test;

class ZonedDateTimeAssert_isBetween_with_String_parameters_Test
    extends AbstractZonedDateTimeAssertBaseTest {

  @Override
  protected ZonedDateTimeAssert invoke_api_method() {
    return assertions.isBetween(YESTERDAY.toString(), TOMORROW.toString());
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertIsBetween(getInfo(assertions), getActual(assertions), YESTERDAY, TOMORROW, true, true);
  }

  @Test
  void should_throw_a_DateTimeParseException_if_start_String_parameter_cant_be_converted() {
    // GIVEN
    String abc = "abc";
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.isBetween(abc, TOMORROW.toString()));
    // THEN
    assertThat(thrown).isInstanceOf(DateTimeParseException.class);
  }

  @Test
  void should_throw_a_DateTimeParseException_if_end_String_parameter_cant_be_converted() {
    // GIVEN
    String abc = "abc";
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.isBetween(YESTERDAY.toString(), abc));
    // THEN
    assertThat(thrown).isInstanceOf(DateTimeParseException.class);
  }

}
