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

import io.github.whilein.jexpr.SimpleExpressionParser;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
class OperandTests {

    @Test
    void testToStringRedundantParentheses() {
        val parser = SimpleExpressionParser.createDefault();

        Operand expression;

        expression = parser.parse("(a + b) - (a * b)");
        assertEquals("(a + b) - a * b", expression.toString());

        expression = parser.parse("(a * (b - c))");
        assertEquals("a * (b - c)", expression.toString());

        expression = parser.parse("(a + b) == (c + d)");
        assertEquals("a + b == c + d", expression.toString());
    }

    @Test
    void testToStringEscape() {
        val parser = SimpleExpressionParser.createDefault();

        Operand expression;

        expression = parser.parse("'Some text\\r\\nNew line 1\\tTab \"Quotes\"' == x");
        assertEquals("\"Some text\\r\\nNew line 1\\tTab \\\"Quotes\\\"\" == x", expression.toString());

        expression = parser.parse("'\\u0FFF' == x");
        assertEquals("\"\\u0FFF\" == x", expression.toString());
    }

    @Test
    void testToStringUnary() {
        val parser = SimpleExpressionParser.createDefault();

        Operand expression;

        expression = parser.parse("-a - -b");
        assertEquals("-a - -b", expression.toString());

        expression = parser.parse("-a - -(b + c)");
        assertEquals("-a - -(b + c)", expression.toString());
    }

}