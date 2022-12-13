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

import io.github.whilein.jexpr.api.exception.SyntaxException;
import io.github.whilein.jexpr.api.token.SelectableTokenParser;
import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operator.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author whilein
 */
public final class NestedOperandParser
        extends AbstractOperandParser
        implements SelectableTokenParser {

    private static final int STATE_LEADING_BRACKET = 0, STATE_CONTENT = 1, STATE_FINAL_BRACKET = 2;

    private int state;

    public NestedOperandParser(final List<SelectableTokenParser> parsers) {
        super(parsers);
    }

    @Override
    protected boolean shouldIgnore(final int ch) {
        final boolean shouldIgnore;

        if (!(shouldIgnore = super.shouldIgnore(ch))) {
            if (ch == ')') {
                state = STATE_FINAL_BRACKET;
                return true;
            }
        }

        return shouldIgnore;
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

        try {
            return super.doFinal();
        } finally {
            state = 0;
        }
    }
}
