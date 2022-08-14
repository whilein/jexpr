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

package io.github.whilein.jexpr.operator;

import io.github.whilein.jexpr.operand.Operand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOperator implements Operator {

    protected static final int ONE_OPERAND = 1;
    protected static final int TWO_OPERAND = 2;

    @Getter
    String value;

    @Getter
    int order;

    int operand;

    @Override
    public boolean isPredictable(final boolean value) {
        return false;
    }

    @Override
    public boolean isPredictable(final int value) {
        return false;
    }

    @Override
    public boolean isPredictable(final long value) {
        return false;
    }

    @Override
    public boolean isPredictable(final double value) {
        return false;
    }

    @Override
    public boolean isPredictable(final float value) {
        return false;
    }

    @Override
    public boolean isPredictable(final @NotNull String value) {
        return false;
    }

    @Override
    public boolean isTwoOperand() {
        return (operand & TWO_OPERAND) == TWO_OPERAND;
    }

    @Override
    public boolean isOneOperand() {
        return (operand & ONE_OPERAND) == ONE_OPERAND;
    }

    private OperatorException error(final Object left, final Object right) {
        throw new OperatorException("Operator " + this + " is not applicable to "
                + "(" + left.getClass().getSimpleName() + ") " + left
                + " & "
                + "(" + right.getClass().getSimpleName() + ") " + right);
    }

    private OperatorException error(final Object value) {
        throw new OperatorException("Operator " + this + " is not applicable to "
                + "(" + value.getClass().getSimpleName() + ") " + value);
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
    public @NotNull Operand apply(final int left, final String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final int value) {
        throw error(value);
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
    public @NotNull Operand apply(final long left, final String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final long value) {
        throw error(value);
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
    public @NotNull Operand apply(final float left, final String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final float value) {
        throw error(value);
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
    public @NotNull Operand apply(final double left, final String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final double value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand apply(final String left, final int right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final String left, final long right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final String left, final float right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final String left, final double right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final String left, final String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final String value) {
        throw error(value);
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
    public @NotNull Operand apply(final String left, final boolean right) {
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
    public @NotNull Operand apply(final boolean left, final String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final boolean left, final boolean right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final boolean value) {
        throw error(value);
    }
}
