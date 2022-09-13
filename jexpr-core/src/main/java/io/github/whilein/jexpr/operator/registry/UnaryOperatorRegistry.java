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

import io.github.whilein.jexpr.operator.UnaryOperator;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseComplement;
import io.github.whilein.jexpr.operator.type.OperatorNegate;
import io.github.whilein.jexpr.operator.type.OperatorUnaryMinus;
import io.github.whilein.jexpr.operator.type.OperatorUnaryPlus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author whilein
 */
public final class UnaryOperatorRegistry extends AbstractOperatorRegistry<UnaryOperator> {

    private static final Set<UnaryOperator> DEFAULT_OPERATORS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    new OperatorUnaryPlus(),
                    new OperatorUnaryMinus(),
                    new OperatorBitwiseComplement(),
                    new OperatorNegate()
            ))
    );

    private static final UnaryOperatorRegistry DEFAULT = new UnaryOperatorRegistry(DEFAULT_OPERATORS);

    private UnaryOperatorRegistry(
            final Collection<UnaryOperator> operators
    ) {
        super(operators);
    }

    public static @NotNull Set<@NotNull UnaryOperator> getDefaultOperators() {
        return DEFAULT_OPERATORS;
    }

    public static @NotNull OperatorRegistry<UnaryOperator> getDefault() {
        return DEFAULT;
    }

    public static @NotNull OperatorRegistry<UnaryOperator> create(
            final @NotNull UnaryOperator @NotNull ... operators
    ) {
        return new UnaryOperatorRegistry(Arrays.asList(operators));
    }

    public static @NotNull OperatorRegistry<UnaryOperator> create(
            final @NotNull Collection<@NotNull UnaryOperator> operators
    ) {
        return new UnaryOperatorRegistry(operators);
    }

}
