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
import io.github.whilein.jexpr.compiler.StackLazyOperand;
import io.github.whilein.jexpr.compiler.operator.AbstractAsmOperator;
import io.github.whilein.jexpr.compiler.util.TypeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
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
    public @NotNull Type getOutputType(final @NotNull Type left, final @NotNull Type right) {
        return Type.BOOLEAN_TYPE;
    }

    @Override
    public void compile(
            final @NotNull AsmMethodCompiler compiler,
            final @NotNull StackLazyOperand left,
            final @NotNull StackLazyOperand right
    ) {
        val primitive = TypeUtils.isPrimitive(left.getType()) || TypeUtils.isPrimitive(right.getType());

        Type castType = primitive && (TypeUtils.isNumber(left.getType()) || TypeUtils.isNumber(right.getType()))
                ? TypeUtils.getPreferredNumber(left.getType(), right.getType())
                : null;

        left.load();

        if (primitive) {
            val leftType = compiler.unbox(left.getType());

            if (castType != null) {
                compiler.cast(leftType, castType);
            }
        }

        right.load();

        if (primitive) {
            val rightType = compiler.unbox(right.getType());

            if (castType != null) {
                compiler.cast(rightType, castType);
            }
        }

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
            compiler.visitJumpInsn(negate ? Opcodes.IF_ICMPEQ : Opcodes.IF_ACMPNE, falseLabel);
        }

        compiler.visitInsn(Opcodes.ICONST_1);
        compiler.visitJumpInsn(Opcodes.GOTO, endLabel);
        compiler.visitLabel(falseLabel);
        compiler.visitInsn(Opcodes.ICONST_0);
        compiler.visitLabel(endLabel);
    }

}
