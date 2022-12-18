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

package io.github.whilein.jexpr.compiler.operand.type;

import io.github.whilein.jexpr.compiler.local.Local;
import io.github.whilein.jexpr.compiler.operand.*;
import io.github.whilein.jexpr.compiler.operator.AsmBinaryOperator;
import io.github.whilein.jexpr.compiler.operator.AsmUnaryOperator;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class TypedOperands {

    private static final TypedOperandConstant NULL = new TypedOperandNullImpl();

    public @NotNull TypedOperandConstant constantInt(int value) {
        return new TypedOperandIntImpl(value);
    }

    public @NotNull TypedOperandConstant constantLong(long value) {
        return new TypedOperandLongImpl(value);
    }

    public @NotNull TypedOperandConstant constantFloat(float value) {
        return new TypedOperandFloatImpl(value);
    }

    public @NotNull TypedOperandConstant constantDouble(double value) {
        return new TypedOperandDoubleImpl(value);
    }

    public @NotNull TypedOperandConstant constantBoolean(boolean value) {
        return new TypedOperandBooleanImpl(value);
    }

    public @NotNull TypedOperandConstant constantString(@NotNull String value) {
        return new TypedOperandStringImpl(value);
    }

    public @NotNull TypedOperandConstant constantNull() {
        return NULL;
    }

    public @NotNull TypedOperandReference reference(@NotNull Local local) {
        return new TypedOperandReferenceImpl(local);
    }

    public @NotNull TypedOperandUnary unary(@NotNull TypedOperandVariable member, @NotNull AsmUnaryOperator op) {
        return new TypedOperandUnaryImpl(member, op, op.getOutputType(member.getType()));
    }

    public @NotNull TypedOperandBinary binary(@NotNull TypedOperand left,
                                              @NotNull TypedOperand right,
                                              @NotNull AsmBinaryOperator op) {
        return new TypedOperandBinaryImpl(left, right, op, op.getOutputType(left.getType(), right.getType()));
    }

}
