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

import io.github.whilein.jexpr.SimpleExpressionParser;
import io.github.whilein.jexpr.UndefinedResolver;
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.undefined.OperandReference;
import io.github.whilein.jexpr.operand.undefined.OperandUndefinedMember;
import io.github.whilein.jexpr.operand.undefined.OperandUndefinedSequence;
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
        val expressionParser = SimpleExpressionParser.createDefault();
        Operand expression = expressionParser.parse(args[0]);

        val solvedMap = new HashMap<String, Operand>();
        val in = new Scanner(System.in);

        int index = 0;

        while (!expression.isDefined()) {
            val graph = Graph.create();

            addOperand(graph, expression, 0, 0);

            graph.save(Paths.get("graph_" + index++ + ".xgml"));

            expression = expression.solve(new UndefinedResolver() {

                boolean solved;

                public Operand input(final String reference) {
                    try {
                        System.out.print(reference + ": ");

                        return expressionParser.parse(in.nextLine());
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
        }
    }

    private static double getWidth(final Operand operand) {
        if (operand instanceof OperandUndefinedSequence) {
            val sequence = (OperandUndefinedSequence) operand;

            val left = getWidth(sequence.getLeft());
            val right = getWidth(sequence.getRight());

            return left + right + BRANCH_GAP;
        } else if (operand instanceof OperandUndefinedMember) {
            val member = (OperandUndefinedMember) operand;

            return getWidth(member.getMember());
        }

        return OPERAND_WIDTH;
    }

    private static int addOperand(final Graph graph, final Operand operand, final double x, final double y) {
        if (operand.isDefined() || operand instanceof OperandReference) {
            return graph.addNode(x, y, OPERAND_WIDTH, 20, String.valueOf(operand.getValue()), OPERAND_COLOR);
        } else if (operand instanceof OperandUndefinedSequence) {
            val sequence = (OperandUndefinedSequence) operand;

            val node = graph.addNode(x, y, OPERATOR_WIDTH, 20, String.valueOf(sequence.getOperator()), OPERATOR_COLOR);

            val left = addOperand(graph, sequence.getLeft(),
                    x - (BRANCH_GAP + getWidth(sequence.getLeft()) / 2),
                    y + 50);
            val right = addOperand(graph, sequence.getRight(),
                    x + (BRANCH_GAP + getWidth(sequence.getRight()) / 2),
                    y + 50);

            graph.addEdge(node, left);
            graph.addEdge(node, right);

            return node;
        } else if (operand instanceof OperandUndefinedMember) {
            val member = (OperandUndefinedMember) operand;

            val node = graph.addNode(x, y, OPERATOR_WIDTH, 20, String.valueOf(member.getOperator()), OPERATOR_COLOR);

            val value = addOperand(graph, member.getMember(), x, y + 50);

            graph.addEdge(node, value);

            return node;
        } else {
            return -1;
        }
    }

}
