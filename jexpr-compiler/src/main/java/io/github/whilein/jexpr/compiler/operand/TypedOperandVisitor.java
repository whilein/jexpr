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

package io.github.whilein.jexpr.compiler.operand;

import io.github.whilein.jexpr.compiler.local.Local;
import io.github.whilein.jexpr.compiler.operator.AsmBinaryOperator;
import io.github.whilein.jexpr.compiler.operator.AsmUnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

public interface TypedOperandVisitor {

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

    default void visitReference(@NotNull Local local) {
        throw new IllegalStateException("Unexpected reference");
    }

    default void visitNull() {
        throw new IllegalStateException("Unexpected null");
    }

    default void visitBinary(@NotNull TypedOperand left, @NotNull TypedOperand right, @NotNull AsmBinaryOperator op,
                             @NotNull Type type) {
        throw new IllegalStateException("Unexpected binary");
    }

    default void visitUnary(@NotNull TypedOperandVariable member, @NotNull AsmUnaryOperator op,
                            @NotNull Type type) {
        throw new IllegalStateException("Unexpected unary");
    }

}
