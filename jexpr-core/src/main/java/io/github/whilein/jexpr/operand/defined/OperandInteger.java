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

package io.github.whilein.jexpr.operand.defined;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.undefined.OperandUndefined;
import io.github.whilein.jexpr.operand.undefined.OperandUndefinedSequence;
import io.github.whilein.jexpr.operator.Operator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class OperandInteger extends OperandNumber {

    int value;

    private OperandInteger(final int value) {
        super(value);

        this.value = value;
    }

    private static final OperandInteger[] CACHE = new OperandInteger[256];

    static {
        for (int i = -128; i <= 127; i++) {
            CACHE[i + 128] = new OperandInteger(i);
        }
    }

    public static @NotNull Operand valueOf(final int value) {
        return value >= -128 && value <= 127 ? CACHE[value + 128] : new OperandInteger(value);
    }

    @Override
    public boolean isPredicable(final @NotNull Operator operator) {
        return operator.isPredictable(value);
    }


    @Override
    public @NotNull Operand apply(final @NotNull Operand operand, final @NotNull Operator operator) {
        return operand.applyToInt(value, operator);
    }

    @Override
    public @NotNull Operand applyToInt(final int number, final @NotNull Operator operator) {
        return operator.apply(number, value);
    }

    @Override
    public @NotNull Operand applyToLong(final long number, final @NotNull Operator operator) {
        return operator.apply(number, value);
    }

    @Override
    public @NotNull Operand applyToDouble(final double number, final @NotNull Operator operator) {
        return operator.apply(number, value);
    }

    @Override
    public @NotNull Operand applyToFloat(final float number, final @NotNull Operator operator) {
        return operator.apply(number, value);
    }

    @Override
    public @NotNull Operand applyToString(final @NotNull String value, final @NotNull Operator operator) {
        return operator.apply(value, this.value);
    }

    @Override
    public @NotNull Operand applyToBoolean(final boolean value, final @NotNull Operator operator) {
        return operator.apply(value, this.value);
    }

    @Override
    public @NotNull Operand applyToUndefined(final @NotNull OperandUndefined undefined, final @NotNull Operator operator) {
        return OperandUndefinedSequence.valueOf(undefined, this, operator);
    }

    @Override
    public @NotNull Operand applyToObject(final @NotNull Object value, final @NotNull Operator operator) {
        return operator.apply(value, this.value);
    }

    @Override
    public @NotNull Operand apply(final @NotNull Operator operator) {
        return operator.apply(value);
    }

}
