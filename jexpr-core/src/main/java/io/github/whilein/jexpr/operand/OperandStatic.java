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

package io.github.whilein.jexpr.operand;

import io.github.whilein.jexpr.DynamicResolver;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public interface OperandStatic extends Operand {

    @Override
    default boolean isDynamic() {
        return false;
    }

    @Override
    default @NotNull Operand solve(final @NotNull DynamicResolver resolver) {
        return this;
    }

}
