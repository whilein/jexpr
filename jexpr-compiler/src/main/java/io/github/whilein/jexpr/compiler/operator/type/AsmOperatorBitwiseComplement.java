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
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
public final class AsmOperatorBitwiseComplement extends AbstractAsmOperator {

    @Override
    public @NotNull Type getOutputType(final @NotNull Type value) {
        val integralType = getIntegralType(value);
        return integralType.getSort() == Type.LONG ? integralType : Type.INT_TYPE;
    }

    @Override
    public void compile(final @NotNull AsmMethodCompiler compiler, final @NotNull StackLazyOperand value) {
        value.load();

        val type = compiler.unbox(value.getType());

        switch (type.getSort()) {
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT:
            case Type.CHAR:
                compiler.visitInsn(Opcodes.ICONST_M1);
                compiler.visitInsn(Opcodes.IXOR);
                break;
            case Type.LONG:
                compiler.visitLdcInsn(-1L);
                compiler.visitInsn(Opcodes.LXOR);
        }
    }

}
