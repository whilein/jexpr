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

package io.github.whilein.jexpr.token.operand;

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandBinary;
import io.github.whilein.jexpr.api.token.operand.OperandVariableResolver;
import io.github.whilein.jexpr.api.token.operator.BinaryLazyOperator;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
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
@RequiredArgsConstructor
final class OperandBinaryImpl extends OperandBase implements OperandBinary {

    Operand leftMember, rightMember;
    BinaryOperator operator;

    @Override
    public void print(final @NotNull StringBuilder out) {
        val presence = operator.getPresence();

        if (leftMember instanceof OperandBinary
                && ((OperandBinary) leftMember).getOperator().getPresence() <= presence) {
            out.append('(');
            leftMember.print(out);
            out.append(')');
        } else {
            leftMember.print(out);
        }

        out.append(' ').append(operator.getValue()).append(' ');

        if (rightMember instanceof OperandBinary
                && ((OperandBinary) rightMember).getOperator().getPresence() <= presence) {
            out.append('(');
            rightMember.print(out);
            out.append(')');
        } else {
            rightMember.print(out);
        }
    }

    @Override
    public @NotNull Operand solve(final @NotNull OperandVariableResolver resolver) {
        val solvedLeft = leftMember.solve(resolver);

        if (operator instanceof BinaryLazyOperator) {
            val lazyOperator = (BinaryLazyOperator) operator;

            if (solvedLeft.isPredicable(lazyOperator)) {
                return solvedLeft.getPredictedResult(lazyOperator);
            }
        }

        return solvedLeft.apply(rightMember.solve(resolver), operator);
    }

}
