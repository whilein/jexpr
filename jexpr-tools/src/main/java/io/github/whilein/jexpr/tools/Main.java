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

import io.github.whilein.jexpr.AbstractJexpr;
import io.github.whilein.jexpr.DefaultJexpr;
import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandVariableResolver;
import io.github.whilein.jexpr.token.operand.variable.OperandBinaryNode;
import io.github.whilein.jexpr.token.operand.variable.OperandReference;
import io.github.whilein.jexpr.token.operand.variable.OperandUnaryNode;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author whilein
 */
@UtilityClass
public class Main {

    private static final int OPERAND_COLOR = 0xffecaf;

    private static final int OPERATOR_COLOR = 0xafdeff;

    private static final double OPERAND_WIDTH = 200;
    private static final double OPERATOR_WIDTH = 100;

    private static final double BRANCH_GAP = 10;

    public void main(final String[] args) throws IOException {
        val jexpr = DefaultJexpr.create();
        Operand expression = jexpr.parse(args[0]);

        val solvedMap = new HashMap<String, Operand>();
        val in = new Scanner(System.in);

        val graph = Graph.create();

        double y = 0;

        while (!expression.isConstant()) {
            val tree = toTree(expression, 0, y);

            addNode(graph, tree);

            expression = expression.solve(new OperandVariableResolver() {

                boolean solved;

                public Operand input(final String reference) {
                    try {
                        System.out.print(reference + ": ");

                        return jexpr.parse(in.nextLine());
                    } finally {
                        solved = true;
                    }
                }

                @Override
                public @NotNull Operand resolve(final @NotNull String reference) {
                    if (solved) {
                        return solvedMap.getOrDefault(reference, OperandReference.valueOf(reference));
                    }

                    return solvedMap.computeIfAbsent(reference, this::input);
                }
            });

            y += tree.getHeight() + 100;
        }

        graph.save(Paths.get("graph.xgml"));
    }

    private static int addNode(final Graph graph, final Node node) {
        if (node instanceof ValueNode) {
            val value = (ValueNode) node;

            return graph.addNode(node.getX(), node.getY(), node.getSurfaceWidth(), node.getSurfaceHeight(),
                    value.getLabel(), value.getColor());
        } else if (node instanceof BinaryNode) {
            val binary = (BinaryNode) node;

            val operator = binary.getOperator();

            val operatorNode = graph.addNode(operator.getX(), operator.getY(), operator.getWidth(), operator.getHeight(),
                    operator.getLabel(), operator.getColor());

            val leftNode = addNode(graph, binary.getLeft());
            val rightNode = addNode(graph, binary.getRight());

            graph.addEdge(operatorNode, leftNode);
            graph.addEdge(operatorNode, rightNode);

            return operatorNode;
        } else if (node instanceof UnaryNode) {
            val unary = (UnaryNode) node;
            val operator = unary.getOperator();

            val operatorNode = graph.addNode(operator.getX(), operator.getY(), operator.getWidth(), operator.getHeight(),
                    operator.getLabel(), operator.getColor());

            val unaryNode = addNode(graph, unary.getNode());

            graph.addEdge(operatorNode, unaryNode);

            return operatorNode;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static Node toTree(final Operand operand, final double x, final double y) {
        if (operand.isConstant() || operand instanceof OperandReference) {
            return new ValueNode(x, y, OPERAND_WIDTH, 20, String.valueOf(operand.getValue()), OPERAND_COLOR);
        } else if (operand instanceof OperandBinaryNode) {
            val binary = (OperandBinaryNode) operand;

            val operator = new ValueNode(x, y, OPERATOR_WIDTH, 20, String.valueOf(binary.getOperator()), OPERATOR_COLOR);

            val left = toTree(binary.getLeftMember(), x - BRANCH_GAP, y + 50);
            left.moveX(-left.getWidth() / 2);

            val right = toTree(binary.getRightMember(), x + BRANCH_GAP, y + 50);
            right.moveX(right.getWidth() / 2);

            return new BinaryNode(operator, left, right, BRANCH_GAP);
        } else if (operand instanceof OperandUnaryNode) {
            val unary = (OperandUnaryNode) operand;

            val node = new ValueNode(x, y, OPERATOR_WIDTH, 20, String.valueOf(unary.getOperator()), OPERATOR_COLOR);

            val value = toTree(unary.getMember(), x, y + 50);

            return new UnaryNode(node, value);
        } else {
            throw new IllegalStateException();
        }
    }

}
