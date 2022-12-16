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

import io.github.whilein.jexpr.api.exception.SyntaxException;
import io.github.whilein.jexpr.api.token.TokenVisitor;
import io.github.whilein.jexpr.io.ByteArrayOutput;
import io.github.whilein.jexpr.token.operand.Operands;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class StringTokenParser extends AbstractSelectableTokenParser {

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
    char highSurrogate;

    @NonFinal
    int unicodeSize;

    @NonFinal
    int unicodeMaxSize, unicodeMinSize;

    @NonFinal
    int unicodeRadix;

    ByteArrayOutput buffer;

    @Override
    protected void writeSyntaxReport(final Map<String, Object> map) {
        map.put("quoteCharacter", (char) quoteCharacter);
        map.put("state", state);

        if (unicodeRadix != 0) {
            map.put("unicode", String.format("%4x", unicode).replace(' ', '0'));
            map.put("unicodeMin", unicodeMinSize);
            map.put("unicodeMax", unicodeMaxSize);
            map.put("unicodeSize", unicodeSize);
            map.put("unicodeRadix", unicodeRadix);
        }
    }

    @Override
    public boolean shouldStaySelected(final int ch) {
        return state != STATE_FINISH_QUOTE;
    }

    @Override
    public boolean shouldSelect(final int ch) {
        return ch == '\'' || ch == '\"';
    }


    private void completeUnicode() {
        completeUnicode(0);
    }

    private void completeUnicode(final int ch) {
        if (unicodeMinSize > unicodeSize) {
            throw invalidSyntax(ch == 0
                    ? "Unexpected end of unicode escape notation"
                    : "Unexpected character: " + (char) ch);
        }

        state = STATE_CONTENT;

        val unicodeCharacter = (char) unicode;

        unicode = 0;
        unicodeRadix = 0;

        if (Character.isHighSurrogate(unicodeCharacter)) {
            highSurrogate = unicodeCharacter;
            return;
        } else if (Character.isLowSurrogate(unicodeCharacter)) {
            if (Character.isSurrogatePair(highSurrogate, unicodeCharacter)) {
                val codePoint = Character.toCodePoint(highSurrogate, unicodeCharacter);
                highSurrogate = 0;

                put(new String(Character.toChars(codePoint)).getBytes(StandardCharsets.UTF_8));
                return;
            }
        }

        put(getBytes(unicodeCharacter));

        if (ch != 0) {
            update(ch);
        }
    }

    @Override
    public void update(final int ch) throws SyntaxException {
        switch (state) {
            case STATE_LEADING_QUOTE: {
                quoteCharacter = ch;
                state = STATE_CONTENT;
                return;
            }
            case STATE_ESCAPE: {
                switch (ch) {
                    case 'u':
                        startUnicode(16, 4, 4);
                        return;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                        startUnicode(8, 3, 1);
                        addUnicode(ch);
                        return;
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                        startUnicode(8, 2, 1);
                        addUnicode(ch);
                        return;
                    case '\\':
                    case '\'':
                    case '"':
                        put(ch);
                        break;
                    case 'n':
                        put('\n');
                        break;
                    case 'r':
                        put('\r');
                        break;
                    case 't':
                        put('\t');
                        break;
                    case 'f':
                        put('\f');
                        break;
                    case 'b':
                        put('\b');
                        break;
                    default:
                        throw unexpected(ch);
                }

                state = STATE_CONTENT;
                return;
            }
        }

        if (ch == '\\') {
            state = STATE_ESCAPE;
            return;
        }

        if (ch == quoteCharacter) {
            if (state == STATE_UNICODE) {
                completeUnicode();
            }

            state = STATE_FINISH_QUOTE;

            return;
        }

        if (state == STATE_UNICODE) {
            addUnicode(ch);

            return;
        }

        put(ch);
    }

    private void addUnicode(final int ch) {
        val digit = Character.digit((char) ch, unicodeRadix);

        if (digit < 0) {
            completeUnicode(ch);
            return;
        }

        unicode = (unicode * unicodeRadix) + digit;

        if (++unicodeSize == unicodeMaxSize) {
            completeUnicode();
        }
    }

    private void startUnicode(final int radix, final int max, final int min) {
        this.state = STATE_UNICODE;
        this.unicode = 0;
        this.unicodeRadix = radix;
        this.unicodeMaxSize = max;
        this.unicodeMinSize = min;
        this.unicodeSize = 0;
    }

    private void put(final byte[] bytes) {
        flushHighSurrogate();

        for (val b : bytes) {
            buffer.put(b);
        }
    }


    private static byte[] getBytes(final char ch) {
        return Character.toString(ch).getBytes(StandardCharsets.UTF_8);
    }

    private void flushHighSurrogate() {
        if (highSurrogate != 0) {
            for (val b : getBytes(highSurrogate)) {
                buffer.put(b);
            }

            highSurrogate = 0;
        }
    }

    private void put(final int ch) {
        flushHighSurrogate();
        buffer.put(ch);
    }

    @Override
    public void doFinal(final @NotNull TokenVisitor tokenVisitor) throws SyntaxException {
        try {
            tokenVisitor.visitOperand(Operands.constantString(buffer.getString()));
        } finally {
            buffer.reset();
            state = STATE_LEADING_QUOTE;
            quoteCharacter = 0;
        }
    }
}
