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

import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.type.OperatorAnd;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseAnd;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseLeftShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseOr;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseRightShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseUnsignedRightShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseXor;
import io.github.whilein.jexpr.operator.type.OperatorDivide;
import io.github.whilein.jexpr.operator.type.OperatorEquals;
import io.github.whilein.jexpr.operator.type.OperatorGreater;
import io.github.whilein.jexpr.operator.type.OperatorLess;
import io.github.whilein.jexpr.operator.type.OperatorMemberSelection;
import io.github.whilein.jexpr.operator.type.OperatorMinus;
import io.github.whilein.jexpr.operator.type.OperatorMultiply;
import io.github.whilein.jexpr.operator.type.OperatorNotEquals;
import io.github.whilein.jexpr.operator.type.OperatorOr;
import io.github.whilein.jexpr.operator.type.OperatorPlus;
import io.github.whilein.jexpr.operator.type.OperatorRemainder;
import io.github.whilein.jexpr.operator.type.OperatorStrictGreater;
import io.github.whilein.jexpr.operator.type.OperatorStrictLess;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author whilein
 */
public final class BinaryOperatorRegistry extends AbstractOperatorRegistry<BinaryOperator> {

    private static final Set<BinaryOperator> DEFAULT_OPERATORS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    new OperatorPlus(),
                    new OperatorMinus(),
                    new OperatorMultiply(),
                    new OperatorDivide(),
                    new OperatorRemainder(),
                    new OperatorBitwiseAnd(),
                    new OperatorBitwiseOr(),
                    new OperatorBitwiseXor(),
                    new OperatorBitwiseLeftShift(),
                    new OperatorBitwiseRightShift(),
                    new OperatorBitwiseUnsignedRightShift(),
                    new OperatorOr(),
                    new OperatorAnd(),
                    new OperatorStrictGreater(),
                    new OperatorGreater(),
                    new OperatorStrictLess(),
                    new OperatorLess(),
                    new OperatorEquals(),
                    new OperatorNotEquals(),
                    new OperatorMemberSelection()
            ))
    );

    private static final BinaryOperatorRegistry DEFAULT = new BinaryOperatorRegistry(DEFAULT_OPERATORS);

    private BinaryOperatorRegistry(
            final Collection<BinaryOperator> operators
    ) {
        super(operators);
    }

    public static @NotNull Set<@NotNull BinaryOperator> getDefaultOperators() {
        return DEFAULT_OPERATORS;
    }

    public static @NotNull OperatorRegistry<BinaryOperator> getDefault() {
        return DEFAULT;
    }

    public static @NotNull OperatorRegistry<BinaryOperator> create(
            final @NotNull BinaryOperator @NotNull ... operators
    ) {
        return new BinaryOperatorRegistry(Arrays.asList(operators));
    }

    public static @NotNull OperatorRegistry<BinaryOperator> create(
            final @NotNull Collection<@NotNull BinaryOperator> operators
    ) {
        return new BinaryOperatorRegistry(operators);
    }

}
