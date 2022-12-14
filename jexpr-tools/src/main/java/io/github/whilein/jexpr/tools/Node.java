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

import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public interface Node {

    double getX();

    double getY();

    double getWidth();

    double getHeight();

    void moveX(double deltaX);

    void moveY(double deltaY);

    double getSurfaceWidth();

    double getSurfaceHeight();

    int add(@NotNull Graph graph);

}
