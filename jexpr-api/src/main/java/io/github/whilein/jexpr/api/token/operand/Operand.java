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

import io.github.whilein.jexpr.api.token.Token;
import io.github.whilein.jexpr.api.token.operator.BinaryLazyOperator;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author whilein
 */
public interface Operand extends Token {

    <T> @NotNull T apply(@NotNull OperandMapper<T> mapper);

    void accept(@NotNull OperandVisitor visitor);

    void print(@NotNull StringBuilder out);

    boolean isPredicable(@NotNull BinaryLazyOperator operator);

    @NotNull Operand getPredictedResult(@NotNull BinaryLazyOperator operator);

    @NotNull Operand apply(@NotNull Operand operand, @NotNull BinaryOperator operator);

    @NotNull Operand applyToInt(int number, @NotNull BinaryOperator operator);

    @NotNull Operand applyToLong(long number, @NotNull BinaryOperator operator);

    @NotNull Operand applyToDouble(double number, @NotNull BinaryOperator operator);

    @NotNull Operand applyToFloat(float number, @NotNull BinaryOperator operator);

    @NotNull Operand applyToString(@NotNull String value, @NotNull BinaryOperator operator);

    @NotNull Operand applyToBoolean(boolean value, @NotNull BinaryOperator operator);

    @NotNull Operand applyToVariable(@NotNull OperandVariable variable, @NotNull BinaryOperator operator);

    @NotNull Operand applyToObject(@Nullable Object value, @NotNull BinaryOperator operator);

    @NotNull Operand apply(@NotNull UnaryOperator operator);

    @Contract(pure = true)
    Object getValue();

    @Contract(pure = true)
    boolean isNumber();

    @Contract(pure = true)
    boolean isString();

    @Contract(pure = true)
    boolean isBoolean();

    @Contract(pure = true)
    boolean isConstant();

    @NotNull Operand solve(@NotNull OperandVariableResolver resolver);

}
