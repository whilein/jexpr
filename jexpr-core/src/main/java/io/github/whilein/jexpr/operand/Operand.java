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

package io.github.whilein.jexpr.operand;

import io.github.whilein.jexpr.UndefinedResolver;
import io.github.whilein.jexpr.operand.undefined.OperandUndefined;
import io.github.whilein.jexpr.operator.BinaryLazyOperator;
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author whilein
 */
public interface Operand {

    @NotNull Number toNumber();

    boolean toBoolean();

    boolean isPredicable(@NotNull BinaryLazyOperator operator);

    @NotNull Operand getPredictedResult(@NotNull BinaryLazyOperator operator);

    @NotNull Operand apply(@NotNull Operand operand, @NotNull BinaryOperator operator);

    @NotNull Operand applyToInt(int number, @NotNull BinaryOperator operator);

    @NotNull Operand applyToLong(long number, @NotNull BinaryOperator operator);

    @NotNull Operand applyToDouble(double number, @NotNull BinaryOperator operator);

    @NotNull Operand applyToFloat(float number, @NotNull BinaryOperator operator);

    @NotNull Operand applyToString(@NotNull String value, @NotNull BinaryOperator operator);

    @NotNull Operand applyToBoolean(boolean value, @NotNull BinaryOperator operator);

    @NotNull Operand applyToUndefined(@NotNull OperandUndefined undefined, @NotNull BinaryOperator operator);

    @NotNull Operand applyToObject(@Nullable Object value, @NotNull BinaryOperator operator);

    Object getValue();

    boolean isNumber();

    boolean isString();

    boolean isBoolean();

    boolean isDefined();

    @NotNull Operand apply(@NotNull UnaryOperator operator);

    @NotNull Operand solve(@NotNull UndefinedResolver resolver);

}
