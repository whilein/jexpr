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

package io.github.whilein.jexpr.token;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operator.Operator;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whilein
 */
public final class StoringTokenVisitor implements TokenVisitor {
    @Getter
    private final List<Object> tokens;

    public StoringTokenVisitor() {
        tokens = new ArrayList<>();
    }

    public void clear() {
        tokens.clear();
    }

    @Override
    public void visitOperand(final @NotNull Operand operand) {
        tokens.add(operand.getValue());
    }

    @Override
    public void visitOperator(final @NotNull Operator operator) {
        tokens.add(operator.getClass());
    }

    @Override
    public void visitParenthesesOpen() {
        tokens.add('(');
    }

    @Override
    public void visitParenthesesClose() {
        tokens.add(')');
    }

}
