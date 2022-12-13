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
import io.github.whilein.jexpr.token.operand.constant.*;
import io.github.whilein.jexpr.token.operand.variable.OperandBinaryNode;
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
        return OperandBinaryNode.valueOf(left, right, this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final int right) {
        return OperandBinaryNode.valueOf(left, OperandInteger.valueOf(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final float right) {
        return OperandBinaryNode.valueOf(left, OperandFloat.valueOf(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final double right) {
        return OperandBinaryNode.valueOf(left, OperandDouble.valueOf(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final long right) {
        return OperandBinaryNode.valueOf(left, OperandLong.valueOf(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final boolean right) {
        return OperandBinaryNode.valueOf(left, OperandBoolean.valueOf(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final @NotNull String right) {
        return OperandBinaryNode.valueOf(left, OperandString.valueOf(right), this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable left, final @Nullable Object right) {
        return OperandBinaryNode.valueOf(left, OperandObject.valueOf(right), this);
    }

    @Override
    public @NotNull Operand apply(final int left, final @NotNull OperandVariable right) {
        return OperandBinaryNode.valueOf(OperandInteger.valueOf(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final float left, final @NotNull OperandVariable right) {
        return OperandBinaryNode.valueOf(OperandFloat.valueOf(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final double left, final @NotNull OperandVariable right) {
        return OperandBinaryNode.valueOf(OperandDouble.valueOf(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final long left, final @NotNull OperandVariable right) {
        return OperandBinaryNode.valueOf(OperandLong.valueOf(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final @NotNull OperandVariable right) {
        return OperandBinaryNode.valueOf(OperandBoolean.valueOf(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final @NotNull OperandVariable right) {
        return OperandBinaryNode.valueOf(OperandString.valueOf(left), right, this);
    }

    @Override
    public @NotNull Operand apply(final @Nullable Object left, final @NotNull OperandVariable right) {
        return OperandBinaryNode.valueOf(OperandObject.valueOf(left), right, this);
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
