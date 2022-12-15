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

import io.github.whilein.jexpr.api.Jexpr;
import io.github.whilein.jexpr.api.keyword.KeywordRegistry;
import io.github.whilein.jexpr.api.token.SelectableTokenParser;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.OperatorRegistry;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.io.ByteArrayOutput;
import io.github.whilein.jexpr.keyword.DefaultKeywordRegistry;
import io.github.whilein.jexpr.token.*;
import io.github.whilein.jexpr.token.operator.DefaultBinaryOperatorRegistry;
import io.github.whilein.jexpr.token.operator.DefaultUnaryOperatorRegistry;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class DefaultJexpr extends AbstractJexpr {

    private DefaultJexpr(
            OperatorRegistry<BinaryOperator> binaryOperatorRegistry,
            OperatorRegistry<UnaryOperator> unaryOperatorRegistry,
            KeywordRegistry keywordRegistry,
            List<SelectableTokenParser> parsers
    ) {
        super(binaryOperatorRegistry, unaryOperatorRegistry, keywordRegistry, parsers);
    }

    public static @NotNull Jexpr create() {
        val buffer = new ByteArrayOutput(8192);

        val binaryOperatorRegistry = new DefaultBinaryOperatorRegistry();
        val unaryOperatorRegistry = new DefaultUnaryOperatorRegistry();
        val keywordRegistry = new DefaultKeywordRegistry();

        return new DefaultJexpr(binaryOperatorRegistry, unaryOperatorRegistry, keywordRegistry, Arrays.asList(
                new NumberTokenParser(buffer),
                new StringTokenParser(buffer),
                new UnaryOperatorTokenParser(unaryOperatorRegistry),
                new BinaryOperatorTokenParser(binaryOperatorRegistry),
                new ReferenceTokenParser(keywordRegistry, buffer)
        ));
    }


}
