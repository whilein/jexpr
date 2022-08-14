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

import io.github.whilein.jexpr.operator.Operator;
import io.github.whilein.jexpr.operator.OperatorRegistry;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseAnd;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseComplement;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseLeftShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseOr;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseRightShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseUnsignedRightShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseXor;
import io.github.whilein.jexpr.operator.type.OperatorDivide;
import io.github.whilein.jexpr.operator.type.OperatorMinus;
import io.github.whilein.jexpr.operator.type.OperatorMultiply;
import io.github.whilein.jexpr.operator.type.OperatorPlus;
import io.github.whilein.jexpr.operator.type.OperatorRemainder;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
class OperatorTokenParserTests extends AbstractTokenParserTests {

    @BeforeEach
    void setup() {
        tokenParser = new OperatorTokenParser(tokenVisitor, OperatorRegistry.createDefault());
    }

    private void testOperator(final String operator, final Class<? extends Operator> type) {
        tokenParser.submit(operator);

        val tokens = tokenVisitor.getTokens();
        assertEquals(1, tokens.size());
        assertEquals(type, tokens.get(0));
    }

    @Test
    void testBitwiseComplement() {
        testOperator("~", OperatorBitwiseComplement.class);
    }

    @Test
    void testBitwiseXor() {
        testOperator("^", OperatorBitwiseXor.class);
    }


    @Test
    void testPlus() {
        testOperator("+", OperatorPlus.class);
    }

    @Test
    void testMinus() {
        testOperator("-", OperatorMinus.class);
    }

    @Test
    void testDivide() {
        testOperator("/", OperatorDivide.class);
    }

    @Test
    void testMultiply() {
        testOperator("*", OperatorMultiply.class);
    }

    @Test
    void testRemainder() {
        testOperator("%", OperatorRemainder.class);
    }

    @Test
    void testBitwiseAnd() {
        testOperator("&", OperatorBitwiseAnd.class);
    }

    @Test
    void testBitwiseOr() {
        testOperator("|", OperatorBitwiseOr.class);
    }

    @Test
    void testBitwiseLeftShift() {
        testOperator("<<", OperatorBitwiseLeftShift.class);
    }

    @Test
    void testBitwiseRightShift() {
        testOperator(">>", OperatorBitwiseRightShift.class);
    }

    @Test
    void testBitwiseUnsignedRightShift() {
        testOperator(">>>", OperatorBitwiseUnsignedRightShift.class);
    }


}