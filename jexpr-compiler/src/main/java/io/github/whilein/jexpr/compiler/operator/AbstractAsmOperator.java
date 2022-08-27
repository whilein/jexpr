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

package io.github.whilein.jexpr.compiler.operator;

import io.github.whilein.jexpr.compiler.AsmMethodCompiler;
import io.github.whilein.jexpr.compiler.StackLazyOperand;
import io.github.whilein.jexpr.compiler.util.TypeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractAsmOperator implements AsmOperator {

    protected static Type compileNumber(
            final AsmMethodCompiler compiler,
            final StackLazyOperand left,
            final StackLazyOperand right
    ) {
        Type leftType = left.getType(), rightType = right.getType();

        val type = TypeUtils.getPreferredNumber(leftType, rightType);

        left.load();
        leftType = compiler.unbox(left.getType());
        compiler.cast(leftType, type);

        right.load();
        rightType = compiler.unbox(right.getType());
        compiler.cast(rightType, type);

        return type;
    }

    protected static Type getNumberType(final Type left, final Type right) {
        try {
            val leftNumber = getNumberType(left);
            val rightNumber = getNumberType(right);

            return TypeUtils.getPreferredNumber(leftNumber, rightNumber);
        } catch (final UnsupportedOperationException e) {
            throw new UnsupportedOperationException("operator is not applicable to " + left + " & " + right);
        }
    }

    protected static Type getIntegralType(final Type left, final Type right) {
        try {
            val leftNumber = getIntegralType(left);
            val rightNumber = getIntegralType(right);

            return TypeUtils.getPreferredNumber(leftNumber, rightNumber);
        } catch (final UnsupportedOperationException e) {
            throw new UnsupportedOperationException("operator is not applicable to " + left + " & " + right);
        }
    }

    protected static void ensureIntegralType(final Type type) {
        if (TypeUtils.isPrimitiveIntegral(type) || TypeUtils.isIntegralWrapper(type)) {
            return;
        }

        throw new UnsupportedOperationException("operator is not applicable to " + type);
    }

    protected static Type getIntegralType(final Type type) {
        if (TypeUtils.isPrimitiveIntegral(type)) {
            return type;
        }

        if (TypeUtils.isIntegralWrapper(type)) {
            return TypeUtils.getPrimitive(type);
        }

        throw new UnsupportedOperationException("operator is not applicable to " + type);
    }

    protected static Type getNumberType(final Type type) {
        if (TypeUtils.isPrimitiveNumber(type)) {
            return type;
        }

        if (TypeUtils.isNumberWrapper(type)) {
            return TypeUtils.getPrimitive(type);
        }

        throw new UnsupportedOperationException("operator is not applicable to " + type);
    }

    @Override
    public @NotNull Type getOutputType(final @NotNull Type value) {
        throw new UnsupportedOperationException(getClass().getName() + ": one operand is not applicable to " + value);
    }

    @Override
    public @NotNull Type getOutputType(final @NotNull Type left, final @NotNull Type right) {
        throw new UnsupportedOperationException(getClass().getName() + ": two operand is not applicable");
    }

    @Override
    public void compile(
            final @NotNull AsmMethodCompiler compiler,
            final @NotNull StackLazyOperand left,
            final @NotNull StackLazyOperand right
    ) {
        throw new UnsupportedOperationException(getClass().getName() + ": two operand is not applicable");
    }

    @Override
    public void compile(
            final @NotNull AsmMethodCompiler compiler,
            final @NotNull StackLazyOperand value
    ) {
        throw new UnsupportedOperationException(getClass().getName() + ": one operand is not applicable");
    }

}
