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

import io.github.whilein.jexpr.keyword.KeywordRegistry;
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.UnaryOperator;
import io.github.whilein.jexpr.operator.registry.BinaryOperatorRegistry;
import io.github.whilein.jexpr.operator.registry.OperatorRegistry;
import io.github.whilein.jexpr.operator.registry.UnaryOperatorRegistry;
import io.github.whilein.jexpr.token.BinaryOperatorTokenParserFactory;
import io.github.whilein.jexpr.token.FactoryContext;
import io.github.whilein.jexpr.token.NumberTokenParserFactory;
import io.github.whilein.jexpr.token.ReferenceTokenParserFactory;
import io.github.whilein.jexpr.token.SelectableTokenParserFactory;
import io.github.whilein.jexpr.token.StringTokenParserFactory;
import io.github.whilein.jexpr.token.UnaryOperatorTokenParserFactory;
import io.github.whilein.jexpr.util.Pool;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConcurrentExpressionParser extends AbstractExpressionParser {

    Pool<ExpressionStreamParser> streamPool;

    public static @NotNull ExpressionParser createDefault(
            final @NotNull OperatorRegistry<UnaryOperator> unaryOperatorRegistry,
            final @NotNull OperatorRegistry<BinaryOperator> binaryOperatorRegistry,
            final @NotNull KeywordRegistry keywordRegistry
    ) {
        return new ConcurrentExpressionParser(new ExpressionStreamParserPool(Arrays.asList(
                new NumberTokenParserFactory(),
                new StringTokenParserFactory(),
                new UnaryOperatorTokenParserFactory(unaryOperatorRegistry),
                new BinaryOperatorTokenParserFactory(binaryOperatorRegistry),
                new ReferenceTokenParserFactory(keywordRegistry)
        )));
    }

    public static @NotNull ExpressionParser createDefault() {
        return createDefault(
                UnaryOperatorRegistry.getDefault(),
                BinaryOperatorRegistry.getDefault(),
                KeywordRegistry.createDefault()
        );
    }

    public static @NotNull ExpressionParser from(
            final @NotNull List<@NotNull SelectableTokenParserFactory> factories
    ) {
        return new ConcurrentExpressionParser(new ExpressionStreamParserPool(new ArrayList<>(factories)));
    }

    @Override
    public @NotNull ExpressionStreamParser getStream() {
        return streamPool.borrowObject();
    }

    @FieldDefaults(makeFinal = true)
    @RequiredArgsConstructor
    private static final class PooledExpressionStreamParser implements ExpressionStreamParser {
        Pool<ExpressionStreamParser> pool;

        ExpressionStreamParser delegate;

        @Override
        public @NotNull Operand doFinal() {
            return delegate.doFinal();
        }

        @Override
        public void update(final int ch) throws SyntaxException {
            delegate.update(ch);
        }

        @Override
        public void close() {
            pool.returnObject(this);
        }
    }

    @RequiredArgsConstructor
    private static final class ExpressionStreamParserPool extends Pool<ExpressionStreamParser> {

        final List<SelectableTokenParserFactory> tokenParserFactories;

        @Override
        protected ExpressionStreamParser createObject() {
            val ctx = new FactoryContext();

            val parsers = tokenParserFactories.stream()
                    .map(factory -> factory.create(ctx))
                    .collect(Collectors.toList());

            return new PooledExpressionStreamParser(this, DefaultExpressionStreamParser.create(parsers));
        }

    }

}
