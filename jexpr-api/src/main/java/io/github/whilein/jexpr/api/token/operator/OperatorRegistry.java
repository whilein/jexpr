/*
 *    Copyright 2022 Whilein
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.whilein.jexpr.api.token.operator;

import io.github.whilein.jexpr.api.registry.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public interface OperatorRegistry<T extends Operator> extends Registry<T, String> {

    /**
     * Check if any operator has value starts with {@code ch}
     *
     * @param ch starting character of operator
     * @return {@code true} if any operator exist
     */
    boolean hasMatcher(int ch);

    /**
     * Get matcher for operator which value starts with {@code ch}
     *
     * @param ch starting character of operator
     * @return matcher
     */
    @NotNull OperatorMatcher<T> matchOperator(int ch);

}
