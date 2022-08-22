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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author whilein
 */
final class StringTokenParserTests extends AbstractTokenParserTests {


    @BeforeEach
    void setup() {
        tokenParser = new StringTokenParser(tokenVisitor, new ByteArrayOutput());
    }

    @Test
    void parseDoubleQuotedString() {
        tokenParser.submit("\"Hello world!\"");
        assertTokens("Hello world!");
    }

    @Test
    void parseSingleQuotedString() {
        tokenParser.submit("'Hello world!'");
        assertTokens("Hello world!");
    }

    @Test
    void parseEscapedDoubleQuoteString() {
        tokenParser.submit("\"\\\"\"");
        assertTokens("\"");
    }

    @Test
    void parseEscapedSingleQuoteString() {
        tokenParser.submit("'\\''");
        assertTokens("'");
    }

    @Test
    void parseTabString() {
        tokenParser.submit("\"\\t\"");
        assertTokens("\t");
    }

    @Test
    void testUnicodeHex() {
        tokenParser.submit("\"\\u00a7\"");
        assertTokens("§");
    }

    @Test
    void testIllegalEscape() {
        try {
            tokenParser.submit("\"\\a\"");

            fail();
        } catch (final SyntaxException e) {
            assertEquals("Unexpected character 'a' {quoteCharacter=\", state=2}",
                    e.getMessage());
        }
    }

    @Test
    void testUnicodeHexError() {
        try {
            tokenParser.submit("\"\\u00a\"");

            fail();
        } catch (final SyntaxException e) {
            assertEquals("Unexpected end of unicode escape notation {quoteCharacter=\", state=3, unicode=000a, unicodeMin=4, unicodeMax=4, unicodeSize=3, unicodeRadix=16}",
                    e.getMessage());
        }
    }

    @Test
    void testUnicodeOctal() {
        tokenParser.submit("\"\\0a\"");
        assertTokens("\0a");
    }

    @Test
    void testUnicodeOctal_1to3OctalOctal() {
        tokenParser.submit("\"\\012\"");
        assertTokens("\012");
    }

    @Test
    void testUnicodeOctal_1to3Octal() {
        tokenParser.submit("\"\\01\"");
        assertTokens("\01");
    }

    @Test
    void testUnicodeOctal_1to3() {
        tokenParser.submit("\"\\0\"");
        assertTokens("\0");
    }

    @Test
    void testUnicodeOctal_Octal() {
        tokenParser.submit("\"\\7\"");
        assertTokens("\7");
    }

    @Test
    void testUnicodeOctal_OctalOctal() {
        tokenParser.submit("\"\\777\"");
        assertTokens("\777");
    }

    @Test
    void testUnicodeEmoji() {
        tokenParser.submit("\"\\uD83E\\uDD21\"");
        assertTokens("\uD83E\uDD21");
    }

    @Test
    void parseNewLineString() {
        tokenParser.submit("\"\\n\"");
        assertTokens("\n");
    }

    @Test
    void parseCarriageReturnString() {
        tokenParser.submit("\"\\r\"");
        assertTokens("\r");
    }

}