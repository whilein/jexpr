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

package io.github.whilein.jexpr.token.operand;

import io.github.whilein.jexpr.api.token.operand.*;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
final class OperandUnaryImpl extends OperandBase implements OperandUnary {

    OperandVariable member;
    UnaryOperator operator;

    @Override
    public String toString() {
        return operator.getValue() + member;
    }

    @Override
    public void print(final @NotNull StringBuilder out) {
        out.append(operator.getValue());

        if (member instanceof OperandBinary) {
            out.append('(');
            member.print(out);
            out.append(')');
        } else {
            member.print(out);
        }
    }

    @Override
    public @NotNull Operand solve(final @NotNull OperandVariableResolver resolver) {
        return member.solve(resolver).apply(operator);
    }
}
