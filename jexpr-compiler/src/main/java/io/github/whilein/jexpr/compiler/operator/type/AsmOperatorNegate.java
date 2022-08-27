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
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
public final class AsmOperatorNegate extends AbstractAsmOperator {

    @Override
    public @NotNull Type getOutputType(final @NotNull Type value) {
        ensureBooleanType(value);

        return Type.BOOLEAN_TYPE;
    }

    //   // access flags 0x9
    //  public static g(Z)Z
    //   L0
    //    LINENUMBER 121 L0
    //    ILOAD 0
    //    IFNE L1
    //    ICONST_1
    //    GOTO L2
    //   L1
    //   FRAME SAME
    //    ICONST_0
    //   L2
    //   FRAME SAME1 I
    //    IRETURN
    //   L3
    //    LOCALVARIABLE b Z L0 L3 0
    //    MAXSTACK = 1
    //    MAXLOCALS = 1
    //}

    @Override
    public void compile(final @NotNull AsmMethodCompiler compiler, final @NotNull StackLazyOperand value) {
        value.load();
        compiler.unbox(value.getType());

        val falseLabel = new Label();
        val endLabel = new Label();
        compiler.visitJumpInsn(Opcodes.IFNE, falseLabel);
        compiler.visitInsn(Opcodes.ICONST_1);
        compiler.visitJumpInsn(Opcodes.GOTO, endLabel);
        compiler.visitLabel(falseLabel);
        compiler.visitInsn(Opcodes.ICONST_0);
        compiler.visitLabel(endLabel);
    }

}
