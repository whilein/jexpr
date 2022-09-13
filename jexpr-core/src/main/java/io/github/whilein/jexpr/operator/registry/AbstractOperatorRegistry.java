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

package io.github.whilein.jexpr.operator.registry;

import io.github.whilein.jexpr.operator.Operator;
import io.github.whilein.jexpr.operator.matcher.NavigableMapOperatorMatcher;
import io.github.whilein.jexpr.operator.matcher.OperatorMatcher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOperatorRegistry<T extends Operator> implements OperatorRegistry<T> {

    @Getter
    List<T> operators;

    Map<Integer, NavigableMap<String, T>> firstCharacterOperatorMap;

    public AbstractOperatorRegistry(final Collection<T> operators) {
        val sortedOperatorList = new ArrayList<>(operators);
        sortedOperatorList.sort(Comparator.comparingInt(operator -> operator.getValue().length()));

        val firstCharacterOperatorMap = sortedOperatorList.stream()
                .collect(Collectors.groupingBy(operator -> (int) operator.getValue().charAt(0),
                        Collectors.collectingAndThen(Collectors.toMap(Operator::getValue, Function.identity()),
                                map -> Collections.unmodifiableNavigableMap(new TreeMap<>(map)))));

        this.operators = Collections.unmodifiableList(sortedOperatorList);
        this.firstCharacterOperatorMap = Collections.unmodifiableMap(firstCharacterOperatorMap);
    }

    @Override
    public boolean hasMatcher(final int ch) {
        return firstCharacterOperatorMap.containsKey(ch);
    }

    @Override
    public @NotNull OperatorMatcher<T> getMatcher(final int ch) {
        val operators = firstCharacterOperatorMap.get(ch);

        if (operators == null) {
            throw new IllegalStateException("No matchers for " + (char) ch);
        }

        return NavigableMapOperatorMatcher.create(new TreeMap<>(operators));
    }


}
