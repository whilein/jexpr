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

package io.github.whilein.jexpr.operator;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.undefined.OperandUndefined;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author whilein
 */
public interface BinaryOperator extends Operator {

    boolean isUnaryExpected(@NotNull UnaryOperator operator);

    @NotNull Operand apply(@NotNull OperandUndefined left, @NotNull OperandUndefined right);

    @NotNull Operand apply(@NotNull OperandUndefined left, int right);

    @NotNull Operand apply(@NotNull OperandUndefined left, float right);

    @NotNull Operand apply(@NotNull OperandUndefined left, double right);

    @NotNull Operand apply(@NotNull OperandUndefined left, long right);

    @NotNull Operand apply(@NotNull OperandUndefined left, boolean right);

    @NotNull Operand apply(@NotNull OperandUndefined left, @NotNull String right);

    @NotNull Operand apply(@NotNull OperandUndefined left, @Nullable Object right);

    @NotNull Operand apply(int left, @NotNull OperandUndefined right);

    @NotNull Operand apply(float left, @NotNull OperandUndefined right);

    @NotNull Operand apply(double left, @NotNull OperandUndefined right);

    @NotNull Operand apply(long left, @NotNull OperandUndefined right);

    @NotNull Operand apply(boolean left, @NotNull OperandUndefined right);

    @NotNull Operand apply(@NotNull String left, @NotNull OperandUndefined right);

    @NotNull Operand apply(@Nullable Object left, @NotNull OperandUndefined right);

    @NotNull Operand apply(@NotNull String left, int right);

    @NotNull Operand apply(@NotNull String left, long right);

    @NotNull Operand apply(@NotNull String left, float right);

    @NotNull Operand apply(@NotNull String left, double right);

    @NotNull Operand apply(@NotNull String left, @NotNull String right);

    @NotNull Operand apply(@NotNull String left, boolean right);

    @NotNull Operand apply(boolean left, int right);

    @NotNull Operand apply(boolean left, long right);

    @NotNull Operand apply(boolean left, float right);

    @NotNull Operand apply(boolean left, double right);

    @NotNull Operand apply(boolean left, @NotNull String right);

    @NotNull Operand apply(boolean left, boolean right);

    @NotNull Operand apply(int left, @Nullable Object right);

    @NotNull Operand apply(long left, @Nullable Object right);

    @NotNull Operand apply(float left, @Nullable Object right);

    @NotNull Operand apply(double left, @Nullable Object right);

    @NotNull Operand apply(boolean left, @Nullable Object right);

    @NotNull Operand apply(@Nullable Object left, int right);

    @NotNull Operand apply(@Nullable Object left, long right);

    @NotNull Operand apply(@Nullable Object left, float right);

    @NotNull Operand apply(@Nullable Object left, double right);

    @NotNull Operand apply(@Nullable Object left, boolean right);

    @NotNull Operand apply(@NotNull String left, @Nullable Object right);

    @NotNull Operand apply(@Nullable Object left, @NotNull String right);

    @NotNull Operand apply(@Nullable Object left, @Nullable Object right);

    @NotNull Operand apply(double left, int right);

    @NotNull Operand apply(double left, long right);

    @NotNull Operand apply(double left, float right);

    @NotNull Operand apply(double left, double right);

    @NotNull Operand apply(double left, @NotNull String right);

    @NotNull Operand apply(double left, boolean right);

    @NotNull Operand apply(float left, int right);

    @NotNull Operand apply(float left, long right);

    @NotNull Operand apply(float left, float right);

    @NotNull Operand apply(float left, double right);

    @NotNull Operand apply(float left, @NotNull String right);

    @NotNull Operand apply(float left, boolean right);

    @NotNull Operand apply(int left, int right);

    @NotNull Operand apply(int left, long right);

    @NotNull Operand apply(int left, float right);

    @NotNull Operand apply(int left, double right);

    @NotNull Operand apply(int left, @NotNull String right);

    @NotNull Operand apply(int left, boolean right);

    @NotNull Operand apply(long left, int right);

    @NotNull Operand apply(long left, long right);

    @NotNull Operand apply(long left, float right);

    @NotNull Operand apply(long left, double right);

    @NotNull Operand apply(long left, @NotNull String right);

    @NotNull Operand apply(long left, boolean right);

}
