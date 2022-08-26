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

package io.github.whilein.jexpr.operand.undefined;

import io.github.whilein.jexpr.UndefinedResolver;
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.defined.OperandBoolean;
import io.github.whilein.jexpr.operand.defined.OperandDouble;
import io.github.whilein.jexpr.operand.defined.OperandFloat;
import io.github.whilein.jexpr.operand.defined.OperandInteger;
import io.github.whilein.jexpr.operand.defined.OperandLong;
import io.github.whilein.jexpr.operand.defined.OperandString;
import io.github.whilein.jexpr.operator.Operator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OperandUndefinedMember implements OperandUndefined {

    Operand member;
    Operator operator;

    @Override
    public String toString() {
        return operator.getValue() + member;
    }

    public static @NotNull Operand valueOf(
            final @NotNull Operand value,
            final @NotNull Operator operator
    ) {
        if (value.isDefined()) {
            throw new IllegalStateException("Cannot create undefined expression from defined operand");
        }

        return new OperandUndefinedMember(value, operator);
    }

    @Override
    public boolean isPredicable(final @NotNull Operator operator) {
        return false;
    }

    @Override
    public @NotNull Operand apply(final @NotNull Operand operand, final @NotNull Operator operator) {
        return operand.applyToUndefined(this, operator);
    }

    @Override
    public @NotNull Operand applyToInt(final int number, final @NotNull Operator operator) {
        return OperandUndefinedSequence.valueOf(OperandInteger.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToLong(final long number, final @NotNull Operator operator) {
        return OperandUndefinedSequence.valueOf(OperandLong.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToDouble(final double number, final @NotNull Operator operator) {
        return OperandUndefinedSequence.valueOf(OperandDouble.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToFloat(final float number, final @NotNull Operator operator) {
        return OperandUndefinedSequence.valueOf(OperandFloat.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToString(final @NotNull String value, final @NotNull Operator operator) {
        return OperandUndefinedSequence.valueOf(OperandString.valueOf(value), this, operator);
    }

    @Override
    public @NotNull Operand applyToBoolean(final boolean value, final @NotNull Operator operator) {
        return OperandUndefinedSequence.valueOf(OperandBoolean.valueOf(value), this, operator);
    }

    @Override
    public @NotNull Operand applyToUndefined(final @NotNull OperandUndefined undefined, final @NotNull Operator operator) {
        return OperandUndefinedSequence.valueOf(undefined, this, operator);
    }

    @Override
    public @NotNull Object getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public @NotNull Operand apply(final @NotNull Operator operator) {
        return OperandUndefinedMember.valueOf(this, operator);
    }

    @Override
    public @NotNull Operand solve(final @NotNull UndefinedResolver resolver) {
        return member.solve(resolver).apply(operator);
    }
}
