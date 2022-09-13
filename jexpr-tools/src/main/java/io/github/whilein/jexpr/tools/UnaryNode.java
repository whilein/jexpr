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

/**
 * @author whilein
 */
@Value
public class UnaryNode implements Node {

    ValueNode operator;
    Node node;

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
        return node.getWidth();
    }

    @Override
    public double getHeight() {
        return (node.getY() - operator.getY()) + node.getHeight();
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
    public void moveX(final double x) {
        operator.moveX(x);
        node.moveX(x);
    }

    @Override
    public void moveY(final double y) {
        operator.moveY(y);
        node.moveY(y);
    }

}
