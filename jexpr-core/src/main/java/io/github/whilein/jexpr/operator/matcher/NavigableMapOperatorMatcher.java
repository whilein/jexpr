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

package io.github.whilein.jexpr.operator.matcher;

import io.github.whilein.jexpr.operator.Operator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class NavigableMapOperatorMatcher<T extends Operator> implements OperatorMatcher<T> {

    NavigableMap<String, T> operatorMap;

    @NonFinal
    int position;

    public static <T extends Operator> @NotNull OperatorMatcher<T> create(
            final @NotNull NavigableMap<@NotNull String, T> operatorMap
    ) {
        return new NavigableMapOperatorMatcher<>(operatorMap);
    }

    @Override
    public void next(final int ch) {
        operatorMap.keySet().removeIf(operator -> operator.length() <= position || operator.charAt(position) != ch);
        position++;
    }

    @Override
    public boolean hasNext(final int ch) {
        for (val operator : operatorMap.keySet()) {
            if (position < operator.length() && operator.charAt(position) == ch) {
                return true;
            }
        }

        return false;
    }

    @Override
    public @NotNull Set<@NotNull T> getProbablyResults() {
        return Collections.unmodifiableSet(new HashSet<>(operatorMap.values()));
    }

    @Override
    public @Nullable T getMatchedResult() {
        val operator = operatorMap.firstEntry().getValue();

        return operator.getValue().length() != position ? null : operator;
    }

}
