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

package io.github.whilein.jexpr.compiler;

import io.github.whilein.jexpr.SimpleJexpr;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
final class ExpressionImplementationCompilerTests {
    @Test
    void testBridge() {
        val parser = new SimpleJexpr();
        val expression = parser.parse("a + b");

        @SuppressWarnings("unchecked")
        val operator = (BinaryOperator<Integer>) ExpressionImplementationCompiler.create(expression, BinaryOperator.class)
                .parameter(0, "a", Integer.class)
                .parameter(1, "b", Integer.class)
                .returnType(Integer.class)
                .compile();

        assertEquals(30, operator.apply(10, 20));
        assertEquals(-30, operator.apply(10, -40));
    }

    @Test
    void testPrimitives() {
        val parser = new SimpleJexpr();
        val expression = parser.parse("a + b");

        val operator = ExpressionImplementationCompiler.create(expression, IntBinaryOperator.class)
                .name(0, "a")
                .name(1, "b")
                .compile();

        assertEquals(30, operator.applyAsInt(10, 20));
        assertEquals(-30, operator.applyAsInt(10, -40));
    }

}
