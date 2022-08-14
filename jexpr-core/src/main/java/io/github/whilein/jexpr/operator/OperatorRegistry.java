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

import io.github.whilein.jexpr.operator.type.OperatorAnd;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseAnd;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseComplement;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseLeftShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseOr;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseRightShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseUnsignedRightShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseXor;
import io.github.whilein.jexpr.operator.type.OperatorDivide;
import io.github.whilein.jexpr.operator.type.OperatorEquals;
import io.github.whilein.jexpr.operator.type.OperatorGreater;
import io.github.whilein.jexpr.operator.type.OperatorLess;
import io.github.whilein.jexpr.operator.type.OperatorMinus;
import io.github.whilein.jexpr.operator.type.OperatorMultiply;
import io.github.whilein.jexpr.operator.type.OperatorNegate;
import io.github.whilein.jexpr.operator.type.OperatorNotEquals;
import io.github.whilein.jexpr.operator.type.OperatorOr;
import io.github.whilein.jexpr.operator.type.OperatorPlus;
import io.github.whilein.jexpr.operator.type.OperatorRemainder;
import io.github.whilein.jexpr.operator.type.OperatorStrictGreater;
import io.github.whilein.jexpr.operator.type.OperatorStrictLess;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OperatorRegistry {

    @Getter
    List<Operator> operators;

    Map<Integer, TreeMap<String, Operator>> firstCharacterOperatorMap;

    public boolean hasMatcher(final int ch) {
        return firstCharacterOperatorMap.containsKey(ch);
    }

    public @NotNull OperatorMatcher getMatcher(final int ch) {
        val operators = firstCharacterOperatorMap.get(ch);

        if (operators == null) {
            throw new IllegalStateException("No matchers for " + (char) ch);
        }

        return new OperatorMatcherImpl(new TreeMap<>(operators));
    }

    private static final Set<Operator> DEFAULT_OPERATORS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    new OperatorPlus(),
                    new OperatorMinus(),
                    new OperatorMultiply(),
                    new OperatorDivide(),
                    new OperatorRemainder(),
                    new OperatorBitwiseComplement(),
                    new OperatorBitwiseAnd(),
                    new OperatorBitwiseOr(),
                    new OperatorBitwiseXor(),
                    new OperatorBitwiseLeftShift(),
                    new OperatorBitwiseRightShift(),
                    new OperatorBitwiseUnsignedRightShift(),
                    new OperatorNegate(),
                    new OperatorOr(),
                    new OperatorAnd(),
                    new OperatorStrictGreater(),
                    new OperatorGreater(),
                    new OperatorStrictLess(),
                    new OperatorLess(),
                    new OperatorEquals(),
                    new OperatorNotEquals()
            ))
    );

    public static @NotNull Set<@NotNull Operator> getDefaultOperators() {
        return DEFAULT_OPERATORS;
    }

    public static @NotNull OperatorRegistry createDefault() {
        return create(DEFAULT_OPERATORS);
    }

    public static @NotNull OperatorRegistry create(final @NotNull Operator @NotNull ... operators) {
        return create(Arrays.asList(operators));
    }

    public static @NotNull OperatorRegistry create(final @NotNull Collection<@NotNull Operator> operators) {
        val sortedOperatorList = new ArrayList<>(operators);
        sortedOperatorList.sort(Comparator.comparingInt(operator -> operator.getValue().length()));

        val firstCharacterOperatorMap = sortedOperatorList.stream()
                .collect(Collectors.groupingBy(operator -> (int) operator.getValue().charAt(0),
                        Collectors.collectingAndThen(Collectors.toMap(Operator::getValue, Function.identity()),
                                TreeMap::new)));

        return new OperatorRegistry(
                Collections.unmodifiableList(sortedOperatorList),
                Collections.unmodifiableMap(firstCharacterOperatorMap)
        );
    }


}
