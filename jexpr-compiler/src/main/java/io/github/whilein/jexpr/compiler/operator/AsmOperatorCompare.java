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
import io.github.whilein.jexpr.compiler.OperandOrigin;
import io.github.whilein.jexpr.compiler.StackLazyOperand;
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
public final class AsmOperatorCompare extends AbstractAsmBinaryOperator {

    int opcode;
    int compareOpcode;
    int doubleOpcode;
    int floatOpcode;

    @Override
    public @NotNull Type getOutputType(final @Nullable Type left, final @Nullable Type right) {
        ensureNumberType(left);
        ensureNumberType(right);

        return Type.BOOLEAN_TYPE;
    }

    @Override
    public void compile(
            final @NotNull AsmMethodCompiler compiler,
            final @NotNull OperandOrigin origin,
            final @NotNull StackLazyOperand left,
            final @NotNull StackLazyOperand right
    ) {
        val type = compileNumber(compiler, left, right);

        val label = new Label();
        val endLabel = new Label();

        switch (type.getSort()) {
            case Type.BYTE:
            case Type.CHAR:
            case Type.SHORT:
            case Type.INT:
                compiler.visitJumpInsn(opcode, label);
                break;
            case Type.FLOAT:
                compiler.visitInsn(floatOpcode);
                compiler.visitJumpInsn(compareOpcode, label);
                break;
            case Type.DOUBLE:
                compiler.visitInsn(doubleOpcode);
                compiler.visitJumpInsn(compareOpcode, label);
                break;
            case Type.LONG:
                compiler.visitInsn(Opcodes.LCMP);
                compiler.visitJumpInsn(compareOpcode, label);
                break;
        }

        compiler.visitInsn(Opcodes.ICONST_1);
        compiler.visitJumpInsn(Opcodes.GOTO, endLabel);
        compiler.visitLabel(label);
        compiler.visitInsn(Opcodes.ICONST_0);
        compiler.visitLabel(endLabel);
    }

}
