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

import lombok.Value;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
@Value
public class BinaryNode implements Node {

    ValueNode operator;
    Node left, right;
    double gap;

    @Override
    public double getX() {
        return operator.getX();
    }

    @Override
    public double getY() {
        return operator.getY();
    }

    @Override
    public double getWidth() {
        // расстояние между левой и правой веткой
        return gap + left.getWidth() + gap + right.getWidth() + gap;
    }

    @Override
    public double getHeight() {
        val leftGap = left.getY() - operator.getY();
        val rightGap = right.getY() - operator.getY();
        // в идеале они должны быть одинаковы, но всё же...

        return Math.max(leftGap + left.getHeight(), rightGap + right.getHeight()) + operator.getSurfaceHeight();
    }

    @Override
    public void moveX(final double x) {
        operator.moveX(x);
        left.moveX(x);
        right.moveX(x);
    }

    @Override
    public void moveY(final double y) {
        operator.moveY(y);
        left.moveY(y);
        right.moveY(y);
    }

    @Override
    public double getSurfaceWidth() {
        return operator.getWidth();
    }

    @Override
    public double getSurfaceHeight() {
        return operator.getHeight();
    }

    @Override
    public int add(@NotNull Graph graph) {
        val operatorNode = graph.addNode(operator.getX(), operator.getY(), operator.getWidth(), operator.getHeight(),
                operator.getLabel(), operator.getColor());

        val leftNode = left.add(graph);
        val rightNode = right.add(graph);

        graph.addEdge(operatorNode, leftNode);
        graph.addEdge(operatorNode, rightNode);

        return operatorNode;
    }
}
