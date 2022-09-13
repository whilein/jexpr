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

package io.github.whilein.jexpr;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operator.Operator;
import io.github.whilein.jexpr.token.SelectableTokenParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author whilein
 */
public final class NestedExpressionStreamParser
        extends AbstractExpressionStreamParser
        implements SelectableTokenParser {

    public NestedExpressionStreamParser(final List<SelectableTokenParser> parsers) {
        super(parsers);

        this.state = STATE_LEADING_BRACKET;
    }

    @Override
    public void update(final int ch) throws SyntaxException {
        if (state == STATE_LEADING_BRACKET) {
            state = STATE_CONTENT;

            // ignoring leading bracket
            return;
        }

        super.update(ch);
    }

    @Override
    public boolean shouldStaySelected(final int ch) {
        return state != STATE_FINAL_BRACKET;
    }

    @Override
    public boolean shouldSelect(final int ch,
                                final @Nullable Operand prevOperand,
                                final @Nullable Operator prevOperator) {
        return ch == '(';
    }

    @Override
    public @NotNull Operand doFinal() {
        if (state != STATE_FINAL_BRACKET) {
            throw invalidSyntax("Unexpected EOF");
        }

        return super.doFinal();
    }
}
