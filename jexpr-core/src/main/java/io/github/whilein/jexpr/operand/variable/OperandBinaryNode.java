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
    public void print(final @NotNull StringBuilder out) {
        val presence = operator.getPresence();

        if (left instanceof OperandBinaryNode
                && ((OperandBinaryNode) left).getOperator().getPresence() <= presence) {
            out.append('(');
            left.print(out);
            out.append(')');
        } else {
            left.print(out);
        }

        out.append(' ').append(operator.getValue()).append(' ');

        if (right instanceof OperandBinaryNode
                && ((OperandBinaryNode) right).getOperator().getPresence() <= presence) {
            out.append('(');
            right.print(out);
            out.append(')');
        } else {
            right.print(out);
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
