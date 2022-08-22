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
import io.github.whilein.jexpr.operator.AbstractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperatorMultiply extends AbstractOperator {

    public OperatorMultiply() {
        super("*", 12, TWO_OPERAND);
    }

    @Override
    public String toString() {
        return "MULTIPLY";
    }

    @Override
    public @NotNull Operand apply(final int left, final int right) {
        return OperandInteger.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final int left, final long right) {
        return OperandLong.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final int left, final float right) {
        return OperandFloat.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final int left, final double right) {
        return OperandDouble.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final long left, final int right) {
        return OperandLong.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final long left, final long right) {
        return OperandLong.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final long left, final float right) {
        return OperandFloat.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final long left, final double right) {
        return OperandDouble.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final float left, final int right) {
        return OperandFloat.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final float left, final long right) {
        return OperandFloat.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final float left, final float right) {
        return OperandFloat.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final float left, final double right) {
        return OperandDouble.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final double left, final int right) {
        return OperandDouble.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final double left, final long right) {
        return OperandDouble.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final double left, final float right) {
        return OperandDouble.valueOf(left * right);
    }

    @Override
    public @NotNull Operand apply(final double left, final double right) {
        return OperandDouble.valueOf(left * right);
    }

}
