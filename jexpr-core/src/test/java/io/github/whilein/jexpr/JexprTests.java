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

import io.github.whilein.jexpr.api.Jexpr;
import io.github.whilein.jexpr.api.exception.OperatorException;
import io.github.whilein.jexpr.api.exception.SyntaxException;
import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.token.operand.Operands;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author whilein
 */
final class JexprTests {

    static Jexpr jexpr;

    @BeforeAll
    static void setup() {
        jexpr = DefaultJexpr.create();
    }

    private static @NotNull Operand parse(final String text) {
        return jexpr.parse(text);
    }

    @Test
    void testAccess() {
        val operand = parse("-user.name");

        assertEquals("-user . name", operand.toString());
    }

    @Test
    void testStringNotEquals() {
        //noinspection EqualsWithItself
        assertEquals(!"123".equals("123"), parse("'123' != '123'").getValue());
    }

    @Test
    void testNestedParenthesesOneLeftOperand() {
        assertEquals(true, parse("('123') == '123'").getValue());
    }

    @Test
    void testNestedParenthesesOneRightOperand() {
        assertEquals(true, parse("'123' == ('123')").getValue());
    }

    @Test
    void testNestedParenthesesBothOperands() {
        assertEquals(true, parse("('123') == ('123')").getValue());
    }

    @Test
    void testNestedParentheses() {
        assertEquals(true, parse("('123' == '123')").getValue());
    }

    @Test
    void testNull() {
        val operand = parse("x != null");
        val result = operand.solve(k -> Operands.constantObject(null));

        assertEquals(false, result.getValue());
    }

    @Test
    void testStringEquals() {
        //noinspection EqualsWithItself
        assertEquals("123".equals("123"), parse("'123' == '123'").getValue());
    }

    @Test
    void testLogicalConstantExpression() {
        //noinspection ConstantConditions,PointlessBooleanExpression
        assertEquals((3 > 8) || (6 > 8) && !false, parse("(3 > 8) || (6 > 8) && !false").getValue());
    }

    @Test
    void testResetAfterFail() {
        assertThrows(SyntaxException.class, () -> parse("(1?2)"));
        assertEquals(2, parse("1 + 1").getValue());
    }

    @Test
    void testInapplicableOperators() {
        assertThrows(OperatorException.class, () -> parse("1 - 'text'"));
    }

    @Test
    void testVariableEvaluation() {
        val x = 3;
        val y = 2;
        val z = 1;

        val map = new VariablesMap()
                .put("x", x)
                .put("y", y)
                .put("z", z);

        val result = parse("(x > y) || z != 1");

        val solvedResult = result.solve(map);

        //noinspection ConstantConditions
        assertEquals((x > y) || z != 1, solvedResult.getValue());

        // laziness check
        assertEquals(Arrays.asList("x", "y"), map.getHistory());
    }

    @Test
    void testConstantEvaluation() {
        assertEquals((0xFaL ^ -~0b10) + 009_9.9_9d, parse("(0xFaL ^ -~0b10) + 009_9.9_9d").getValue());
    }

}