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

package io.github.whilein.jexpr.operand.variable;

import io.github.whilein.jexpr.OperandVariableResolver;
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.OperandDelegate;
import io.github.whilein.jexpr.operand.constant.OperandBoolean;
import io.github.whilein.jexpr.operand.constant.OperandDouble;
import io.github.whilein.jexpr.operand.constant.OperandFloat;
import io.github.whilein.jexpr.operand.constant.OperandInteger;
import io.github.whilein.jexpr.operand.constant.OperandLong;
import io.github.whilein.jexpr.operand.constant.OperandObject;
import io.github.whilein.jexpr.operand.constant.OperandString;
import io.github.whilein.jexpr.operator.BinaryLazyOperator;
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.UnaryOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperandReference extends OperandDelegate<String> implements OperandVariable {

    private OperandReference(final String value) {
        super(value);
    }

    public static @NotNull Operand valueOf(final @NotNull String reference) {
        return new OperandReference(reference);
    }

    @Override
    public void toString(final @NotNull StringBuilder out) {
        out.append(delegatedValue);
    }

    @Override
    public boolean isPredicable(final @NotNull BinaryLazyOperator operator) {
        return false;
    }

    @Override
    public @NotNull Operand getPredictedResult(final @NotNull BinaryLazyOperator operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Operand apply(final @NotNull Operand operand, final @NotNull BinaryOperator operator) {
        return operand.applyToVariable(this, operator);
    }

    @Override
    public @NotNull Operand applyToInt(final int number, final @NotNull BinaryOperator operator) {
        return OperandBinaryNode.valueOf(OperandInteger.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToLong(final long number, final @NotNull BinaryOperator operator) {
        return OperandBinaryNode.valueOf(OperandLong.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToDouble(final double number, final @NotNull BinaryOperator operator) {
        return OperandBinaryNode.valueOf(OperandDouble.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToFloat(final float number, final @NotNull BinaryOperator operator) {
        return OperandBinaryNode.valueOf(OperandFloat.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToString(final @NotNull String value, final @NotNull BinaryOperator operator) {
        return OperandBinaryNode.valueOf(OperandString.valueOf(value), this, operator);
    }

    @Override
    public @NotNull Operand applyToBoolean(final boolean value, final @NotNull BinaryOperator operator) {
        return OperandBinaryNode.valueOf(OperandBoolean.valueOf(value), this, operator);
    }

    @Override
    public @NotNull Operand applyToVariable(final @NotNull OperandVariable variable, final @NotNull BinaryOperator operator) {
        return OperandBinaryNode.valueOf(variable, this, operator);
    }

    @Override
    public @NotNull Operand applyToObject(final Object value, final @NotNull BinaryOperator operator) {
        return OperandBinaryNode.valueOf(OperandObject.valueOf(value), this, operator);
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public @NotNull Operand apply(final @NotNull UnaryOperator operator) {
        return OperandUnaryNode.valueOf(this, operator);
    }

    @Override
    public @NotNull Operand solve(final @NotNull OperandVariableResolver resolver) {
        return resolver.resolve(delegatedValue);
    }

}
