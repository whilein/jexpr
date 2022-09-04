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
import io.github.whilein.jexpr.operator.Operator;
import io.github.whilein.jexpr.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public interface Operand extends Token {

    @NotNull Number toNumber();

    boolean isPredicable(@NotNull Operator operator);

    @NotNull Operand apply(@NotNull Operand operand, @NotNull Operator operator);

    @NotNull Operand applyToInt(int number, @NotNull Operator operator);

    @NotNull Operand applyToLong(long number, @NotNull Operator operator);

    @NotNull Operand applyToDouble(double number, @NotNull Operator operator);

    @NotNull Operand applyToFloat(float number, @NotNull Operator operator);

    @NotNull Operand applyToString(@NotNull String value, @NotNull Operator operator);

    @NotNull Operand applyToBoolean(boolean value, @NotNull Operator operator);

    @NotNull Operand applyToUndefined(@NotNull OperandUndefined undefined, @NotNull Operator operator);

    //@NotNull Operand applyToNull(@NotNull Operator operator);

    @NotNull Object getValue();

    boolean isNumber();

    boolean isString();

    boolean isBoolean();

    boolean isDefined();

    @NotNull Operand apply(@NotNull Operator operator);

    @NotNull Operand solve(@NotNull UndefinedResolver resolver);

}
