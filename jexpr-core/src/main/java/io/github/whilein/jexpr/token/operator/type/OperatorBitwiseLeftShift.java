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
import io.github.whilein.jexpr.token.operator.AbstractBinaryOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperatorBitwiseLeftShift extends AbstractBinaryOperator {

    public OperatorBitwiseLeftShift() {
        super("<<", OperatorPrecedenceConsts.BITWISE_SHIFT);
    }

    @Override
    public String toString() {
        return "BITWISE_LEFT_SHIFT";
    }

    @Override
    public @NotNull Operand apply(final int left, final int right) {
        return Operands.constantInt(left << right);
    }

    @Override
    public @NotNull Operand apply(final int left, final long right) {
        return Operands.constantInt(left << right);
    }

    @Override
    public @NotNull Operand apply(final long left, final int right) {
        return Operands.constantLong(left << right);
    }

    @Override
    public @NotNull Operand apply(final long left, final long right) {
        return Operands.constantLong(left << right);
    }

}
