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

package io.github.whilein.jexpr.operator.type;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.defined.OperandDouble;
import io.github.whilein.jexpr.operand.defined.OperandFloat;
import io.github.whilein.jexpr.operand.defined.OperandInteger;
import io.github.whilein.jexpr.operand.defined.OperandLong;
import io.github.whilein.jexpr.operator.AbstractUnaryOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperatorUnaryMinus extends AbstractUnaryOperator {

    public OperatorUnaryMinus() {
        super("-", OperatorPrecedenceConsts.UNARY);
    }

    @Override
    public String toString() {
        return "UNARY_MINUS";
    }

    @Override
    public @NotNull Operand apply(final int value) {
        return OperandInteger.valueOf(-value);
    }

    @Override
    public @NotNull Operand apply(final long value) {
        return OperandLong.valueOf(-value);
    }

    @Override
    public @NotNull Operand apply(final float value) {
        return OperandFloat.valueOf(-value);
    }

    @Override
    public @NotNull Operand apply(final double value) {
        return OperandDouble.valueOf(-value);
    }

}
