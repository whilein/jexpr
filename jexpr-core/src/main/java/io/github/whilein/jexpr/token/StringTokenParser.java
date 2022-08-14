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

import io.github.whilein.jexpr.SyntaxException;
import io.github.whilein.jexpr.io.ByteArrayOutput;
import io.github.whilein.jexpr.operand.OperandString;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;

import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class StringTokenParser extends AbstractTokenParser {

    @NonFinal
    int quoteCharacter = 0;

    @NonFinal
    int state = STATE_LEADING_QUOTE;

    private static final int
            STATE_LEADING_QUOTE = 0,
            STATE_CONTENT = 1,
            STATE_ESCAPE = 2,
            STATE_UNICODE = 3,
            STATE_FINISH_QUOTE = 4;

    @NonFinal
    int unicode;

    @NonFinal
    int unicodeSize;

    TokenVisitor tokenVisitor;

    ByteArrayOutput buffer;

    @Override
    protected void writeSyntaxReport(final Map<String, Object> map) {
        map.put("quoteCharacter", (char) quoteCharacter);
        map.put("state", state);
    }

    @Override
    public boolean shouldStayActive(final int ch) {
        return state != STATE_FINISH_QUOTE;
    }

    @Override
    public boolean shouldActivate(final int ch) {
        return ch == '\'' || ch == '\"';
    }


    @Override
    public void update(final int ch) throws SyntaxException {
        if (state == STATE_LEADING_QUOTE) {
            quoteCharacter = ch;
            state = STATE_CONTENT;
            return;
        }

        if (state == STATE_UNICODE) {
            unicode *= 16;
            unicode += Character.digit(ch, 16);

            if (++unicodeSize == 4) {
                state = STATE_CONTENT;

            }

            return;
        }

        if (state == STATE_ESCAPE) {
            switch (ch) {
                case 'u':
                    state = STATE_UNICODE;
                    unicode = 0;
                    unicodeSize = 0;
                    return;
                case '\\':
                case '\'':
                case '"':
                    buffer.put(ch);
                    break;
                case 'n':
                    buffer.put('\n');
                    break;
                case 'r':
                    buffer.put('\r');
                    break;
                case 't':
                    buffer.put('\t');
                    break;
                case 'f':
                    buffer.put('\f');
                    break;
                case 'b':
                    buffer.put('\b');
                    break;
                default:
                    throw unexpected(ch);
            }

            state = STATE_CONTENT;
            return;
        }

        if (ch == '\\') {
            state = STATE_ESCAPE;
            return;
        }

        if (ch == quoteCharacter) {
            state = STATE_FINISH_QUOTE;
            return;
        }

        buffer.put(ch);
    }

    @Override
    public void doFinal() throws SyntaxException {
        try {
            val text = buffer.getString();
            tokenVisitor.visitOperand(OperandString.valueOf(text));
        } finally {
            buffer.reset();
            state = STATE_LEADING_QUOTE;
            quoteCharacter = 0;
        }
    }
}
