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
import io.github.whilein.jexpr.operand.variable.OperandVariable;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public interface UnaryOperator extends Operator {
    @NotNull Operand apply(int value);

    @NotNull Operand apply(long value);

    @NotNull Operand apply(float value);

    @NotNull Operand apply(double value);

    @NotNull Operand apply(Object value);

    @NotNull Operand apply(String value);

    @NotNull Operand apply(boolean value);

    @NotNull Operand apply(@NotNull OperandVariable variable);
}
