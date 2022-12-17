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

import io.github.whilein.jexpr.DefaultJexpr;
import io.github.whilein.jexpr.api.Jexpr;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
final class OperandInterfaceImplementorTests {

    static Jexpr jexpr;

    static OperandInterfaceImplementorFactory operandInterfaceImplementorFactory;

    @BeforeAll
    static void setup() {
        jexpr = DefaultJexpr.create();
        operandInterfaceImplementorFactory = SimpleOperandInterfaceImplementorFactory.create();
    }

    @Test
    void testBridge() {
        val operand = jexpr.parse("a + b");

        @SuppressWarnings("unchecked")
        val operator = (BinaryOperator<Integer>) operandInterfaceImplementorFactory.create(BinaryOperator.class)
                .parameter(0, "a", Integer.class)
                .parameter(1, "b", Integer.class)
                .returnType(Integer.class)
                .implement(operand);

        assertEquals(30, operator.apply(10, 20));
        assertEquals(-30, operator.apply(10, -40));
    }

    @Test
    void testPrimitives() {
        val operand = jexpr.parse("a + b");

        val operator = operandInterfaceImplementorFactory.create(IntBinaryOperator.class)
                .name(0, "a")
                .name(1, "b")
                .implement(operand);

        assertEquals(30, operator.applyAsInt(10, 20));
        assertEquals(-30, operator.applyAsInt(10, -40));
    }

}
