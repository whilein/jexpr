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

import io.github.whilein.jexpr.api.token.operand.*;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Operands {

    private static final OperandConstant
            NULL = new OperandObjectImpl(null),
            TRUE = new OperandBooleanImpl(true),
            FALSE = new OperandBooleanImpl(false);

    private final OperandConstant[] INT_CACHE = new OperandConstant[256];
    private final OperandConstant[] LONG_CACHE = new OperandConstant[256];

    static {
        for (int i = -128; i <= 127; i++) {
            INT_CACHE[i + 128] = new OperandIntImpl(i);
            LONG_CACHE[i + 128] = new OperandLongImpl(i);
        }
    }

    public @NotNull OperandConstant constantInt(int value) {
        return value >= -128 && value <= 127 ? INT_CACHE[value + 128] : new OperandIntImpl(value);
    }

    public @NotNull OperandConstant constantLong(long value) {
        return value >= -128 && value <= 127 ? LONG_CACHE[(int) (value + 128)] : new OperandLongImpl(value);
    }

    public @NotNull OperandConstant constantFloat(float value) {
        return new OperandFloatImpl(value);
    }

    public @NotNull OperandConstant constantDouble(double value) {
        return new OperandDoubleImpl(value);
    }

    public @NotNull OperandConstant constantString(@NotNull String value) {
        return new OperandStringImpl(value);
    }

    public @NotNull OperandConstant constantObject(@Nullable Object object) {
        return object == null ? NULL : new OperandObjectImpl(object);
    }

    public @NotNull OperandConstant constantNull() {
        return constantObject(null);
    }

    public @NotNull OperandConstant constantBoolean(boolean b) {
        return b ? TRUE : FALSE;
    }

    public @NotNull OperandConstant constantTrue() {
        return TRUE;
    }

    public @NotNull OperandConstant constantFalse() {
        return FALSE;
    }

    public @NotNull OperandReferenceImpl reference(@NotNull String value) {
        return new OperandReferenceImpl(value);
    }

    public @NotNull OperandBinary binary(@NotNull Operand left, @NotNull Operand right, @NotNull BinaryOperator op) {
        if (left.isConstant() && right.isConstant()) {
            throw new IllegalStateException("Cannot create variable binary node from constant operands");
        }

        return new OperandBinaryImpl(left, right, op);
    }

    public @NotNull OperandUnary unary(@NotNull OperandVariable member, @NotNull UnaryOperator op) {
        return new OperandUnaryImpl(member, op);
    }

}
