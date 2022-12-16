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

package io.github.whilein.jexpr.token.operator;

import io.github.whilein.jexpr.api.exception.OperatorException;
import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandVariable;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.token.operand.Operands;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author whilein
 */
public abstract class AbstractBinaryOperator extends AbstractOperator implements BinaryOperator {

    protected AbstractBinaryOperator(final String value, final int presence) {
        super(value, presence);
    }

    private OperatorException error(final Object left, final Object right) {
        throw new OperatorException("Operator " + this + " is not applicable to "
                + "(" + left.getClass().getSimpleName() + ") " + left
                + " & "
                + "(" + right.getClass().getSimpleName() + ") " + right);
    }

    @Override
    public boolean isUnaryExpected(final @NotNull UnaryOperator operator) {
        return true;
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final @NotNull OperandVariable right) {
        return Operands.binary(left, right, this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final int right) {
        return Operands.binary(left, Operands.constantInt(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final float right) {
        return Operands.binary(left, Operands.constantFloat(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final double right) {
        return Operands.binary(left, Operands.constantDouble(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final long right) {
        return Operands.binary(left, Operands.constantLong(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final boolean right) {
        return Operands.binary(left, Operands.constantBoolean(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final @NotNull String right) {
        return Operands.binary(left, Operands.constantString(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final @Nullable Object right) {
        return Operands.binary(left, Operands.constantObject(right), this);
    }

    @Override
    public @NotNull Operand apply(final int left, final @NotNull OperandVariable right) {
        return Operands.binary(Operands.constantInt(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final float left, final @NotNull OperandVariable right) {
        return Operands.binary(Operands.constantFloat(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final double left, final @NotNull OperandVariable right) {
        return Operands.binary(Operands.constantDouble(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final long left, final @NotNull OperandVariable right) {
        return Operands.binary(Operands.constantLong(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final @NotNull OperandVariable right) {
        return Operands.binary(Operands.constantBoolean(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final @NotNull OperandVariable right) {
        return Operands.binary(Operands.constantString(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final @Nullable Object left, final @NotNull OperandVariable right) {
        return Operands.binary(Operands.constantObject(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final int left, final int right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final int left, final long right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final int left, final float right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final int left, final double right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final int left, final @NotNull String right) {
        throw error(left, right);
    }


    @Override
    public @NotNull Operand apply(final long left, final int right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final long left, final long right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final long left, final float right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final long left, final double right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final long left, final @NotNull String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final float left, final int right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final float left, final long right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final float left, final float right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final float left, final double right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final float left, final @NotNull String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final double left, final int right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final double left, final long right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final double left, final float right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final double left, final double right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final double left, final @NotNull String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final int right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final long right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final float right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final double right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final @NotNull String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final int left, final boolean right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final long left, final boolean right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final float left, final boolean right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final double left, final boolean right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final boolean right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final int right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final long right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final float right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final double right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final @NotNull String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final boolean right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final int left, final Object right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final long left, final Object right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final float left, final Object right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final double left, final Object right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final Object right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final Object left, final int right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final Object left, final long right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final Object left, final float right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final Object left, final double right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final Object left, final boolean right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final Object right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final Object left, final @NotNull String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final Object left, final Object right) {
        throw error(left, right);
    }


}
