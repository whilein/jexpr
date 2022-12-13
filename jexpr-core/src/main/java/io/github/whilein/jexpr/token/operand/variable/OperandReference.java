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

package io.github.whilein.jexpr.token.operand.variable;

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandVariable;
import io.github.whilein.jexpr.api.token.operand.OperandVariableResolver;
import io.github.whilein.jexpr.token.operand.OperandDelegate;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperandReference extends OperandDelegate<@NotNull String> implements OperandVariable {

    private OperandReference(final String value) {
        super(value);
    }

    public static @NotNull Operand valueOf(final @NotNull String reference) {
        return new OperandReference(reference);
    }

    @Override
    public void print(final @NotNull StringBuilder out) {
        out.append(delegatedValue);
    }

    @Override
    public @NotNull Operand solve(final @NotNull OperandVariableResolver resolver) {
        return resolver.resolve(delegatedValue);
    }

}
