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
import io.github.whilein.jexpr.operand.OperandBase;
import io.github.whilein.jexpr.operand.defined.OperandBoolean;
import io.github.whilein.jexpr.operand.defined.OperandDouble;
import io.github.whilein.jexpr.operand.defined.OperandFloat;
import io.github.whilein.jexpr.operand.defined.OperandInteger;
import io.github.whilein.jexpr.operand.defined.OperandLong;
import io.github.whilein.jexpr.operand.defined.OperandObject;
import io.github.whilein.jexpr.operand.defined.OperandString;
import io.github.whilein.jexpr.operand.flat.FlatStream;
import io.github.whilein.jexpr.operator.BinaryLazyOperator;
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.UnaryOperator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OperandUndefinedBinary extends OperandBase implements OperandUndefined {

    Operand left, right;
    BinaryOperator operator;

    @Override
    public void flat(final @NotNull FlatStream flatStream) {
        right.flat(flatStream);
        left.flat(flatStream);
        flatStream.put(operator);
    }

    @Override
    public void toString(final @NotNull StringBuilder out) {
        val presence = operator.getPresence();

        if (left instanceof OperandUndefinedBinary
                && ((OperandUndefinedBinary) left).getOperator().getPresence() <= presence) {
            out.append('(');
            left.toString(out);
            out.append(')');
        } else {
            left.toString(out);
        }

        out.append(' ').append(operator.getValue()).append(' ');

        if (right instanceof OperandUndefinedBinary
                && ((OperandUndefinedBinary) right).getOperator().getPresence() <= presence) {
            out.append('(');
            right.toString(out);
            out.append(')');
        } else {
            right.toString(out);
        }
    }

    public static @NotNull Operand valueOf(
            final @NotNull Operand left,
            final @NotNull Operand right,
            final @NotNull BinaryOperator operator
    ) {
        if (left.isDefined() && right.isDefined()) {
            throw new IllegalStateException("Cannot create undefined expression from defined operands");
        }

        return new OperandUndefinedBinary(left, right, operator);
    }

    @Override
    public boolean isPredicable(final @NotNull BinaryLazyOperator operator) {
        return false;
    }

    @Override
    public @NotNull Operand getPredictedResult(final @NotNull BinaryLazyOperator operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Operand apply(final @NotNull Operand operand, final @NotNull BinaryOperator operator) {
        return operand.applyToUndefined(this, operator);
    }

    @Override
    public @NotNull Operand applyToInt(final int number, final @NotNull BinaryOperator operator) {
        return OperandUndefinedBinary.valueOf(OperandInteger.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToLong(final long number, final @NotNull BinaryOperator operator) {
        return OperandUndefinedBinary.valueOf(OperandLong.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToDouble(final double number, final @NotNull BinaryOperator operator) {
        return OperandUndefinedBinary.valueOf(OperandDouble.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToFloat(final float number, final @NotNull BinaryOperator operator) {
        return OperandUndefinedBinary.valueOf(OperandFloat.valueOf(number), this, operator);
    }

    @Override
    public @NotNull Operand applyToString(final @NotNull String value, final @NotNull BinaryOperator operator) {
        return OperandUndefinedBinary.valueOf(OperandString.valueOf(value), this, operator);
    }

    @Override
    public @NotNull Operand applyToBoolean(final boolean value, final @NotNull BinaryOperator operator) {
        return OperandUndefinedBinary.valueOf(OperandBoolean.valueOf(value), this, operator);
    }

    @Override
    public @NotNull Operand applyToUndefined(final @NotNull OperandUndefined undefined, final @NotNull BinaryOperator operator) {
        return OperandUndefinedBinary.valueOf(undefined, this, operator);
    }

    @Override
    public @NotNull Operand applyToObject(final Object value, final @NotNull BinaryOperator operator) {
        return OperandUndefinedBinary.valueOf(OperandObject.valueOf(value), this, operator);
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
    public @NotNull Operand apply(final @NotNull UnaryOperator operator) {
        return OperandUndefinedUnary.valueOf(this, operator);
    }

    @Override
    public @NotNull Operand solve(final @NotNull UndefinedResolver resolver) {
        val solvedLeft = left.solve(resolver);

        if (operator instanceof BinaryLazyOperator) {
            val lazyOperator = (BinaryLazyOperator) operator;

            if (solvedLeft.isPredicable(lazyOperator)) {
                return solvedLeft.getPredictedResult(lazyOperator);
            }
        }

        return solvedLeft.apply(right.solve(resolver), operator);
    }

}