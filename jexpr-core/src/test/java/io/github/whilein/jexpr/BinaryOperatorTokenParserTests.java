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

import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.token.BinaryOperatorTokenParser;
import io.github.whilein.jexpr.token.operator.DefaultBinaryOperatorRegistry;
import io.github.whilein.jexpr.token.operator.SimpleOperatorRegistry;
import io.github.whilein.jexpr.token.operator.type.*;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
final class BinaryOperatorTokenParserTests extends AbstractTokenParserTests {

    @BeforeEach
    void setup() {
        val registry = new DefaultBinaryOperatorRegistry();

        tokenParser = new BinaryOperatorTokenParser(registry);
        ignoreCannotBeSelected = true;
    }

    private void testOperator(final String operator, final String type) {
        assertEquals(type, parseOperator(operator));
    }

//    @Test
//    void testBitwiseComplement() {
//        testOperator("~", "BITWISE_COMPLEMENT");
//    }

    @Test
    void testAnd() {
        testOperator("&&", "AND");
    }

    @Test
    void testOr() {
        testOperator("||", "OR");
    }

//    @Test
//    void testNegate() {
//        testOperator("!", "NEGATE");
//    }

    @Test
    void testLess() {
        testOperator("<=", "LESS");
    }

    @Test
    void testGreater() {
        testOperator(">=", "GREATER");
    }

    @Test
    void testStrictLess() {
        testOperator("<", "STRICT_LESS");
    }

    @Test
    void testStrictGreater() {
        testOperator(">", "STRICT_GREATER");
    }

    @Test
    void testEquals() {
        testOperator("==", "EQUALS");
    }

    @Test
    void testNotEquals() {
        testOperator("!=", "NOT_EQUALS");
    }

    @Test
    void testBitwiseXor() {
        testOperator("^", "BITWISE_XOR");
    }

    @Test
    void testPlus() {
        testOperator("+", "PLUS");
    }

    @Test
    void testMinus() {
        testOperator("-", "MINUS");
    }
    @Test
    void testDivide() {
        testOperator("/", "DIVIDE");
    }

    @Test
    void testMultiply() {
        testOperator("*", "MULTIPLY");
    }

    @Test
    void testRemainder() {
        testOperator("%", "REMAINDER");
    }

    @Test
    void testBitwiseAnd() {
        testOperator("&", "BITWISE_AND");
    }

    @Test
    void testBitwiseOr() {
        testOperator("|", "BITWISE_OR");
    }

    @Test
    void testBitwiseLeftShift() {
        testOperator("<<", "BITWISE_LEFT_SHIFT");
    }

    @Test
    void testBitwiseRightShift() {
        testOperator(">>", "BITWISE_RIGHT_SHIFT");
    }

    @Test
    void testBitwiseUnsignedRightShift() {
        testOperator(">>>", "BITWISE_UNSIGNED_RIGHT_SHIFT");
    }

}