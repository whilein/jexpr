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
import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandBinary;
import io.github.whilein.jexpr.api.token.operand.OperandUnary;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.token.operator.type.*;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static io.github.whilein.jexpr.token.operand.Operands.reference;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * @author whilein
 */
class AstTests {
    static Jexpr jexpr;

    @BeforeAll
    static void setup() {
        jexpr = DefaultJexpr.create();
    }

    /**
     * Тестирует приоритет бинарных операторов
     */
    @Test
    public void testBinaryPrecedence() {
        val root = jexpr.parse("x + y * z");

        assertBinary(
                root,
                reference("x"),
                right -> assertBinary(
                        right,
                        reference("y"),
                        reference("z"),
                        OperatorMultiply.class
                ),
                OperatorPlus.class
        );
    }

    /**
     * Протестировать приоритет унарных операторов. У унарных операторов одинаковый приоритет,
     * поэтому тест идёт с оператором '.', поскольку он является единственным, чей приоритет выше унарного.
     */
    @Test
    public void testUnaryPrecedence() {
        val root = jexpr.parse("-x.y");

        assertUnary(
                root,
                member -> assertBinary(
                        member,
                        reference("x"),
                        reference("y"),
                        OperatorMemberSelection.class
                ),
                OperatorUnaryMinus.class
        );
    }

    /**
     * Простейшее выражение с использованием унарного и бинарного операторов
     */
    @Test
    public void testSimpleMixed0() {
        val root = jexpr.parse("-x / y");

        assertBinary(
                root,
                left -> assertUnary(left, reference("x"), OperatorUnaryMinus.class),
                reference("y"),
                OperatorDivide.class
        );
    }

    /**
     * Простейшее выражение с использованием унарного и бинарного операторов
     */
    @Test
    public void testSimpleMixed1() {
        val root = jexpr.parse("x / -y");

        assertBinary(
                root,
                reference("x"),
                right -> assertUnary(right, reference("y"), OperatorUnaryMinus.class),
                OperatorDivide.class
        );
    }

    /**
     * Простейшее выражение с использованием унарного и бинарного операторов
     */
    @Test
    public void testSimpleMixed2() {
        val root = jexpr.parse("-x / -y");

        assertBinary(
                root,
                left -> assertUnary(left, reference("x"), OperatorUnaryMinus.class),
                right -> assertUnary(right, reference("y"), OperatorUnaryMinus.class),
                OperatorDivide.class
        );
    }

    @Test
    public void testUnaryOrder() {
        val root = jexpr.parse("-~x");

        assertUnary(
                root,
                member -> assertUnary(member, reference("x"), OperatorBitwiseComplement.class),
                OperatorUnaryMinus.class
        );
    }

    @Test
    public void testSimpleUnary() {
        val root = jexpr.parse("-x");

        assertUnary(
                root,
                reference("x"),
                OperatorUnaryMinus.class
        );
    }

    /**
     * Тест простейшего бинарного выражения
     */
    @Test
    public void testSimpleBinary() {
        val root = jexpr.parse("x + y");

        assertBinary(
                root,
                reference("x"),
                reference("y"),
                OperatorPlus.class
        );
    }

    private void assertUnary(
            final Operand operand,
            final Operand member,
            final Class<? extends UnaryOperator> operator
    ) {
        assertUnary(operand, actualMember -> assertEquals(member, actualMember), operator);
    }

    private void assertUnary(
            final Operand operand,
            final Consumer<Operand> memberAssertion,
            final Class<? extends UnaryOperator> operator
    ) {
        assertInstanceOf(OperandUnary.class, operand);

        val unaryNode = (OperandUnary) operand;
        memberAssertion.accept(unaryNode.getMember());

        assertInstanceOf(operator, unaryNode.getOperator());
    }

    private void assertBinary(
            final Operand operand,
            final Consumer<Operand> leftAssertion,
            final Consumer<Operand> rightAssertion,
            final Class<? extends BinaryOperator> operator
    ) {
        assertInstanceOf(OperandBinary.class, operand);

        val binaryNode = (OperandBinary) operand;

        leftAssertion.accept(binaryNode.getLeftMember());
        rightAssertion.accept(binaryNode.getRightMember());
        assertInstanceOf(operator, binaryNode.getOperator());
    }

    private void assertBinary(
            final Operand binaryNode,
            final Operand left,
            final Consumer<Operand> rightAssertion,
            final Class<? extends BinaryOperator> operator
    ) {
        assertBinary(binaryNode, actualLeft -> assertEquals(left, actualLeft), rightAssertion, operator);
    }

    private void assertBinary(
            final Operand binaryNode,
            final Consumer<Operand> leftAssertion,
            final Operand right,
            final Class<? extends BinaryOperator> operator
    ) {
        assertBinary(binaryNode, leftAssertion, actualRight -> assertEquals(right, actualRight), operator);
    }

    private void assertBinary(
            final Operand binaryNode,
            final Operand left,
            final Operand right,
            final Class<? extends BinaryOperator> operator
    ) {
        assertBinary(
                binaryNode,
                actualLeft -> assertEquals(left, actualLeft),
                actualRight -> assertEquals(right, actualRight),
                operator
        );
    }


}
