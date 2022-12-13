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

package io.github.whilein.jexpr.api.token.operator;

import io.github.whilein.jexpr.api.token.operand.Operand;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public interface BinaryLazyOperator extends BinaryOperator {
    boolean isPredictable(boolean value);

    boolean isPredictable(int value);

    boolean isPredictable(long value);

    boolean isPredictable(double value);

    boolean isPredictable(float value);

    boolean isPredictable(Object value);

    boolean isPredictable(@NotNull String value);

    @NotNull Operand getPredictedResult(int value);

    @NotNull Operand getPredictedResult(long value);

    @NotNull Operand getPredictedResult(float value);

    @NotNull Operand getPredictedResult(double value);

    @NotNull Operand getPredictedResult(Object value);

    @NotNull Operand getPredictedResult(String value);

    @NotNull Operand getPredictedResult(boolean value);

}
