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

package io.github.whilein.jexpr.token.operator.type;

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.token.operand.Operands;
import io.github.whilein.jexpr.token.operator.AbstractBinaryLazyOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperatorOr extends AbstractBinaryLazyOperator {

    public OperatorOr() {
        super("||", OperatorPrecedenceConsts.OR);
    }

    @Override
    public boolean isPredictable(final boolean value) {
        return value;
    }

    @Override
    public @NotNull Operand getPredictedResult(final boolean value) {
        return !value ? super.getPredictedResult(false) : Operands.constantTrue();
    }

    @Override
    public String toString() {
        return "OR";
    }

    @Override
    public @NotNull Operand apply(final boolean left, final boolean right) {
        return Operands.constantBoolean(left || right);
    }
}
