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
import io.github.whilein.jexpr.compiler.util.TypeUtils;
import lombok.val;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
public abstract class AbstractAsmBinaryOperator extends AbstractAsmOperator implements AsmBinaryOperator {
    protected static Type compileNumber(
            final AsmMethodCompiler compiler,
            final StackLazyOperand left,
            final StackLazyOperand right
    ) {
        Type leftType = left.getType(), rightType = right.getType();
        assert leftType != null && rightType != null;

        val type = TypeUtils.getPreferredNumber(leftType, rightType);

        left.load();
        leftType = compiler.unbox(left.getType());
        compiler.cast(leftType, type);

        right.load();
        rightType = compiler.unbox(right.getType());
        compiler.cast(rightType, type);

        return type;
    }

}
