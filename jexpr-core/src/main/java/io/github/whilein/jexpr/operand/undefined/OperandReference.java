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

package io.github.whilein.jexpr.operand.undefined;

import io.github.whilein.jexpr.UndefinedResolver;
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.OperandDelegate;
import io.github.whilein.jexpr.operand.defined.OperandBoolean;
import io.github.whilein.jexpr.operand.defined.OperandDouble;
import io.github.whilein.jexpr.operand.defined.OperandFloat;
import io.github.whilein.jexpr.operand.defined.OperandInteger;
import io.github.whilein.jexpr.operand.defined.OperandLong;
import io.github.whilein.jexpr.operand.defined.OperandObject;
import io.github.whilein.jexpr.operand.defined.OperandString;
import io.github.whilein.jexpr.operator.BinaryLazyOperator;
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.UnaryOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperandReference extends OperandDelegate<String> implements OperandUndefined {

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
        return operand.applyToUndefined(this, operator);
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
    public @NotNull Operand applyToUndefined(final @NotNull OperandUndefined undefined, final @NotNull BinaryOperator operator) {
        return OperandBinaryNode.valueOf(undefined, this, operator);
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
    public @NotNull Operand solve(final @NotNull UndefinedResolver resolver) {
        return resolver.resolve(delegatedValue);
    }

}
