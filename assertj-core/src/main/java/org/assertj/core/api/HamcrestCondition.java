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
package org.assertj.core.api;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * Allows to use a Hamcrest matcher as a condition.
 *
 * Example:
 * <pre><code class='java'> Condition&lt;String&gt; aStringContainingA = new HamcrestCondition&lt;&gt;(containsString(&quot;a&quot;));
 *
 * // assertions will pass
 * assertThat(&quot;abc&quot;).is(aStringContainingA);
 * assertThat(&quot;bc&quot;).isNot(aStringContainingA);
 *
 * // assertion will fail
 * assertThat(&quot;bc&quot;).is(aStringContainingA);</code></pre>
 *
 * By static-importing the {@link #matching(Matcher)} method you can do:
 * <pre><code class='java'> assertThat(&quot;abc&quot;).is(matching(containsString(&quot;a&quot;)));</code></pre>
 * @since 2.9.0 / 3.9.0
 */
public class HamcrestCondition<T> extends Condition<T> {

  private Matcher<? extends T> matcher;

  /**
   * Constructs a {@link Condition} using the matcher given as a parameter.
   *
   * @param matcher the Hamcrest matcher to use as a condition
   */
  public HamcrestCondition(Matcher<? extends T> matcher) {
    this.matcher = matcher;
    as(describeMatcher());
  }

  /**
   * Constructs a {@link Condition} using the matcher given as a parameter.
   * <p>
   * Example:
   * <pre><code class='java'> import static org.assertj.core.api.Assertions.assertThat;
   * import static org.assertj.core.api.HamcrestCondition.matching;
   * import static org.hamcrest.core.StringContains.containsString;
   *
   * assertThat(&quot;abc&quot;).is(matching(containsString(&quot;a&quot;)));</code></pre>
   *
   * @param <T> the type the condition is about
   * @param matcher the Hamcrest matcher to use as a condition
   * @return the built {@link HamcrestCondition}
   */
  public static <T> HamcrestCondition<T> matching(Matcher<? extends T> matcher) {
    return new HamcrestCondition<>(matcher);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean matches(T value) {
    return matcher.matches(value);
  }

  private String describeMatcher() {
    Description d = new StringDescription();
    matcher.describeTo(d);
    return d.toString();
  }
}
