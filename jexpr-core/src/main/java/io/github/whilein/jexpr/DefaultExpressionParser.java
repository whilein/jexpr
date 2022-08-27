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

package io.github.whilein.jexpr;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operator.Operator;
import io.github.whilein.jexpr.token.AbstractTokenVisitor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultExpressionParser extends AbstractTokenVisitor implements ExpressionParser {

    private static final int STATE_OPEN_PARENTHESES = 0, STATE_CONTENT = 1;

    Deque<Operator> operators = new ArrayDeque<>();

    Deque<Operator> operatorStack = new ArrayDeque<>();

    Deque<Operand> operandStack = new ArrayDeque<>();

    @NonFinal
    DefaultExpressionParser parent;

    // 5 + 5 * 5 = 5 * 5 + 5
    // 5 * 5 + 5 * 5 = 50
    // 5 - (5 + 1)

    @NonFinal
    int state = STATE_OPEN_PARENTHESES;

    @NonFinal
    DefaultExpressionParser nestedExpressionParser;

    @NonFinal
    Operand previousOperand;

    @Getter
    @NonFinal
    Operand result;

    public static @NotNull ExpressionParser create() {
        return new DefaultExpressionParser();
    }

    private static SyntaxException invalidSyntax(final String message) {
        throw new SyntaxException(message);
    }

    @Override
    public void visitOperator(final @NotNull Operator operator) {
        if (nestedExpressionParser != null) {
            nestedExpressionParser.visitOperator(operator);
            return;
        }

        operators.push(operator);
    }

    @Override
    public void visitOperand(final @NotNull Operand operand) {
        if (nestedExpressionParser != null) {
            nestedExpressionParser.visitOperand(operand);
            return;
        }

        addOperand(operand);
    }

    private void addOperand(final Operand operand) {
        Operand processedNumber = operand;

        val operators = this.operators;

        if (operators.isEmpty()) {
            if (previousOperand != null) {
                throw new SyntaxException("Unexpected operand got: " + operand);
            }
        } else {
            while (true) {
                val operator = operators.pop();

                if (operators.isEmpty()) {
                    if (previousOperand != null) {
                        if (!operator.isTwoOperand()) {
                            throw invalidSyntax("Unexpected operator: " + operator);
                        }

                        addOperator(operator);
                        break;
                    }
                }

                processedNumber = processedNumber.apply(operator);

                if (operators.isEmpty()) {
                    break;
                }
            }
        }

        addMember(processedNumber);
    }

    private void solve(final int minOperator) {
        while (operandStack.size() >= 2 && operatorStack.size() >= 1) {
            val topOperator = operatorStack.getFirst();

            if (minOperator > topOperator.getOrder()) {
                break;
            }

            operatorStack.removeFirst();

            val right = operandStack.pop();
            val left = operandStack.pop();

            addMember(left.apply(right, topOperator));
        }
    }

    private void addOperator(final Operator operator) {
        solve(operator.getOrder());

        operatorStack.push(operator);
    }

    private void addMember(final Operand operand) {
        previousOperand = operand;
        operandStack.push(operand);
    }

    @Override
    public void visitParenthesesOpen() {
        if (state == STATE_OPEN_PARENTHESES) {
            state = STATE_CONTENT;

            return;
        }

        if (nestedExpressionParser == null) {
            nestedExpressionParser = new DefaultExpressionParser();
            nestedExpressionParser.parent = this;
        }

        nestedExpressionParser.visitParenthesesOpen();
    }

    @Override
    public void visitParenthesesClose() {
        if (nestedExpressionParser != null) {
            nestedExpressionParser.visitParenthesesClose();
            return;
        }

        val result = this.result = doFinal();

        if (parent != null) {
            parent.addOperand(result);
            parent.nestedExpressionParser = null;
        }
    }

    private Operand doFinal() {
        try {
            if (!operators.isEmpty()) {
                throw invalidSyntax("There are " + operators.size() + " remaining operators: " + operators);
            }

            solve(-1);

            return operandStack.pop();
        } finally {
            operators.clear();
            operandStack.clear();
            operatorStack.clear();

            previousOperand = null;
            state = STATE_OPEN_PARENTHESES;
        }
    }

}
