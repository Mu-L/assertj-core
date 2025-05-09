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
package org.assertj.core.error.future;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldBeCompleted extends BasicErrorMessageFactory {

  private static final String SHOULD_BE_COMPLETED = "%nExpecting%n  <%s>%nto be completed.%n" + Warning.WARNING;

  public static ErrorMessageFactory shouldBeCompleted(CompletableFuture<?> actual) {
    return new ShouldBeCompleted(actual);
  }

  private ShouldBeCompleted(CompletableFuture<?> actual) {
    super(SHOULD_BE_COMPLETED, actual);
  }
}
