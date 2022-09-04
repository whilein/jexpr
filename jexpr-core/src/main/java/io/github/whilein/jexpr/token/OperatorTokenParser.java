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
import io.github.whilein.jexpr.operator.OperatorMatcher;
import io.github.whilein.jexpr.operator.OperatorRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class OperatorTokenParser extends AbstractTokenParser {

    OperatorRegistry operatorRegistry;

    @NonFinal
    OperatorMatcher operatorMatcher;

    @Override
    protected void writeSyntaxReport(final Map<String, Object> map) {
        map.put("operatorMatcher", operatorMatcher);
    }

    @Override
    public boolean shouldStayActive(final int ch) {
        return operatorMatcher == null || operatorMatcher.hasNext(ch);
    }

    @Override
    public boolean shouldActivate(final int ch) {
        return operatorRegistry.hasMatcher(ch);
    }

    @Override
    public void update(final int ch) throws SyntaxException {
        if (operatorMatcher == null) {
            operatorMatcher = operatorRegistry.getMatcher(ch);
        }

        operatorMatcher.next(ch);
    }

    @Override
    public @NotNull Operator doFinal() throws SyntaxException {
        try {
            return operatorMatcher.getMatchedResult();
        } finally {
            operatorMatcher = null;
        }
    }
}
