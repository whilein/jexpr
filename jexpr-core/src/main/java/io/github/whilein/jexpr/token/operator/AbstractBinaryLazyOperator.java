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
import io.github.whilein.jexpr.api.token.operator.BinaryLazyOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public abstract class AbstractBinaryLazyOperator extends AbstractBinaryOperator implements BinaryLazyOperator {

    protected AbstractBinaryLazyOperator(final String value, final int presence) {
        super(value, presence);
    }

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
    public boolean isPredictable(final Object value) {
        return false;
    }

    @Override
    public boolean isPredictable(final @NotNull String value) {
        return false;
    }

    private OperatorException error(final Object value) {
        throw new OperatorException("Operator " + this + " is not predictable from "
                + "(" + value.getClass().getSimpleName() + ") " + value);
    }

    @Override
    public @NotNull Operand getPredictedResult(final int value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand getPredictedResult(final long value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand getPredictedResult(final float value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand getPredictedResult(final double value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand getPredictedResult(final Object value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand getPredictedResult(final String value) {
        throw error(value);
    }

    @Override
    public @NotNull Operand getPredictedResult(final boolean value) {
        throw error(value);
    }

}
