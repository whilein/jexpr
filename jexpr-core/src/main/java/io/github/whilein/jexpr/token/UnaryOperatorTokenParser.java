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

import io.github.whilein.jexpr.api.token.TokenVisitor;
import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operator.Operator;
import io.github.whilein.jexpr.api.token.operator.OperatorRegistry;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class UnaryOperatorTokenParser extends AbstractOperatorTokenParser<UnaryOperator> {

    public UnaryOperatorTokenParser(final OperatorRegistry<UnaryOperator> operatorRegistry) {
        super(operatorRegistry);
    }

    @Override
    public boolean shouldSelect(
            final int ch,
            final @Nullable Operand prevOperand,
            final @Nullable Operator prevOperator
    ) {
        return (prevOperand == null || prevOperator != null) && operatorRegistry.hasMatcher(ch);
    }

    @Override
    protected void doVisit(final TokenVisitor visitor, final UnaryOperator operator) {
        visitor.visitUnaryOperator(operator);
    }
}
