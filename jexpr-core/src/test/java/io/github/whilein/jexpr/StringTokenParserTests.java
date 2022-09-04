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

import io.github.whilein.jexpr.io.ByteArrayOutput;
import io.github.whilein.jexpr.token.StringTokenParser;
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
        tokenParser = new StringTokenParser(new ByteArrayOutput());
    }

    @Test
    void parseDoubleQuotedString() {
        assertEquals("Hello world!", parse("\"Hello world!\""));
    }

    @Test
    void parseSingleQuotedString() {
        assertEquals("Hello world!", parse("'Hello world!'"));
    }

    @Test
    void parseEscapedDoubleQuoteString() {
        assertEquals("\"", parse("\"\\\"\""));
    }

    @Test
    void parseEscapedSingleQuoteString() {
        assertEquals("'", parse("'\\''"));
    }

    @Test
    void parseTabString() {
        assertEquals("\t", parse("\"\\t\""));
    }

    @Test
    void testUnicodeHex() {
        assertEquals("§", parse("\"\\u00a7\""));
    }

    @Test
    void testIllegalEscape() {
        try {
            parse("\"\\a\"");
            fail();
        } catch (final SyntaxException e) {
            assertEquals("Unexpected character 'a' {quoteCharacter=\", state=2}",
                    e.getMessage());
        }
    }

    @Test
    void testUnicodeHexError() {
        try {
            parse("\"\\u00a\"");
            fail();
        } catch (final SyntaxException e) {
            assertEquals("Unexpected end of unicode escape notation {quoteCharacter=\", state=3, unicode=000a, unicodeMin=4, unicodeMax=4, unicodeSize=3, unicodeRadix=16}",
                    e.getMessage());
        }
    }

    @Test
    void testUnicodeOctal() {
        assertEquals("\0a", parse("\"\\0a\""));
    }

    @Test
    void testUnicodeOctal_1to3OctalOctal() {
        assertEquals("\012", parse("\"\\012\""));
    }

    @Test
    void testUnicodeOctal_1to3Octal() {
        assertEquals("\01", parse("\"\\01\""));
    }

    @Test
    void testUnicodeOctal_1to3() {
        assertEquals("\0", parse("\"\\0\""));
    }

    @Test
    void testUnicodeOctal_Octal() {
        assertEquals("\7", parse("\"\\7\""));
    }

    @Test
    void testUnicodeOctal_OctalOctal() {
        assertEquals("\777", parse("\"\\777\""));
    }

    @Test
    void testUnicodeEmoji() {
        assertEquals("\uD83E\uDD21", parse("\"\\uD83E\\uDD21\""));
    }

    @Test
    void parseNewLineString() {
        assertEquals("\n", parse("\"\\n\""));
    }

    @Test
    void parseCarriageReturnString() {
        assertEquals("\r", parse("\"\\r\""));
    }

}