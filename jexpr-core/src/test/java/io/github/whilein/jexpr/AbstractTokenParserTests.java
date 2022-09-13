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

import io.github.whilein.jexpr.token.SelectableTokenParser;
import io.github.whilein.jexpr.token.TokenVisitor;
import io.github.whilein.jexpr.token.TokenVisitors;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author whilein
 */
abstract class AbstractTokenParserTests {

    protected SelectableTokenParser tokenParser;

    protected boolean ignoreCannotBeSelected;

    public final void parse(final @NotNull String value, final @NotNull TokenVisitor tokenVisitor) throws SyntaxException {
        if (!ignoreCannotBeSelected && !tokenParser.shouldSelect(value.charAt(0), null, null)) {
            throw new IllegalStateException(this + " cannot be selected at " + value.charAt(0));
        }

        for (int i = 0, j = value.length(); i < j; i++) {
            val ch = value.charAt(i);

            if (!tokenParser.shouldStaySelected(ch)) {
                throw new IllegalStateException("Unexpected end");
            }

            tokenParser.update(ch);
        }

        tokenParser.doFinal(tokenVisitor);
    }

    public final String parseOperator(final @NotNull String value) throws SyntaxException {
        val result = new AtomicReference<String>();

        parse(value, TokenVisitors.interceptOperator(operator -> result.set(operator.toString())));

        return result.get();
    }

    public final Object parse(final @NotNull String value) throws SyntaxException {
        val result = new AtomicReference<>();

        parse(value, TokenVisitors.interceptOperand(operand -> result.set(operand.getValue())));

        return result.get();
    }


}
