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

import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OperandMapper<T> {

    default T mapConstant(Object object) {
        throw new IllegalStateException("Unexpected constant");
    }

    default T mapInt(int value) {
        return mapConstant(value);
    }

    default T mapFloat(float value) {
        return mapConstant(value);
    }

    default T mapLong(long value) {
        return mapConstant(value);
    }

    default T mapDouble(double value) {
        return mapConstant(value);
    }

    default T mapBoolean(boolean value) {
        return mapConstant(value);
    }

    default T mapString(@NotNull String value) {
        return mapConstant(value);
    }

    default T mapReference(@NotNull String value) {
        throw new IllegalStateException("Unexpected reference");
    }

    default T mapObject(@Nullable Object value) {
        return mapConstant(value);
    }

    default T mapBinary(@NotNull Operand left, @NotNull Operand right, @NotNull BinaryOperator op) {
        throw new IllegalStateException("Unexpected binary");
    }

    default T mapUnary(@NotNull OperandVariable left, @NotNull UnaryOperator op) {
        throw new IllegalStateException("Unexpected unary");
    }

}
