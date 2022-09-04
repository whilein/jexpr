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
import io.github.whilein.jexpr.token.NumberTokenParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
final class NumberTokenParserTests extends AbstractTokenParserTests {

    @BeforeEach
    void setup() {
        tokenParser = new NumberTokenParser(new ByteArrayOutput());
    }

    @Test
    void parseZero() {
        assertEquals(0, parse("0"));
    }

    @Test
    void parseFloat() {
        assertEquals(1.1f, parse("1.1f"));
    }

    @Test
    void parseInt() {
        assertEquals(1, parse("1"));
    }

    @Test
    void parseLong() {
        assertEquals(1L, parse("1L"));
    }

    @Test
    void parseDoubleWithSuffix() {
        assertEquals(1.1D, parse("1.1D"));
    }

    @Test
    void parseDouble() {
        assertEquals(1.1, parse("1.1"));
    }

    @Test
    void parseLeadingPointDouble() {
        assertEquals(.1, parse(".1"));
    }

    @Test
    void parseLeadingPointFloat() {
        assertEquals(.1f, parse(".1f"));
    }

    @Test
    void parseZeroDouble() {
        assertEquals(0d, parse("0D"));
    }

    @Test
    void parseZeroFloat() {
        assertEquals(0f, parse("0F"));
    }

    @Test
    void parseZeroLong() {
        assertEquals(0L, parse("0L"));
    }

    @Test
    void parseZeroInt() {
        assertEquals(0, parse("0"));
    }

    @Test
    void parseLeadingZeroDouble() {
        assertEquals(0.1, parse("0.1"));
    }

    @Test
    void parseLeadingZeroFloat() {
        assertEquals(0.1f, parse("0.1f"));
    }

    @Test
    void parseLong16() {
        assertEquals(0xaFL, parse("0xaFL"));
    }

    @Test
    void parseInt16() {
        assertEquals(0xFa, parse("0xFa"));
    }


    @Test
    void parseFloatOctal() {
        System.out.println(0999f);

        assertEquals(0999f, parse("0999f"));
    }

    @Test
    @SuppressWarnings("OctalInteger")
    void parseLong8() {
        assertEquals(0123L, parse("0123L"));
    }

    @Test
    @SuppressWarnings("OctalInteger")
    void parseInt8() {
        assertEquals(0321, parse("0321"));
    }

    @Test
    void parseLong2() {
        assertEquals(0b1010L, parse("0b1010L"));
    }

    @Test
    void parseLeadingPointExponent() {
        assertEquals(.1e1, parse(".1e1"));
    }

    @Test
    void parseLeadingZeroExponent() {
        assertEquals(0.1e1, parse("0.1e1"));
    }

    @Test
    void parseExponent() {
        assertEquals(1.1e1, parse("1.1e1"));
    }

    @Test
    void parseNegativeExponent() {
        assertEquals(1.1e-1, parse("1.1e-1"));
    }

    @Test
    void parseInt2() {
        assertEquals(0b0101, parse("0b0101"));
    }

}