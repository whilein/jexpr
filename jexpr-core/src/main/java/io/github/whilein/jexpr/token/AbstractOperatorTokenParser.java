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

import io.github.whilein.jexpr.SyntaxException;
import io.github.whilein.jexpr.operator.Operator;
import io.github.whilein.jexpr.operator.matcher.OperatorMatcher;
import io.github.whilein.jexpr.operator.registry.OperatorRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOperatorTokenParser<T extends Operator>
        extends AbstractTokenParser
        implements SelectableTokenParser {

    OperatorRegistry<T> operatorRegistry;

    @NonFinal
    OperatorMatcher<T> operatorMatcher;

    @Override
    protected void writeSyntaxReport(final Map<String, Object> map) {
        map.put("operatorMatcher", operatorMatcher);
    }

    @Override
    public boolean shouldStaySelected(final int ch) {
        return operatorMatcher == null || operatorMatcher.hasNext(ch);
    }

    @Override
    public void update(final int ch) throws SyntaxException {
        if (operatorMatcher == null) {
            operatorMatcher = operatorRegistry.getMatcher(ch);
        }

        operatorMatcher.next(ch);
    }

    protected abstract void doVisit(TokenVisitor visitor, T operator);


    @Override
    public void doFinal(final @NotNull TokenVisitor tokenVisitor) throws SyntaxException {
        try {
            val result = operatorMatcher.getMatchedResult();

            if (result == null) {
                throw invalidSyntax("Unknown operator got, do you mean: " + operatorMatcher.getProbablyResults().stream()
                        .map(Operator::getValue)
                        .collect(Collectors.joining("', '", "'", "'")) + "?");
            }

            doVisit(tokenVisitor, result);
        } finally {
            operatorMatcher = null;
        }
    }
}
