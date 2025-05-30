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
package org.assertj.tests.core.internal.paths;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

/**
 * @author Valeriy Vyrva
 */
class Paths_assertIsNotEmptyDirectory_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsNotEmptyDirectory(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsNotEmptyDirectory(INFO, actual));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_directory() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("file"));
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsNotEmptyDirectory(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_pass_if_actual_is_not_empty() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    createFile(actual.resolve("file"));
    // WHEN/THEN
    underTest.assertIsNotEmptyDirectory(INFO, actual);
  }

  @Test
  void should_fail_if_actual_is_empty() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsNotEmptyDirectory(INFO, actual));
    // THEN
    then(error).hasMessage(shouldNotBeEmpty(actual).create());
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    IOException cause = new IOException("boom!");
    willThrow(cause).given(nioFilesWrapper).newDirectoryStream(any(), any());
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertIsNotEmptyDirectory(INFO, actual));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(cause);
  }

}
