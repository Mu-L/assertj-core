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
package org.assertj.core.internal.files;

import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.testkit.ClasspathResources.resourceFile;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.assertj.core.internal.Diff;
import org.assertj.core.internal.FilesBaseTest;
import org.assertj.core.util.Files;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Files#assertSameContentAs(org.assertj.core.api.AssertionInfo, java.io.File, java.nio.charset.Charset, java.io.File,  java.nio.charset.Charset)}</code>.
 *
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class Files_assertSameContentAs_Test extends FilesBaseTest {

  private static File actual;
  private static File expected;

  @BeforeAll
  static void setUpOnce() {
    actual = resourceFile("actual_file.txt");
    expected = resourceFile("expected_file.txt");
  }

  @Test
  void should_throw_error_if_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> underTest.assertSameContentAs(INFO, actual, defaultCharset(),
                                                                                    null, defaultCharset()))
                                    .withMessage("The file to compare to should not be null");
  }

  @Test
  void should_throw_error_if_expected_is_not_file() {
    assertThatIllegalArgumentException().isThrownBy(() -> {
      File notAFile = new File("xyz");
      underTest.assertSameContentAs(INFO, actual, defaultCharset(), notAFile, defaultCharset());
    }).withMessage("Expected file:<'xyz'> should be an existing file");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> underTest.assertSameContentAs(INFO, actual, defaultCharset(),
                                                                                             expected, defaultCharset()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_file() {
    // GIVEN

    File notAFile = new File("xyz");
    // WHEN
    expectAssertionError(() -> underTest.assertSameContentAs(INFO, notAFile, defaultCharset(), expected, defaultCharset()));
    // THEN
    verify(failures).failure(INFO, shouldBeFile(notAFile));
  }

  @Test
  void should_pass_if_files_have_equal_content() {
    unMockedFiles.assertSameContentAs(INFO,
                                      actual, defaultCharset(),
                                      actual, defaultCharset());
  }

  @Test
  void should_throw_error_wrapping_caught_IOException() throws IOException {
    IOException cause = new IOException();
    when(diff.diff(actual, defaultCharset(), expected, defaultCharset())).thenThrow(cause);

    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> underTest.assertSameContentAs(INFO, actual,
                                                                                                         defaultCharset(),
                                                                                                         expected,
                                                                                                         defaultCharset()))
                                                         .withCause(cause);
  }

  @Test
  void should_fail_if_files_do_not_have_equal_content() throws IOException {
    // GIVEN
    Diff diff = new Diff();
    List<Delta<String>> diffs = diff.diff(actual, defaultCharset(), expected, defaultCharset());

    // WHEN
    expectAssertionError(() -> unMockedFiles.assertSameContentAs(INFO, actual, defaultCharset(), expected, defaultCharset()));
    // THEN
    verify(failures).failure(INFO, shouldHaveSameContent(actual, expected, diffs));
  }

  @Test
  void should_throw_an_error_if_files_cant_be_compared_with_the_given_charsets_even_if_binary_identical() {
    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> unMockedFiles.assertSameContentAs(INFO,
                                                                                                             createFileWithNonUTF8Character(),
                                                                                                             StandardCharsets.UTF_8,
                                                                                                             createFileWithNonUTF8Character(),
                                                                                                             StandardCharsets.UTF_8))
                                                         .withMessageStartingWith("Unable to compare contents of files");
  }

  @Test
  void should_fail_if_files_are_not_binary_identical() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> unMockedFiles.assertSameContentAs(INFO,
                                                                                                       createFileWithNonUTF8Character(),
                                                                                                       StandardCharsets.UTF_8,
                                                                                                       expected,
                                                                                                       StandardCharsets.UTF_8))
                                                   .withMessageEndingWith(format("does not have expected binary content at offset 0, expecting:%n"
                                                                                 +
                                                                                 "  \"EOF\"%n" +
                                                                                 "but was:%n" +
                                                                                 "  \"0x0\""));
  }

  private File createFileWithNonUTF8Character() throws IOException {
    byte[] data = new BigInteger("FE", 16).toByteArray();
    File file = Files.newTemporaryFile();
    file.deleteOnExit();
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(data, 0, data.length);
      return file;
    }
  }
}
