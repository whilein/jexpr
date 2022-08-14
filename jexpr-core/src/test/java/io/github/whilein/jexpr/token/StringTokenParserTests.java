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

import io.github.whilein.jexpr.io.ByteArrayOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author whilein
 */
class StringTokenParserTests extends AbstractTokenParserTests {


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

//    @Test
//    void testUnicode() {
//        tokenParser.submit("\"\\u00a7\"");
//        assertTokens("§");
//    }

//    @Test
//    void testUnicodeEmoji() {
//        System.out.println(Arrays.toString("\uD83E\uDD21".getBytes()));
//
//        tokenParser.submit("\"\\uD83E\\uDD21\"");
//        assertTokens("\uD83E\uDD21");
//    }

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