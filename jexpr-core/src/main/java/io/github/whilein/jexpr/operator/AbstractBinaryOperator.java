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
import org.jetbrains.annotations.NotNull;

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
    public @NotNull Operand apply(final String left, final Object right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final Object left, final String right) {
        throw error(left, right);
    }

    @Override
    public @NotNull Operand apply(final Object left, final Object right) {
        throw error(left, right);
    }


}
