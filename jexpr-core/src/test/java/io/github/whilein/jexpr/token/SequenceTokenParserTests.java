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

import io.github.whilein.jexpr.operator.type.OperatorBitwiseRightShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseXor;
import io.github.whilein.jexpr.operator.type.OperatorDivide;
import io.github.whilein.jexpr.operator.type.OperatorMinus;
import io.github.whilein.jexpr.operator.type.OperatorPlus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author whilein
 */
class SequenceTokenParserTests extends AbstractTokenParserTests {

    @BeforeEach
    void setup() {
        tokenParser = SequenceTokenParser.createDefault(tokenVisitor);
    }

    @Test
    void testSequence() {
        update("1 + 2");

        assertTokens(
                '(',
                1, OperatorPlus.class, 2,
                ')'
        );
    }

    @Test
    void testComplexSequence() {
        update("0xFaL / .5 + '23\\n' ^ (var >> 2 - (-1))");

        assertTokens(
                '(',
                0xFaL, OperatorDivide.class, .5, OperatorPlus.class, "23\n", OperatorBitwiseXor.class,
                '(', "var", OperatorBitwiseRightShift.class, 2, OperatorMinus.class, '(', OperatorMinus.class, 1, ')', ')',
                ')'
        );
    }

}