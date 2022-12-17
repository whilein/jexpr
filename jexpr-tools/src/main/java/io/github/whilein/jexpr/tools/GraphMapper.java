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

package io.github.whilein.jexpr.tools;

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandMapper;
import io.github.whilein.jexpr.api.token.operand.OperandVariable;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class GraphMapper implements OperandMapper<Node> {
    private static final int OPERAND_COLOR = 0xffecaf;

    private static final int OPERATOR_COLOR = 0xafdeff;

    private static final double OPERAND_WIDTH = 200;
    private static final double OPERATOR_WIDTH = 100;

    private static final double BRANCH_GAP = 10;

    private double x, y;

    public void loc(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Node mapUnary(@NotNull OperandVariable left, @NotNull UnaryOperator op) {
        val x = this.x;
        val y = this.y;

        val node = new ValueNode(x, y, OPERATOR_WIDTH, 20, String.valueOf(op), OPERATOR_COLOR);

        loc(x, y + 50);
        val value = left.apply(this);

        loc(x, y);

        return new UnaryNode(node, value);
    }

    @Override
    public Node mapBinary(@NotNull Operand left, @NotNull Operand right, @NotNull BinaryOperator op) {
        val x = this.x;
        val y = this.y;

        val operator = new ValueNode(x, y, OPERATOR_WIDTH, 20, String.valueOf(op), OPERATOR_COLOR);

        loc(x - BRANCH_GAP, y + 50);
        val leftNode = left.apply(this);
        leftNode.moveX(-leftNode.getWidth() / 2);

        loc(x + BRANCH_GAP, y + 50);
        val rightNode = right.apply(this);
        rightNode.moveX(rightNode.getWidth() / 2);

        loc(x, y);

        return new BinaryNode(operator, leftNode, rightNode, BRANCH_GAP);
    }

    private Node mapValue(String value) {
        return new ValueNode(x, y, OPERAND_WIDTH, 20, String.valueOf(value), OPERAND_COLOR);
    }

    @Override
    public Node mapReference(@NotNull String value) {
        return mapValue(value);
    }

    @Override
    public Node mapConstant(Object object) {
        return mapValue(String.valueOf(object));
    }

}
