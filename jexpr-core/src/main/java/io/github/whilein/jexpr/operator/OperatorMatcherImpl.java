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

package io.github.whilein.jexpr.operator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
final class OperatorMatcherImpl implements OperatorMatcher {

    TreeMap<String, Operator> operatorValues;

    @NonFinal
    int position;

    @Override
    public void next(final int ch) {
        operatorValues.keySet().removeIf(operator -> operator.length() <= position || operator.charAt(position) != ch);
        position++;
    }

    @Override
    public boolean hasNext(final int ch) {
        for (val operator : operatorValues.keySet()) {
            if (position < operator.length() && operator.charAt(position) == ch) {
                return true;
            }
        }

        return false;
    }

    @Override
    public @NotNull Operator getMatchedResult() {
        return operatorValues.firstEntry().getValue();
    }

}
