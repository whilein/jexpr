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

package io.github.whilein.jexpr.api.token.operand;

import io.github.whilein.jexpr.api.token.operator.BinaryLazyOperator;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public interface OperandVariable extends Operand {
    @Override
    default @NotNull Number toNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean toBoolean() {
        throw new UnsupportedOperationException();
    }

    @Override
    default @NotNull Operand getPredictedResult(final @NotNull BinaryLazyOperator operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isPredicable(final @NotNull BinaryLazyOperator operator) {
        return false;
    }

    @Override
    default @NotNull Operand apply(final @NotNull Operand operand, final @NotNull BinaryOperator operator) {
        return operand.applyToVariable(this, operator);
    }

    @Override
    default @NotNull Operand applyToInt(final int number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, this);
    }

    @Override
    default @NotNull Operand applyToLong(final long number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, this);
    }

    @Override
    default @NotNull Operand applyToDouble(final double number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, this);
    }

    @Override
    default @NotNull Operand applyToFloat(final float number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, this);
    }

    @Override
    default @NotNull Operand applyToString(final @NotNull String value, final @NotNull BinaryOperator operator) {
        return operator.apply(value, this);
    }

    @Override
    default @NotNull Operand applyToBoolean(final boolean value, final @NotNull BinaryOperator operator) {
        return operator.apply(value, this);
    }

    @Override
    default @NotNull Operand applyToVariable(
            final @NotNull OperandVariable variable,
            final @NotNull BinaryOperator operator
    ) {
        return operator.apply(variable, this);
    }

    @Override
    default @NotNull Operand applyToObject(final Object value, final @NotNull BinaryOperator operator) {
        return operator.apply(value, this);
    }

    @Override
    default @NotNull Operand apply(final @NotNull UnaryOperator operator) {
        return operator.apply(this);
    }

    @Override
    default Object getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isNumber() {
        return false;
    }

    @Override
    default boolean isString() {
        return false;
    }

    @Override
    default boolean isBoolean() {
        return false;
    }

    @Override
    default boolean isConstant() {
        return false;
    }

}
