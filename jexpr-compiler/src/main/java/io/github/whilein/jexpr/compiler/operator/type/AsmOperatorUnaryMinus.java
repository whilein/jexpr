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

import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.compiler.AsmMethodCompiler;
import io.github.whilein.jexpr.compiler.OperandOrigin;
import io.github.whilein.jexpr.compiler.StackLazyOperand;
import io.github.whilein.jexpr.compiler.operator.AbstractAsmUnaryOperator;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
public final class AsmOperatorUnaryMinus extends AbstractAsmUnaryOperator {

    public AsmOperatorUnaryMinus(Class<? extends UnaryOperator> operatorType) {
        super(operatorType);
    }

    @Override
    public @NotNull Type getOutputType(final @Nullable Type value) {
        return getNumberType(value);
    }

    @Override
    public void compile(final @NotNull AsmMethodCompiler compiler, final @NotNull OperandOrigin origin,
                        final @NotNull StackLazyOperand value) {
        value.load();

        val type = compiler.unbox(value.getType());
        compiler.visitInsn(type.getOpcode(Opcodes.INEG));
    }

}
