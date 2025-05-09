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
package org.assertj.core.extractor;

import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.junit.jupiter.api.Test;

class ToStringExtractorTest {

  private final ToStringExtractor underTest = new ToStringExtractor();

  @Test
  void should_extract_toString() {
    // GIVEN
    Employee someEmployee = new Employee(1, new Name("John Doe"), 28);
    // WHEN
    String result = underTest.apply(someEmployee);
    // THEN
    then(result).isEqualTo("Employee[id=1, name=Name[first='John Doe', last='null'], age=28]");
  }

}
