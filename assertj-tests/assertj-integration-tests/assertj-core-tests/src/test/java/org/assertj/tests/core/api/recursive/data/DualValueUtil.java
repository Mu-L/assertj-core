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
package org.assertj.tests.core.api.recursive.data;

import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.recursive.comparison.DualValue;

public class DualValueUtil {

  public static DualValue dualValueWithPath(String... pathElements) {
    return new DualValue(list(pathElements), "foo", "bar");
  }

  public static List<String> randomPath() {
    return list(RandomStringUtils.random(RandomUtils.nextInt(1, 10), true, false));
  }

}
