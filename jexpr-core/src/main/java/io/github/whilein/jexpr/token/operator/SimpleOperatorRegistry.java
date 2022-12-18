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

package io.github.whilein.jexpr.token.operator;

import io.github.whilein.jexpr.api.registry.AbstractRegistry;
import io.github.whilein.jexpr.api.token.operator.Operator;
import io.github.whilein.jexpr.api.token.operator.OperatorMatcher;
import io.github.whilein.jexpr.api.token.operator.OperatorRegistry;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class SimpleOperatorRegistry<T extends Operator>
        extends AbstractRegistry<T, String>
        implements OperatorRegistry<T> {

    Map<Integer, NavigableMap<String, T>> firstCharacterOperatorMap = new HashMap<>();

    @Override
    protected void onRegister(T item) {
        firstCharacterOperatorMap.computeIfAbsent(firstCharacter(item), __ -> new TreeMap<>())
                .put(item.getValue(), item);
    }

    @Override
    protected void onUnregister(T item) {
        val firstCharacter = firstCharacter(item);

        val operators = firstCharacterOperatorMap.get(firstCharacter);

        if (operators != null) {
            operators.remove(item.getValue(), item);

            if (operators.isEmpty()) {
                firstCharacterOperatorMap.remove(firstCharacter);
            }
        }
    }

    private static int firstCharacter(Operator operator) {
        return operator.getValue().charAt(0);
    }

    @Override
    public boolean hasMatcher(final int ch) {
        return firstCharacterOperatorMap.containsKey(ch);
    }

    @Override
    public @NotNull OperatorMatcher<T> matchOperator(final int ch) {
        val operators = firstCharacterOperatorMap.get(ch);

        if (operators == null) {
            throw new IllegalStateException("No matchers for " + (char) ch);
        }

        return NavigableMapOperatorMatcher.create(new TreeMap<>(operators));
    }


}
