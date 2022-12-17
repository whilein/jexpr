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

package io.github.whilein.jexpr.token.operand;

import io.github.whilein.jexpr.api.token.operand.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
final class OperandReferenceImpl extends OperandDelegate<@NotNull String> implements OperandReference {

    public OperandReferenceImpl(final String value) {
        super(value);
    }

    @Override
    public <T> @NotNull T apply(@NotNull OperandMapper<T> mapper) {
        return mapper.mapReference(delegatedValue);
    }

    @Override
    public void accept(@NotNull OperandVisitor visitor) {
        visitor.visitReference(delegatedValue);
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
