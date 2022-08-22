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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
final class NumberTokenParserTests extends AbstractTokenParserTests {

    @BeforeEach
    void setup() {
        tokenParser = new NumberTokenParser(tokenVisitor, new ByteArrayOutput());
    }

    @Test
    void parseZero() {
        assertEquals(0, tokenParser.submit("0"));
        assertTokens(0);
    }

    @Test
    void parseFloat() {
        assertEquals(0, tokenParser.submit("1.1f"));
        assertTokens(1.1f);
    }

    @Test
    void parseInt() {
        assertEquals(0, tokenParser.submit("1"));
        assertTokens(1);
    }

    @Test
    void parseLong() {
        assertEquals(0, tokenParser.submit("1L"));
        assertTokens(1L);
    }

    @Test
    void parseDoubleWithSuffix() {
        assertEquals(0, tokenParser.submit("1.1D"));
        assertTokens(1.1D);
    }

    @Test
    void parseDouble() {
        assertEquals(0, tokenParser.submit("1.1"));
        assertTokens(1.1);
    }

    @Test
    void parseLeadingPointDouble() {
        assertEquals(0, tokenParser.submit(".1"));
        assertTokens(.1);
    }

    @Test
    void parseLeadingPointFloat() {
        assertEquals(0, tokenParser.submit(".1f"));
        assertTokens(.1f);
    }

    @Test
    void parseZeroDouble() {
        assertEquals(0, tokenParser.submit("0D"));
        assertTokens(0d);
    }

    @Test
    void parseZeroFloat() {
        assertEquals(0, tokenParser.submit("0F"));
        assertTokens(0f);
    }

    @Test
    void parseZeroLong() {
        assertEquals(0, tokenParser.submit("0L"));
        assertTokens(0L);
    }

    @Test
    void parseZeroInt() {
        assertEquals(0, tokenParser.submit("0"));
        assertTokens(0);
    }

    @Test
    void parseLeadingZeroDouble() {
        assertEquals(0, tokenParser.submit("0.1"));
        assertTokens(0.1);
    }

    @Test
    void parseLeadingZeroFloat() {
        assertEquals(0, tokenParser.submit("0.1f"));
        assertTokens(0.1f);
    }

    @Test
    void parseLong16() {
        assertEquals(0, tokenParser.submit("0xaFL"));
        assertTokens(0xaFL);
    }

    @Test
    void parseInt16() {
        assertEquals(0, tokenParser.submit("0xFa"));
        assertTokens(0xFa);
    }


    @Test
    void parseFloatOctal() {
        System.out.println(0999f);

        assertEquals(0, tokenParser.submit("0999f"));
        assertTokens(0999f);
    }

    @Test
    @SuppressWarnings("OctalInteger")
    void parseLong8() {
        assertEquals(0, tokenParser.submit("0123L"));
        assertTokens(0123L);
    }

    @Test
    @SuppressWarnings("OctalInteger")
    void parseInt8() {
        assertEquals(0, tokenParser.submit("0321"));
        assertTokens(0321);
    }

    @Test
    void parseLong2() {
        assertEquals(0, tokenParser.submit("0b1010L"));
        assertTokens(0b1010L);
    }

    @Test
    void parseLeadingPointExponent() {
        assertEquals(0, tokenParser.submit(".1e1"));
        assertTokens(.1e1);
    }

    @Test
    void parseLeadingZeroExponent() {
        assertEquals(0, tokenParser.submit("0.1e1"));
        assertTokens(0.1e1);
    }

    @Test
    void parseExponent() {
        assertEquals(0, tokenParser.submit("1.1e1"));
        assertTokens(1.1e1);
    }

    @Test
    void parseNegativeExponent() {
        assertEquals(0, tokenParser.submit("1.1e-1"));
        assertTokens(1.1e-1);
    }

    @Test
    void parseInt2() {
        assertEquals(0, tokenParser.submit("0b0101"));
        assertTokens(0b0101);
    }

}