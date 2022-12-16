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

package io.github.whilein.jexpr.token.operand;

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandConstantKind;
import io.github.whilein.jexpr.api.token.operand.OperandVariable;
import io.github.whilein.jexpr.api.token.operator.BinaryLazyOperator;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class OperandDoubleImpl extends OperandNumber {

    double value;

    public OperandDoubleImpl(final double value) {
        super(value);

        this.value = value;
    }

    @Override
    public void print(final @NotNull StringBuilder out) {
        out.append(value);
    }

    @Override
    public boolean isPredicable(final @NotNull BinaryLazyOperator operator) {
        return operator.isPredictable(value);
    }

    @Override
    public @NotNull Operand getPredictedResult(final @NotNull BinaryLazyOperator operator) {
        return operator.getPredictedResult(value);
    }

    @Override
    public @NotNull Operand apply(final @NotNull Operand operand, final @NotNull BinaryOperator operator) {
        return operand.applyToDouble(value, operator);
    }

    @Override
    public @NotNull Operand applyToInt(final int number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, value);
    }

    @Override
    public @NotNull Operand applyToLong(final long number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, value);
    }

    @Override
    public @NotNull Operand applyToDouble(final double number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, value);
    }

    @Override
    public @NotNull Operand applyToFloat(final float number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, value);
    }

    @Override
    public @NotNull Operand applyToString(final @NotNull String value, final @NotNull BinaryOperator operator) {
        return operator.apply(value, this.value);
    }

    @Override
    public @NotNull Operand applyToBoolean(final boolean value, final @NotNull BinaryOperator operator) {
        return operator.apply(value, this.value);
    }

    @Override
    public @NotNull Operand applyToVariable(
            final @NotNull OperandVariable variable,
            final @NotNull BinaryOperator operator
    ) {
        return operator.apply(variable, this.value);
    }

    @Override
    public @NotNull Operand applyToObject(final @Nullable Object value, final @NotNull BinaryOperator operator) {
        return operator.apply(value, this.value);
    }

    @Override
    public @NotNull Operand apply(final @NotNull UnaryOperator operator) {
        return operator.apply(value);
    }

    @Override
    public @NotNull OperandConstantKind getKind() {
        return OperandConstantKind.DOUBLE;
    }
}
