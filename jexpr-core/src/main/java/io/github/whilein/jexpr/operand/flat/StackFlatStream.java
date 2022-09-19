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

package io.github.whilein.jexpr.operand.flat;

import io.github.whilein.jexpr.UndefinedResolver;
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operator.BinaryLazyOperator;
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.UnaryOperator;
import io.github.whilein.jexpr.token.Token;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class StackFlatStream implements FlatStream {

    Deque<Token> tokenStack;

    public static @NotNull FlatStream create() {
        return new StackFlatStream(new ArrayDeque<>());
    }

    @Override
    public @NotNull Operand solve(final @NotNull UndefinedResolver resolver) {
        Token token;

        while ((token = tokenStack.poll()) != null) {
            if (token instanceof BinaryOperator) {
                val operator = (BinaryOperator) token;

                val left = solve(resolver).solve(resolver);
                val right = solve(resolver);

                if (operator instanceof BinaryLazyOperator) {
                    val lazyOperator = (BinaryLazyOperator) operator;

                    if (left.isPredicable(lazyOperator)) {
                        tokenStack.push(left.getPredictedResult(lazyOperator));
                        continue;
                    }
                }

                tokenStack.push(left.apply(right, operator));
            } else if (token instanceof UnaryOperator) {
                val operator = (UnaryOperator) token;

                val member = solve(resolver).solve(resolver);
                tokenStack.push(member.apply(operator));
            } else if (token instanceof Operand) {
                return ((Operand) token);
            } else {
                throw new UnsupportedOperationException();
            }
        }

        return (Operand) tokenStack.pop();
    }

    @Override
    public void put(final @NotNull Token token) {
        tokenStack.push(token);
    }


}
