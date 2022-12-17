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

package io.github.whilein.jexpr.compiler.operand.type;

import io.github.whilein.jexpr.api.token.operand.OperandConstantKind;
import io.github.whilein.jexpr.compiler.operand.TypedOperandConstant;
import io.github.whilein.jexpr.compiler.operand.TypedOperandVisitor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
@Value
class TypedOperandLongImpl implements TypedOperandConstant {

    long value;

    @Override
    public @NotNull Object getValue() {
        return value;
    }

    @Override
    public @NotNull OperandConstantKind getKind() {
        return OperandConstantKind.LONG;
    }

    @Override
    public void accept(@NotNull TypedOperandVisitor visitor) {
        visitor.visitLong(value);
    }

    @Override
    public @NotNull Type getType() {
        return Type.LONG_TYPE;
    }

}
