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

import io.github.whilein.jexpr.io.ByteArrayOutput;
import io.github.whilein.jexpr.keyword.KeywordRegistry;
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.UnaryOperator;
import io.github.whilein.jexpr.operator.registry.BinaryOperatorRegistry;
import io.github.whilein.jexpr.operator.registry.OperatorRegistry;
import io.github.whilein.jexpr.operator.registry.UnaryOperatorRegistry;
import io.github.whilein.jexpr.token.BinaryOperatorTokenParser;
import io.github.whilein.jexpr.token.FactoryContext;
import io.github.whilein.jexpr.token.NumberTokenParser;
import io.github.whilein.jexpr.token.ReferenceTokenParser;
import io.github.whilein.jexpr.token.SelectableTokenParser;
import io.github.whilein.jexpr.token.SelectableTokenParserFactory;
import io.github.whilein.jexpr.token.StringTokenParser;
import io.github.whilein.jexpr.token.UnaryOperatorTokenParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleExpressionParser extends AbstractExpressionParser {

    @Getter
    ExpressionStreamParser stream;

    public static @NotNull ExpressionParser createDefault(
            final @NotNull OperatorRegistry<UnaryOperator> unaryOperatorRegistry,
            final @NotNull OperatorRegistry<BinaryOperator> binaryOperatorRegistry,
            final @NotNull KeywordRegistry keywordRegistry
    ) {
        val buffer = new ByteArrayOutput();

        return create(Arrays.asList(
                new NumberTokenParser(buffer),
                new StringTokenParser(buffer),
                new UnaryOperatorTokenParser(unaryOperatorRegistry),
                new BinaryOperatorTokenParser(binaryOperatorRegistry),
                new ReferenceTokenParser(keywordRegistry, buffer)
        ));
    }

    public static @NotNull ExpressionParser createDefault() {
        return createDefault(UnaryOperatorRegistry.getDefault(), BinaryOperatorRegistry.getDefault(),
                KeywordRegistry.createDefault());
    }

    public static @NotNull ExpressionParser from(
            final @NotNull List<@NotNull SelectableTokenParserFactory> factories
    ) {
        val context = new FactoryContext();

        return create(factories.stream()
                .map(factory -> factory.create(context))
                .collect(Collectors.toList()));
    }

    public static @NotNull ExpressionParser create(
            final @NotNull List<@NotNull SelectableTokenParser> parsers
    ) {
        return new SimpleExpressionParser(DefaultExpressionStreamParser.create(parsers));
    }

}
