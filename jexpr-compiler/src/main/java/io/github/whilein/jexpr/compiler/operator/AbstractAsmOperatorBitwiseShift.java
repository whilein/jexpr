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
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
public abstract class AbstractAsmOperatorBitwiseShift extends AbstractAsmOperator {


    @Override
    public @NotNull Type getOutputType(final @NotNull Type left, final @NotNull Type right) {
        val leftType = getIntegralType(left);
        ensureIntegralType(right);

        return leftType;
    }

    protected static Type compileShift(
            final AsmMethodCompiler compiler,
            final StackLazyOperand left,
            final StackLazyOperand right
    ) {
        Type leftType = left.getType(), rightType = right.getType();

        left.load();
        leftType = compiler.unbox(leftType);

        right.load();
        rightType = compiler.unbox(rightType);
        compiler.cast(rightType, Type.INT_TYPE);

        return leftType;
    }

}
