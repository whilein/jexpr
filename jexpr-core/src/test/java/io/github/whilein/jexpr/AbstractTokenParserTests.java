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
import io.github.whilein.jexpr.token.Token;
import io.github.whilein.jexpr.token.TokenParser;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
abstract class AbstractTokenParserTests {

    protected TokenParser tokenParser;

    public final Token parseAsToken(final @NotNull String value) throws SyntaxException {
        if (!tokenParser.shouldActivate(value.charAt(0))) {
            throw new IllegalStateException(this + " cannot be activated at " + value.charAt(0));
        }

        for (int i = 0, j = value.length(); i < j; i++) {
            val ch = value.charAt(i);

            if (!tokenParser.shouldStayActive(ch)) {
                throw new IllegalStateException("Unexpected end");
            }

            tokenParser.update(ch);
        }

        return tokenParser.doFinal();
    }

    public final Object parse(final @NotNull String value) throws SyntaxException {
        val token = parseAsToken(value);

        return token instanceof Operand
                ? ((Operand) token).getValue()
                : token.toString();
    }


}
