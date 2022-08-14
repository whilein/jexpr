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

package io.github.whilein.jexpr.operator.type;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.OperandBoolean;
import io.github.whilein.jexpr.operator.AbstractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperatorOr extends AbstractOperator {

    public OperatorOr() {
        super("||", 3, TWO_OPERAND);
    }

    @Override
    public boolean isPredictable(final boolean value) {
        return value;
    }

    @Override
    public @NotNull Operand apply(final boolean value) {
        return !value ? super.apply(false) : OperandBoolean.TRUE;
    }

    @Override
    public String toString() {
        return "OR";
    }

    @Override
    public @NotNull Operand apply(final boolean left, final boolean right) {
        return OperandBoolean.valueOf(left || right);
    }
}
