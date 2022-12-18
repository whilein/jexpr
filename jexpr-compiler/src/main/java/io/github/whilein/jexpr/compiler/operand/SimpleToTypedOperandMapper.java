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

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandVariable;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.compiler.local.LocalMap;
import io.github.whilein.jexpr.compiler.operand.type.TypedOperands;
import io.github.whilein.jexpr.compiler.operator.AsmBinaryOperator;
import io.github.whilein.jexpr.compiler.operator.AsmOperatorRegistry;
import io.github.whilein.jexpr.compiler.operator.AsmUnaryOperator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class SimpleToTypedOperandMapper implements ToTypedOperandMapper {

    AsmOperatorRegistry<AsmBinaryOperator, BinaryOperator> binaryOperatorRegistry;

    AsmOperatorRegistry<AsmUnaryOperator, UnaryOperator> unaryOperatorRegistry;

    @Getter
    LocalMap localMap;

    @Override
    public TypedOperand mapUnary(@NotNull OperandVariable left, @NotNull UnaryOperator op) {
        val asmOperator = unaryOperatorRegistry.get(op.getClass());

        if (asmOperator == null) {
            throw new IllegalArgumentException("Got unknown unary operator: " + op);
        }

        val typedMember = left.apply(this);

        return TypedOperands.unary(
                (TypedOperandVariable) typedMember,
                asmOperator
        );
    }


    @Override
    public TypedOperand mapBinary(@NotNull Operand left, @NotNull Operand right, @NotNull BinaryOperator op) {
        val asmOperator = binaryOperatorRegistry.get(op.getClass());

        if (asmOperator == null) {
            throw new IllegalArgumentException("Got unknown binary operator: " + op);
        }

        val typedLeft = left.apply(this);
        val typedRight = right.apply(this);

        return TypedOperands.binary(
                typedLeft,
                typedRight,
                asmOperator
        );
    }

    @Override
    public TypedOperand mapReference(@NotNull String value) {
        return TypedOperands.reference(localMap.get(value));
    }

    @Override
    public TypedOperand mapObject(@Nullable Object value) {
        if (value != null) {
            throw new UnsupportedOperationException("Cannot compile object into bytecode");
        }

        return TypedOperands.constantNull();
    }

    @Override
    public TypedOperand mapInt(int value) {
        return TypedOperands.constantInt(value);
    }

    @Override
    public TypedOperand mapFloat(float value) {
        return TypedOperands.constantFloat(value);
    }

    @Override
    public TypedOperand mapLong(long value) {
        return TypedOperands.constantLong(value);
    }

    @Override
    public TypedOperand mapDouble(double value) {
        return TypedOperands.constantDouble(value);
    }

    @Override
    public TypedOperand mapBoolean(boolean value) {
        return TypedOperands.constantBoolean(value);
    }

    @Override
    public TypedOperand mapString(@NotNull String value) {
        return TypedOperands.constantString(value);
    }

}
