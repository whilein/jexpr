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
import io.github.whilein.jexpr.operator.OperatorRegistry;
import io.github.whilein.jexpr.token.FactoryContext;
import io.github.whilein.jexpr.token.NumberTokenParserFactory;
import io.github.whilein.jexpr.token.OperatorTokenParserFactory;
import io.github.whilein.jexpr.token.ReferenceTokenParserFactory;
import io.github.whilein.jexpr.token.SelectableTokenParserFactory;
import io.github.whilein.jexpr.token.StringTokenParserFactory;
import io.github.whilein.jexpr.util.Pool;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
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
            final @NotNull OperatorRegistry operatorRegistry,
            final @NotNull KeywordRegistry keywordRegistry
    ) {
        return new ConcurrentExpressionParser(new ExpressionStreamParserPool(Arrays.asList(
                new NumberTokenParserFactory(),
                new StringTokenParserFactory(),
                new OperatorTokenParserFactory(operatorRegistry),
                new ReferenceTokenParserFactory(keywordRegistry)
        )));
    }

    public static @NotNull ExpressionParser createDefault() {
        return createDefault(OperatorRegistry.createDefault(), KeywordRegistry.createDefault());
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

    @RequiredArgsConstructor
    private static final class PooledExpressionStreamParser implements ExpressionStreamParser {
        final Pool<ExpressionStreamParser> pool;

        @Delegate(types = ExpressionStreamParser.class, excludes = AutoCloseable.class)
        final ExpressionStreamParser delegate;

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
