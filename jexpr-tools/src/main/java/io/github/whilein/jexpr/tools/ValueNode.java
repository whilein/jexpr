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

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * @author whilein
 */
@Value
@AllArgsConstructor
public class ValueNode implements Node {

    @NonFinal
    double x, y;
    double width, height;
    String label;
    int color;


    @Override
    public void moveX(final double deltaX) {
        x += deltaX;
    }

    @Override
    public void moveY(final double deltaY) {
        y += deltaY;
    }

    @Override
    public double getSurfaceWidth() {
        return width;
    }

    @Override
    public double getSurfaceHeight() {
        return height;
    }
}
