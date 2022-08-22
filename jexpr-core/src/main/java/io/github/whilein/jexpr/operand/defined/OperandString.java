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

package io.github.whilein.jexpr.operand.defined;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.OperandDelegate;
import io.github.whilein.jexpr.operand.undefined.OperandUndefined;
import io.github.whilein.jexpr.operand.undefined.OperandUndefinedSequence;
import io.github.whilein.jexpr.operator.Operator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperandString extends OperandDelegate<String> implements OperandDefined {

    private OperandString(final String delegatedValue) {
        super(delegatedValue);
    }

    public static @NotNull Operand valueOf(final String value) {
        return new OperandString(value);
    }

    @Override
    public boolean isPredicable(final @NotNull Operator operator) {
        return operator.isPredictable(delegatedValue);
    }

    @Override
    public @NotNull Number toNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Operand apply(final @NotNull Operand operand, final @NotNull Operator operator) {
        return operand.applyToString(delegatedValue, operator);
    }

    @Override
    public @NotNull Operand applyToInt(final int number, final @NotNull Operator operator) {
        return operator.apply(number, delegatedValue);
    }

    @Override
    public @NotNull Operand applyToLong(final long number, final @NotNull Operator operator) {
        return operator.apply(number, delegatedValue);
    }

    @Override
    public @NotNull Operand applyToDouble(final double number, final @NotNull Operator operator) {
        return operator.apply(number, delegatedValue);
    }

    @Override
    public @NotNull Operand applyToFloat(final float number, final @NotNull Operator operator) {
        return operator.apply(number, delegatedValue);
    }

    @Override
    public @NotNull Operand applyToString(final @NotNull String value, final @NotNull Operator operator) {
        return operator.apply(value, this.delegatedValue);
    }

    @Override
    public @NotNull Operand applyToBoolean(final boolean value, final @NotNull Operator operator) {
        return operator.apply(value, this.delegatedValue);
    }

    @Override
    public @NotNull Operand applyToUndefined(final @NotNull OperandUndefined undefined, final @NotNull Operator operator) {
        return OperandUndefinedSequence.valueOf(undefined, this, operator);
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }


    @Override
    public @NotNull Operand apply(final @NotNull Operator operator) {
        return operator.apply(delegatedValue);
    }

}
