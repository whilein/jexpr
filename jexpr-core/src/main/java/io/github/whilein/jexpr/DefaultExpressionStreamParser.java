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
import io.github.whilein.jexpr.token.AbstractTokenParser;
import io.github.whilein.jexpr.token.SelectableTokenParser;
import io.github.whilein.jexpr.token.Token;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultExpressionStreamParser extends AbstractTokenParser
        implements SelectableTokenParser, ExpressionStreamParser {

    private static final int STATE_LEADING_BRACKET = 0, STATE_CONTENT = 1, STATE_FINAL_BRACKET = 2;

    Deque<Operator> operators = new ArrayDeque<>();

    Deque<Operator> operatorStack = new ArrayDeque<>();

    Deque<Operand> operandStack = new ArrayDeque<>();

    @NonFinal
    DefaultExpressionStreamParser parent;

    @NonFinal
    DefaultExpressionStreamParser nestedExpressionParser;

    List<SelectableTokenParser> parsers;

    @NonFinal
    SelectableTokenParser activeParser;

    @NonFinal
    int state = STATE_LEADING_BRACKET;

    @NonFinal
    Operand previousOperand;

    @Override
    public boolean shouldStayActive(final int ch) {
        return state != STATE_FINAL_BRACKET;
    }

    @Override
    public boolean shouldActivate(final int ch) {
        return parent == null || ch == '(';
    }

    public static @NotNull ExpressionStreamParser create(final @NotNull List<@NotNull SelectableTokenParser> parsers) {
        return new DefaultExpressionStreamParser(new ArrayList<>(parsers));
    }

    @Override
    protected void writeSyntaxReport(final Map<String, Object> map) {
        map.put("activeParser", activeParser);
        map.put("parsers", parsers);
        map.put("state", state);
    }

    private void onToken(final @NotNull Token token) {
        if (nestedExpressionParser != null) {
            nestedExpressionParser.onToken(token);
            return;
        }

        if (token instanceof Operator) {
            operators.push((Operator) token);
        } else if (token instanceof Operand) {
            addOperand((Operand) token);
        }
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

    private void onParenthesesOpen() {
        if (state == STATE_LEADING_BRACKET) {
            state = STATE_CONTENT;
            return;
        }

        if (nestedExpressionParser == null) {
            nestedExpressionParser = new DefaultExpressionStreamParser(parsers);
            nestedExpressionParser.parent = this;
        }

        nestedExpressionParser.onParenthesesOpen();
    }

    @Override
    public void update(final int ch) throws SyntaxException {
        if (state == STATE_LEADING_BRACKET) {
            onParenthesesOpen();

            // ignoring leading bracket
            if (parent != null) {
                return;
            }
        }

        SelectableTokenParser activeParser = this.activeParser;

        if (activeParser != null && !activeParser.shouldStayActive(ch)) {
            if (activeParser == nestedExpressionParser) {
                nestedExpressionParser = null;
            }

            onToken(activeParser.doFinal());

            this.activeParser = activeParser = null;
        }

        if (activeParser == null) {
            if (isControl(ch)) {
                return;
            }

            if (ch == ')') {
                state = STATE_FINAL_BRACKET;
                return;
            }

            this.activeParser = activeParser = initActiveParser(ch);
        }

        activeParser.update(ch);
    }

    private SelectableTokenParser initActiveParser(final int ch) {
        for (val parser : parsers) {
            if (parser.shouldActivate(ch)) {
                return parser;
            }
        }

        if (ch == '(') {
            if (nestedExpressionParser == null) {
                nestedExpressionParser = new DefaultExpressionStreamParser(parsers);
                nestedExpressionParser.parent = this;
            }

            return nestedExpressionParser;
        }

        throw new SyntaxException("Unexpected character " + (char) ch);
    }

    @Override
    public @NotNull Operand doFinal() {
        try {
            val nestedExpressionParser = this.nestedExpressionParser;

            if (nestedExpressionParser != null) {
                this.nestedExpressionParser = null;

                switch (nestedExpressionParser.state) {
                    case STATE_FINAL_BRACKET:
                        onToken(nestedExpressionParser.doFinal());
                        break;
                    case STATE_CONTENT:
                        throw new SyntaxException("Unexpected end, nested parentheses isn't closed");
                }
            }

            if (activeParser != null) {
                onToken(activeParser.doFinal());
                activeParser = null;
            }

            if (!operators.isEmpty()) {
                throw invalidSyntax("There are " + operators.size() + " remaining operators: " + operators);
            }

            solve(-1);

            if (operandStack.size() != 1) {
                throw invalidSyntax("Unexpected end of parser");
            }

            return operandStack.pop();
        } finally {
            operators.clear();
            operandStack.clear();
            operatorStack.clear();

            previousOperand = null;
            state = STATE_LEADING_BRACKET;
        }
    }

    private static boolean isControl(final int ch) {
        return ch <= 32;
    }

    @Override
    public void close() {
    }
}
