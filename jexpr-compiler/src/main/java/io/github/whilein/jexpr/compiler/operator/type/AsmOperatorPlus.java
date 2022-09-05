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
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
public final class AsmOperatorPlus extends AbstractAsmOperator {

    @Override
    public @NotNull Type getOutputType(final @Nullable Type value) {
        return getNumberType(value);
    }

    private static boolean isAnyString(final Type left, final Type right) {
        return (left != null && left.equals(TypeUtils.STRING_TYPE))
                || (right != null && right.equals(TypeUtils.STRING_TYPE));
    }

    @Override
    public @NotNull Type getOutputType(final @Nullable Type left, final @Nullable Type right) {
        if (isAnyString(left, right)) { // concat
            return TypeUtils.STRING_TYPE;
        }

        return getNumberType(left, right);
    }

    @Override
    public void compile(final @NotNull AsmMethodCompiler compiler, final @Nullable StackLazyOperand origin,
                        final @NotNull StackLazyOperand value) {
        value.load();

        compiler.unbox(value.getType());
    }

    @Override
    public void compile(
            final @NotNull AsmMethodCompiler compiler,
            final @Nullable StackLazyOperand origin,
            final @NotNull StackLazyOperand left,
            final @NotNull StackLazyOperand right
    ) {
        val leftType = left.getType();
        val rightType = right.getType();

        if (isAnyString(leftType, rightType)) { // concat
            compiler.beginConcat();

            left.load();

            if (!left.isConcatenated()) {
                compiler.concat(leftType);
            }

            right.load();

            if (!right.isConcatenated()) {
                compiler.concat(rightType);
            }

            if (origin != null) {
                origin.setConcatenated(true);
            }

            return;
        }

        compiler.visitInsn(compileNumber(compiler, left, right).getOpcode(Opcodes.IADD));
    }

}
