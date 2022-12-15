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

package io.github.whilein.jexpr.operand;

import io.github.whilein.jexpr.AbstractJexpr;
import io.github.whilein.jexpr.DefaultJexpr;
import io.github.whilein.jexpr.api.Jexpr;
import io.github.whilein.jexpr.api.token.operand.Operand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
class OperandTests {

    Jexpr jexpr;

    @BeforeEach
    void setup() {
        jexpr = DefaultJexpr.create();
    }

    @Test
    void testToStringRedundantParentheses() {
        Operand expression;

        expression = jexpr.parse("(a + b) - (a * b)");
        assertEquals("(a + b) - a * b", expression.toString());

        expression = jexpr.parse("(a * (b - c))");
        assertEquals("a * (b - c)", expression.toString());

        expression = jexpr.parse("(a + b) == (c + d)");
        assertEquals("a + b == c + d", expression.toString());
    }

    @Test
    void testToStringEscape() {
        Operand expression;

        expression = jexpr.parse("'Some text\\r\\nNew line 1\\tTab \"Quotes\"' == x");
        assertEquals("\"Some text\\r\\nNew line 1\\tTab \\\"Quotes\\\"\" == x", expression.toString());

        expression = jexpr.parse("'\\u0FFF' == x");
        assertEquals("\"\\u0FFF\" == x", expression.toString());
    }

    @Test
    void testToStringUnary() {
        Operand expression;

        expression = jexpr.parse("-a - -b");
        assertEquals("-a - -b", expression.toString());

        expression = jexpr.parse("-a - -(b + c)");
        assertEquals("-a - -(b + c)", expression.toString());
    }

}