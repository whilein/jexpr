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

package io.github.whilein.jexpr.operator;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.constant.OperandBoolean;
import io.github.whilein.jexpr.operand.constant.OperandDouble;
import io.github.whilein.jexpr.operand.constant.OperandFloat;
import io.github.whilein.jexpr.operand.constant.OperandInteger;
import io.github.whilein.jexpr.operand.constant.OperandLong;
import io.github.whilein.jexpr.operand.constant.OperandString;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseAnd;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseOr;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseXor;
import io.github.whilein.jexpr.operator.type.OperatorDivide;
import io.github.whilein.jexpr.operator.type.OperatorEquals;
import io.github.whilein.jexpr.operator.type.OperatorMinus;
import io.github.whilein.jexpr.operator.type.OperatorMultiply;
import io.github.whilein.jexpr.operator.type.OperatorNegate;
import io.github.whilein.jexpr.operator.type.OperatorNotEquals;
import io.github.whilein.jexpr.operator.type.OperatorPlus;
import io.github.whilein.jexpr.operator.type.OperatorRemainder;
import io.github.whilein.jexpr.operator.type.OperatorUnaryMinus;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
final class OperatorTests {

    private static final Map<Class<?>, Function<Object, Operand>> OBJECT_TO_OPERAND_MAP = new HashMap<>();

    static {
        put(String.class, OperandString::valueOf);
        put(Integer.class, OperandInteger::valueOf);
        put(Long.class, OperandLong::valueOf);
        put(Float.class, OperandFloat::valueOf);
        put(Double.class, OperandDouble::valueOf);
        put(Boolean.class, OperandBoolean::valueOf);
    }

    @SuppressWarnings("unchecked")
    private static <T> void put(final Class<T> type, final Function<T, Operand> factory) {
        OBJECT_TO_OPERAND_MAP.put(type, (Function<Object, Operand>) factory);
    }

    private static Operand map(final Object o) {
        return OBJECT_TO_OPERAND_MAP.get(o.getClass()).apply(o);
    }

    void testOperator(final BinaryOperator operator, final Object expect, final Object left, final Object right) {
        assertEquals(map(expect), map(left).apply(map(right), operator));
    }

    void testOperator(final UnaryOperator operator, final Object expect, final Object value) {
        assertEquals(map(expect), map(value).apply(operator));
    }

    @Test
    void testPlus() {
        val operator = new OperatorPlus();
        testOperator(operator, 3, 1, 2);
        testOperator(operator, 3L, 1, 2L);
        testOperator(operator, 3.5, 1, 2.5);
        testOperator(operator, 3.5F, 1, 2.5F);
        testOperator(operator, 3, 2, 1);
        testOperator(operator, 3L, 2L, 1);
        testOperator(operator, 3.5, 2.5, 1);
        testOperator(operator, 3.5F, 2.5F, 1);
    }

    @Test
    void testMinus() {
        val operator = new OperatorMinus();
        testOperator(operator, -1, 1, 2);
        testOperator(operator, -1L, 1, 2L);
        testOperator(operator, -1.5, 1, 2.5);
        testOperator(operator, -1.5F, 1, 2.5F);
        testOperator(operator, 1, 2, 1);
        testOperator(operator, 1L, 2L, 1);
        testOperator(operator, 1.5, 2.5, 1);
        testOperator(operator, 1.5F, 2.5F, 1);
    }

    @Test
    void testUnaryMinus() {
        val operator = new OperatorUnaryMinus();
        testOperator(operator, -1.5F, 1.5F);
        testOperator(operator, 1.5, -1.5);
    }

    @Test
    void testMultiply() {
        val operator = new OperatorMultiply();
        testOperator(operator, 2, 1, 2);
        testOperator(operator, 2L, 1, 2L);
        testOperator(operator, 2.5, 1, 2.5);
        testOperator(operator, 2.5F, 1, 2.5F);
        testOperator(operator, 2, 2, 1);
        testOperator(operator, 2L, 2L, 1);
        testOperator(operator, 2.5, 2.5, 1);
        testOperator(operator, 2.5F, 2.5F, 1);
    }

    @Test
    void testDivide() {
        val operator = new OperatorDivide();
        testOperator(operator, 0, 1, 2);
        testOperator(operator, 0L, 1, 2L);
        testOperator(operator, 0.4, 1, 2.5);
        testOperator(operator, 0.4F, 1, 2.5F);
        testOperator(operator, 2, 2, 1);
        testOperator(operator, 2L, 2L, 1);
        testOperator(operator, 2.5, 2.5, 1);
        testOperator(operator, 2.5F, 2.5F, 1);
    }


    @Test
    void testRemainder() {
        val operator = new OperatorRemainder();
        testOperator(operator, 1, 1, 2);
        testOperator(operator, 1L, 1, 2L);
        testOperator(operator, 1.0, 1, 2.5);
        testOperator(operator, 1F, 1, 2.5F);
        testOperator(operator, 0, 2, 1);
        testOperator(operator, 0L, 2L, 1);
        testOperator(operator, 0.5, 2.5, 1);
        testOperator(operator, 0.5F, 2.5F, 1);
    }

    @Test
    void testBitwiseXor() {
        val operator = new OperatorBitwiseXor();
        testOperator(operator, 3, 1, 2);
        testOperator(operator, 3L, 1, 2L);
        testOperator(operator, 3, 2, 1);
        testOperator(operator, 3L, 2L, 1);
    }

    @Test
    void testBitwiseOr() {
        val operator = new OperatorBitwiseOr();
        testOperator(operator, 3, 1, 2);
        testOperator(operator, 3L, 1, 2L);
        testOperator(operator, 3, 2, 1);
        testOperator(operator, 3L, 2L, 1);
    }

    @Test
    void testBitwiseAnd() {
        val operator = new OperatorBitwiseAnd();
        testOperator(operator, 0, 1, 2);
        testOperator(operator, 0L, 1, 2L);
        testOperator(operator, 0, 2, 1);
        testOperator(operator, 0L, 2L, 1);
    }

    @Test
    void testEquals() {
        val operator = new OperatorEquals();
        testOperator(operator, true, 1, 1);
        testOperator(operator, true, 1, 1L);
        testOperator(operator, true, 1F, 1L);
        testOperator(operator, false, 1, 0);
        testOperator(operator, false, 1, 3L);
        testOperator(operator, false, 1f, 1.1);
    }

    @Test
    void testNegate() {
        val operator = new OperatorNegate();
        testOperator(operator, false, true);
        testOperator(operator, true, false);
    }

    @Test
    void testNotEquals() {
        val operator = new OperatorNotEquals();
        testOperator(operator, false, 1, 1);
        testOperator(operator, false, 1, 1L);
        testOperator(operator, false, 1F, 1L);
        testOperator(operator, true, 1, 0);
        testOperator(operator, true, 1, 3L);
        testOperator(operator, true, 1f, 1.1);
    }
}