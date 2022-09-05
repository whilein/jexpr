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

package io.github.whilein.jexpr.compiler.operator.type;

import io.github.whilein.jexpr.compiler.AsmMethodCompiler;
import io.github.whilein.jexpr.compiler.OperandOrigin;
import io.github.whilein.jexpr.compiler.StackLazyOperand;
import io.github.whilein.jexpr.compiler.operator.AbstractAsmOperator;
import io.github.whilein.jexpr.compiler.util.TypeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class AsmOperatorEquals extends AbstractAsmOperator {

    boolean negate;

    @Override
    public @NotNull Type getOutputType(final @Nullable Type left, final @Nullable Type right) {
        boolean leftNull, leftPrimitive, rightNull, rightPrimitive;

        leftPrimitive = !(leftNull = left == null) && TypeUtils.isPrimitive(left);
        rightPrimitive = !(rightNull = right == null) && TypeUtils.isPrimitive(right);

        if ((leftNull || rightNull) && (leftPrimitive || rightPrimitive)) {
            throw new UnsupportedOperationException(getClass().getName() + ": unable to compare null and primitive");
        }

        return Type.BOOLEAN_TYPE;
    }

    private static Type prepare(AsmMethodCompiler compiler, StackLazyOperand operand, boolean primitive, Type castType) {
        Type type = operand.getType();

        if (type != null) {
            operand.load();

            if (operand.isConcatenated()) {
                compiler.endConcat();
            }

            if (primitive) {
                type = compiler.unbox(type);

                if (castType != null) {
                    compiler.cast(type, castType);
                }
            }
        }

        return type;
    }

    @Override
    public void compile(
            final @NotNull AsmMethodCompiler compiler,
            final @NotNull OperandOrigin origin,
            final @NotNull StackLazyOperand left,
            final @NotNull StackLazyOperand right
    ) {
        boolean leftNull, rightNull, leftPrimitive, rightPrimitive, leftNumber, rightNumber;

        Type leftType = left.getType(), rightType = right.getType();

        if (!(leftNull = leftType == null)) {
            leftPrimitive = TypeUtils.isPrimitive(leftType);
            leftNumber = TypeUtils.isNumber(leftType);
        } else {
            leftPrimitive = leftNumber = false;
        }

        if (!(rightNull = rightType == null)) {
            rightPrimitive = TypeUtils.isPrimitive(rightType);
            rightNumber = TypeUtils.isNumber(rightType);
        } else {
            rightNumber = rightPrimitive = false;
        }

        final Type castType = leftNumber && rightNumber
                ? TypeUtils.getPreferredNumber(leftType, rightType)
                : null;

        val primitive = leftPrimitive || rightPrimitive;

        prepare(compiler, left, primitive, castType);
        prepare(compiler, right, primitive, castType);

        val endLabel = new Label();
        val falseLabel = new Label();

        if (primitive) {
            val outputType = castType == null ? Type.BOOLEAN_TYPE : castType;

            switch (outputType.getSort()) {
                case Type.BOOLEAN:
                case Type.BYTE:
                case Type.SHORT:
                case Type.CHAR:
                case Type.INT:
                    compiler.visitJumpInsn(negate ? Opcodes.IF_ICMPEQ : Opcodes.IF_ICMPNE, falseLabel);
                    break;
                case Type.LONG:
                    compiler.visitInsn(Opcodes.LCMP);
                    compiler.visitJumpInsn(negate ? Opcodes.IFEQ : Opcodes.IFNE, falseLabel);
                    break;
                case Type.DOUBLE:
                    compiler.visitInsn(Opcodes.DCMPL);
                    compiler.visitJumpInsn(negate ? Opcodes.IFEQ : Opcodes.IFNE, falseLabel);
                    break;
                case Type.FLOAT:
                    compiler.visitInsn(Opcodes.FCMPL);
                    compiler.visitJumpInsn(negate ? Opcodes.IFEQ : Opcodes.IFNE, falseLabel);
                    break;
            }
        } else {
            if (leftNull || rightNull) {
                compiler.visitJumpInsn(negate ? Opcodes.IFNONNULL : Opcodes.IFNULL, falseLabel);
            } else {
                compiler.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "equals",
                        "(Ljava/lang/Object;)Z", false);

                compiler.visitJumpInsn(negate ? Opcodes.IFNE : Opcodes.IFEQ, falseLabel);
            }
        }

        compiler.visitInsn(Opcodes.ICONST_1);
        compiler.visitJumpInsn(Opcodes.GOTO, endLabel);
        compiler.visitLabel(falseLabel);
        compiler.visitInsn(Opcodes.ICONST_0);
        compiler.visitLabel(endLabel);
    }

}
