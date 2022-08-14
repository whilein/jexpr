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
import io.github.whilein.jexpr.operand.OperandInteger;
import io.github.whilein.jexpr.operand.OperandLong;
import io.github.whilein.jexpr.operator.AbstractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public final class OperatorBitwiseComplement extends AbstractOperator {

    public OperatorBitwiseComplement() {
        super("~", 13, ONE_OPERAND);
    }

    @Override
    public String toString() {
        return "BITWISE_COMPLEMENT";
    }

    @Override
    public @NotNull Operand apply(final int value) {
        return OperandInteger.valueOf(~value);
    }

    @Override
    public @NotNull Operand apply(final long value) {
        return OperandLong.valueOf(~value);
    }

}
