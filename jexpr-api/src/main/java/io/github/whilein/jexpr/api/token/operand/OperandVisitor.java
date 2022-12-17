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

public interface OperandVisitor {

    default void visitInt(int value) {
        throw new IllegalStateException("Unexpected int");
    }

    default void visitFloat(float value) {
        throw new IllegalStateException("Unexpected float");
    }

    default void visitLong(long value) {
        throw new IllegalStateException("Unexpected long");
    }

    default void visitDouble(double value) {
        throw new IllegalStateException("Unexpected double");
    }

    default void visitBoolean(boolean value) {
        throw new IllegalStateException("Unexpected boolean");
    }

    default void visitString(@NotNull String value) {
        throw new IllegalStateException("Unexpected string");
    }

    default void visitReference(@NotNull String value) {
        throw new IllegalStateException("Unexpected reference");
    }

    default void visitObject(@Nullable Object value) {
        throw new IllegalStateException("Unexpected object");
    }

    default void visitBinary(@NotNull Operand left, @NotNull Operand right, @NotNull BinaryOperator op) {
        throw new IllegalStateException("Unexpected binary");
    }

    default void visitUnary(@NotNull OperandVariable left, @NotNull UnaryOperator op) {
        throw new IllegalStateException("Unexpected unary");
    }

}
