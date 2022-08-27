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
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
public abstract class AbstractAsmOperatorCompare extends AbstractAsmOperator {


    @Override
    public @NotNull Type getOutputType(final @NotNull Type left, final @NotNull Type right) {
        val leftType = getIntegralType(left);
        ensureIntegralType(right);

        return leftType;
    }

    //   // access flags 0x9
    //  public static a(I)Z
    //   L0
    //    LINENUMBER 7 L0
    //    ILOAD 0
    //    I2D
    //    DCONST_1
    //    DCMPG
    //    IFGE L1
    //    ICONST_1
    //    GOTO L2
    //   L1
    //   FRAME SAME
    //    ICONST_0
    //   L2
    //   FRAME SAME1 I
    //    IRETURN
    //   L3
    //    LOCALVARIABLE i I L0 L3 0
    //    MAXSTACK = 4
    //    MAXLOCALS = 1
    //
    //  // access flags 0x9
    //  public static b(I)Z
    //   L0
    //    LINENUMBER 11 L0
    //    ILOAD 0
    //    I2D
    //    DCONST_1
    //    DCMPL
    //    IFLE L1
    //    ICONST_1
    //    GOTO L2
    //   L1
    //   FRAME SAME
    //    ICONST_0
    //   L2
    //   FRAME SAME1 I
    //    IRETURN
    //   L3
    //    LOCALVARIABLE i I L0 L3 0
    //    MAXSTACK = 4
    //    MAXLOCALS = 1
    //
    //  // access flags 0x9
    //  public static c(I)Z
    //   L0
    //    LINENUMBER 15 L0
    //    ILOAD 0
    //    I2D
    //    DCONST_1
    //    DCMPG
    //    IFGT L1
    //    ICONST_1
    //    GOTO L2
    //   L1
    //   FRAME SAME
    //    ICONST_0
    //   L2
    //   FRAME SAME1 I
    //    IRETURN
    //   L3
    //    LOCALVARIABLE i I L0 L3 0
    //    MAXSTACK = 4
    //    MAXLOCALS = 1
    //
    //  // access flags 0x9
    //  public static d(I)Z
    //   L0
    //    LINENUMBER 19 L0
    //    ILOAD 0
    //    I2D
    //    DCONST_1
    //    DCMPL
    //    IFLT L1
    //    ICONST_1
    //    GOTO L2
    //   L1
    //   FRAME SAME
    //    ICONST_0
    //   L2
    //   FRAME SAME1 I
    //    IRETURN
    //   L3
    //    LOCALVARIABLE i I L0 L3 0
    //    MAXSTACK = 4
    //    MAXLOCALS = 1

    protected static void compileCompare(
            final AsmMethodCompiler compiler,
            final StackLazyOperand left,
            final StackLazyOperand right,
            final int opcode,
            final int compareOpcode,
            final int doubleOpcode,
            final int floatOpcode
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
