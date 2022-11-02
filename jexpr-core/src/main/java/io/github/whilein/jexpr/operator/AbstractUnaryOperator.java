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
import io.github.whilein.jexpr.operand.variable.OperandUnaryNode;
import io.github.whilein.jexpr.operand.variable.OperandVariable;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public abstract class AbstractUnaryOperator extends AbstractOperator implements UnaryOperator {

    protected AbstractUnaryOperator(final String value, final int presence) {
        super(value, presence);
    }

    private OperatorException error(final Object value) {
        throw new OperatorException("Operator " + this + " is not applicable to "
                + "(" + value.getClass().getSimpleName() + ") " + value);
    }

    @Override
    public @NotNull Operand apply(final @NotNull OperandVariable variable) {
        return OperandUnaryNode.valueOf(variable, this);
    }

    @Override
    public @NotNull Operand apply(final int value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand apply(final long value) {
        throw error(value);
    }

    public @NotNull Operand apply(final float value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand apply(final double value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand apply(final String value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand apply(final Object value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand apply(final boolean value) {
        throw error(value);
    }

}
