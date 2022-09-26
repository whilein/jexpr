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
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.Operator;
import io.github.whilein.jexpr.operator.UnaryOperator;
import io.github.whilein.jexpr.token.AbstractTokenParser;
import io.github.whilein.jexpr.token.SelectableTokenParser;
import io.github.whilein.jexpr.token.TokenVisitor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractExpressionStreamParser extends AbstractTokenParser
        implements ExpressionStreamParser, TokenVisitor {

    @NonFinal
    BinaryOperator binaryOperator;

    Deque<UnaryOperator> unaryOperators = new ArrayDeque<>();

    Deque<Operator> operatorStack = new ArrayDeque<>();

    Deque<Operand> operandStack = new ArrayDeque<>();

    List<SelectableTokenParser> parsers;

    @NonFinal
    SelectableTokenParser activeParser;

    @NonFinal
    Operand previousOperand;

    @NonFinal
    Operator previousOperator;

    @Override
    protected void writeSyntaxReport(final Map<String, Object> map) {
        map.put("activeParser", activeParser);
        map.put("parsers", parsers);
        map.put("previousOperand", previousOperand);
        map.put("previousOperator", previousOperator);
    }

    @Override
    public void visitUnaryOperator(final @NotNull UnaryOperator unaryOperator) {
        if (binaryOperator != null && !binaryOperator.isUnaryExpected(unaryOperator)) {
            throw new SyntaxException("Unexpected " + unaryOperator + " after " + binaryOperator);
        }

        this.unaryOperators.add(unaryOperator);
        this.previousOperator = unaryOperator;
    }

    @Override
    public void visitBinaryOperator(final @NotNull BinaryOperator binaryOperator) {
        if (this.binaryOperator != null) {
            throw invalidSyntax("Unexpected operator got: " + binaryOperator);
        }

        this.binaryOperator = binaryOperator;
        this.previousOperator = binaryOperator;
    }

    @Override
    public void visitOperand(final @NotNull Operand operand) {
        val binaryOperator = this.binaryOperator;

        if (previousOperand != null) {
            if (binaryOperator == null) {
                throw new SyntaxException("Unexpected operand got: " + operand);
            }

            addOperator(binaryOperator);
        }

        Operator unary;

        while ((unary = unaryOperators.poll()) != null) {
            operatorStack.push(unary);
        }

        addMember(operand);

        this.previousOperator = null;
        this.binaryOperator = null;
    }

    private void solve(final int minOperator) {
        Operator operator;

        while ((operator = operatorStack.peekFirst()) != null) {
            if (minOperator > operator.getPresence()) {
                break;
            }

            operatorStack.removeFirst();

            if (operator instanceof BinaryOperator) {
                val right = operandStack.pop();
                val left = operandStack.pop();

                addMember(left.apply(right, (BinaryOperator) operator));
            } else if (operator instanceof UnaryOperator) {
                addMember(operandStack.pop().apply((UnaryOperator) operator));
            }
        }
    }

    private void addOperator(final Operator operator) {
        solve(operator.getPresence());

        operatorStack.push(operator);
    }

    private void addMember(final Operand operand) {
        previousOperand = operand;
        operandStack.push(operand);
    }

    @Override
    public void update(final int ch) throws SyntaxException {
        SelectableTokenParser activeParser = this.activeParser;

        if (activeParser != null && !activeParser.shouldStaySelected(ch)) {
            activeParser.doFinal(this);
            this.activeParser = activeParser = null;
        }

        if (activeParser == null) {
            if (shouldIgnore(ch)) {
                return;
            }

            this.activeParser = activeParser = initActiveParser(ch);
        }

        activeParser.update(ch);
    }

    protected boolean shouldIgnore(final int ch) {
        return isControl(ch);
    }

    private SelectableTokenParser initActiveParser(final int ch) {
        for (val parser : parsers) {
            if (parser.shouldSelect(ch, previousOperand, previousOperator)) {
                return parser;
            }
        }

        if (ch == '(') {
            return new NestedExpressionStreamParser(parsers);
        }

        throw new SyntaxException("Unexpected character " + (char) ch);
    }

    @Override
    public void doFinal(final @NotNull TokenVisitor tokenVisitor) throws SyntaxException {
        tokenVisitor.visitOperand(doFinal());
    }

    @Override
    public @NotNull Operand doFinal() {
        try {
            val activeParser = this.activeParser;

            if (activeParser != null) {
                activeParser.doFinal(this);

                this.activeParser = null;
            }

            if (binaryOperator != null || !unaryOperators.isEmpty()) {
                throw invalidSyntax("Unexpected EOF");
            }

            solve(-1);

            if (operandStack.size() != 1) {
                throw invalidSyntax("No operands on stack");
            }

            return operandStack.pop();
        } finally {
            unaryOperators.clear();
            operandStack.clear();
            operatorStack.clear();

            binaryOperator = null;
            previousOperator = null;
            previousOperand = null;
        }
    }

    private static boolean isControl(final int ch) {
        return ch <= 32;
    }

    @Override
    public void close() {
    }
}
