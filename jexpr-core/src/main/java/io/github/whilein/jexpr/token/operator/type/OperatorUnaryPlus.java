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
import io.github.whilein.jexpr.token.operand.OperandDouble;
import io.github.whilein.jexpr.token.operand.OperandFloat;
import io.github.whilein.jexpr.token.operand.OperandInteger;
import io.github.whilein.jexpr.token.operand.OperandLong;
import io.github.whilein.jexpr.token.operator.AbstractUnaryOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperatorUnaryPlus extends AbstractUnaryOperator {

    public OperatorUnaryPlus() {
        super("+", OperatorPrecedenceConsts.ADDITIVE);
    }

    @Override
    public String toString() {
        return "UNARY_PLUS";
    }

    @Override
    public @NotNull Operand apply(final int value) {
        return OperandInteger.valueOf(+value);
    }

    @Override
    public @NotNull Operand apply(final long value) {
        return OperandLong.valueOf(+value);
    }

    @Override
    public @NotNull Operand apply(final float value) {
        return OperandFloat.valueOf(+value);
    }

    @Override
    public @NotNull Operand apply(final double value) {
        return OperandDouble.valueOf(+value);
    }

}
