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
import io.github.whilein.jexpr.compiler.operator.AbstractAsmOperatorBitwiseShift;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;

/**
 * @author whilein
 */
public final class AsmOperatorBitwiseRightShift extends AbstractAsmOperatorBitwiseShift {

    @Override
    public void compile(
            final @NotNull AsmMethodCompiler compiler,
            final @NotNull StackLazyOperand left,
            final @NotNull StackLazyOperand right
    ) {
        compiler.visitInsn(compileShift(compiler, left, right).getOpcode(Opcodes.ISHR));
    }

}