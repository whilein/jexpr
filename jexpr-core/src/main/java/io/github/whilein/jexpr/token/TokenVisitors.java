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

package io.github.whilein.jexpr.token;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.Operator;
import io.github.whilein.jexpr.operator.UnaryOperator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author whilein
 */
@UtilityClass
public class TokenVisitors {

    public @NotNull TokenVisitor interceptOperand(
            final @NotNull Consumer<? super @NotNull Operand> operandInterceptor
    ) {
        return new InterceptingTokenVisitor(null, null, operandInterceptor);
    }

    public @NotNull TokenVisitor interceptUnaryOperator(
            final @NotNull Consumer<? super @NotNull UnaryOperator> unaryOperatorInterceptor
    ) {
        return new InterceptingTokenVisitor(unaryOperatorInterceptor, null, null);
    }

    public @NotNull TokenVisitor interceptBinaryOperand(
            final @NotNull Consumer<? super @NotNull BinaryOperator> binaryOperatorConsumer
    ) {
        return new InterceptingTokenVisitor(null, binaryOperatorConsumer, null);
    }

    public @NotNull TokenVisitor interceptOperator(
            final @NotNull Consumer<? super @NotNull Operator> operatorInterceptor
    ) {
        return new InterceptingTokenVisitor(operatorInterceptor, operatorInterceptor, null);
    }

    @FieldDefaults(makeFinal = true)
    @RequiredArgsConstructor
    private static final class InterceptingTokenVisitor implements TokenVisitor {

        Consumer<? super UnaryOperator> unaryOperatorInterceptor;

        Consumer<? super BinaryOperator> binaryOperatorInterceptor;

        Consumer<? super Operand> operandInterceptor;

        @Override
        public void visitOperand(final @NotNull Operand operand) {
            if (operandInterceptor != null) {
                operandInterceptor.accept(operand);
            }
        }

        @Override
        public void visitUnaryOperator(final @NotNull UnaryOperator unaryOperator) {
            if (unaryOperatorInterceptor != null) {
                unaryOperatorInterceptor.accept(unaryOperator);
            }
        }

        @Override
        public void visitBinaryOperator(final @NotNull BinaryOperator binaryOperator) {
            if (binaryOperatorInterceptor != null) {
                binaryOperatorInterceptor.accept(binaryOperator);
            }

        }
    }

}
