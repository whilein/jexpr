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

import io.github.whilein.jexpr.operand.defined.OperandBoolean;
import io.github.whilein.jexpr.operand.defined.OperandDouble;
import io.github.whilein.jexpr.operator.OperatorException;
import io.github.whilein.jexpr.token.SequenceTokenParser;
import io.github.whilein.jexpr.token.TokenParser;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author whilein
 */
final class ExpressionParserTests {

    ExpressionParser expressionParser;

    TokenParser tokenParser;

    @BeforeEach
    void setup() {
        expressionParser = DefaultExpressionParser.create();
        tokenParser = SequenceTokenParser.createDefault(expressionParser);
    }

    protected int update(final String text) {
        return tokenParser.submit(text);
    }

    @Test
    void testStringNotEquals() {
        assertEquals(0, update("'123' != '123'"));

        val result = expressionParser.getResult();

        //noinspection EqualsWithItself
        assertEquals(OperandBoolean.valueOf(!"123".equals("123")), result);
    }


    @Test
    void testStringEquals() {
        assertEquals(0, update("'123' == '123'"));

        val result = expressionParser.getResult();
        //noinspection EqualsWithItself
        assertEquals(OperandBoolean.valueOf("123".equals("123")), result);
    }

    @Test
    void testLogicalDefinedExpression() {
        assertEquals(0, update("(3 > 8) || (6 > 8) && !false"));

        val result = expressionParser.getResult();

        //noinspection ConstantConditions,PointlessBooleanExpression
        assertEquals(OperandBoolean.valueOf((3 > 8) || (6 > 8) && !false), result);
    }

    @Test
    void testInapplicableOperators() {
        assertThrows(OperatorException.class, () -> update("1 - 'text'"));
    }

    @Test
    void testUndefinedEvaluation() {
        val x = 3;
        val y = 2;
        val z = 1;

        val map = new VariablesMap()
                .put("x", x)
                .put("y", y)
                .put("z", z);

        assertEquals(0, update("(x > y) || z != 1"));

        val result = expressionParser.getResult();
        val solvedResult = result.solve(map);

        //noinspection ConstantConditions
        assertEquals((x > y) || z != 1, solvedResult.getValue());

        // laziness check
        assertEquals(Arrays.asList("x", "y"), map.getHistory());
    }

    @Test
    void testDefinedEvaluation() {
        assertEquals(0, update("(0xFaL ^ -~0b10) + 009_9.9_9d)"));

        val result = expressionParser.getResult();
        assertEquals(OperandDouble.valueOf((0xFaL ^ -~0b10) + 009_9.9_9d), result);
    }


}