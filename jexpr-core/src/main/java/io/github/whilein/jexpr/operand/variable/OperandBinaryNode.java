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

package io.github.whilein.jexpr.operand.variable;

import io.github.whilein.jexpr.OperandVariableResolver;
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.OperandBase;
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
public final class OperandBinaryNode extends OperandBase implements OperandVariable {

    Operand left, right;
    BinaryOperator operator;

    @Override
    public void toString(final @NotNull StringBuilder out) {
        val presence = operator.getPresence();

        if (left instanceof OperandBinaryNode
                && ((OperandBinaryNode) left).getOperator().getPresence() <= presence) {
            out.append('(');
            left.toString(out);
            out.append(')');
        } else {
            left.toString(out);
        }

        out.append(' ').append(operator.getValue()).append(' ');

        if (right instanceof OperandBinaryNode
                && ((OperandBinaryNode) right).getOperator().getPresence() <= presence) {
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
        if (left.isConstant() && right.isConstant()) {
            throw new IllegalStateException("Cannot create variable binary node from constant operands");
        }

        return new OperandBinaryNode(left, right, operator);
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
        return operand.applyToVariable(this, operator);
    }

    @Override
    public @NotNull Operand applyToInt(final int number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, this);
    }

    @Override
    public @NotNull Operand applyToLong(final long number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, this);
    }

    @Override
    public @NotNull Operand applyToDouble(final double number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, this);
    }

    @Override
    public @NotNull Operand applyToFloat(final float number, final @NotNull BinaryOperator operator) {
        return operator.apply(number, this);
    }

    @Override
    public @NotNull Operand applyToString(final @NotNull String value, final @NotNull BinaryOperator operator) {
        return operator.apply(value, this);
    }

    @Override
    public @NotNull Operand applyToBoolean(final boolean value, final @NotNull BinaryOperator operator) {
        return operator.apply(value, this);
    }

    @Override
    public @NotNull Operand applyToVariable(
            final @NotNull OperandVariable variable,
            final @NotNull BinaryOperator operator
    ) {
        return operator.apply(variable, this);
    }

    @Override
    public @NotNull Operand applyToObject(final Object value, final @NotNull BinaryOperator operator) {
        return operator.apply(value, this);
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
        return OperandUnaryNode.valueOf(this, operator);
    }

    @Override
    public @NotNull Operand solve(final @NotNull OperandVariableResolver resolver) {
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
