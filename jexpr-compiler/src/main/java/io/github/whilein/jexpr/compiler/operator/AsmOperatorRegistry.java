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

package io.github.whilein.jexpr.compiler.operator;

import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorAnd;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorBitwiseAnd;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorBitwiseComplement;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorBitwiseLeftShift;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorBitwiseOr;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorBitwiseRightShift;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorBitwiseUnsignedRightShift;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorBitwiseXor;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorDivide;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorGreater;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorLess;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorMinus;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorMultiply;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorOr;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorPlus;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorRemainder;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorStrictGreater;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorStrictLess;
import io.github.whilein.jexpr.operator.Operator;
import io.github.whilein.jexpr.operator.type.OperatorAnd;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseAnd;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseComplement;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseLeftShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseOr;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseRightShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseUnsignedRightShift;
import io.github.whilein.jexpr.operator.type.OperatorBitwiseXor;
import io.github.whilein.jexpr.operator.type.OperatorDivide;
import io.github.whilein.jexpr.operator.type.OperatorGreater;
import io.github.whilein.jexpr.operator.type.OperatorLess;
import io.github.whilein.jexpr.operator.type.OperatorMinus;
import io.github.whilein.jexpr.operator.type.OperatorMultiply;
import io.github.whilein.jexpr.operator.type.OperatorOr;
import io.github.whilein.jexpr.operator.type.OperatorPlus;
import io.github.whilein.jexpr.operator.type.OperatorRemainder;
import io.github.whilein.jexpr.operator.type.OperatorStrictGreater;
import io.github.whilein.jexpr.operator.type.OperatorStrictLess;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AsmOperatorRegistry {

    Map<Class<? extends Operator>, AsmOperator> operatorMap;

    private static final Map<Class<? extends Operator>, AsmOperator> DEFAULT_OPERATOR_MAP;

    static {
        val defaultOperatorMap = new HashMap<Class<? extends Operator>, AsmOperator>();
        defaultOperatorMap.put(OperatorMinus.class, new AsmOperatorMinus());
        defaultOperatorMap.put(OperatorRemainder.class, new AsmOperatorRemainder());
        defaultOperatorMap.put(OperatorMultiply.class, new AsmOperatorMultiply());
        defaultOperatorMap.put(OperatorDivide.class, new AsmOperatorDivide());
        defaultOperatorMap.put(OperatorPlus.class, new AsmOperatorPlus());
        defaultOperatorMap.put(OperatorBitwiseAnd.class, new AsmOperatorBitwiseAnd());
        defaultOperatorMap.put(OperatorBitwiseOr.class, new AsmOperatorBitwiseOr());
        defaultOperatorMap.put(OperatorBitwiseXor.class, new AsmOperatorBitwiseXor());
        defaultOperatorMap.put(OperatorBitwiseLeftShift.class, new AsmOperatorBitwiseLeftShift());
        defaultOperatorMap.put(OperatorBitwiseRightShift.class, new AsmOperatorBitwiseRightShift());
        defaultOperatorMap.put(OperatorBitwiseUnsignedRightShift.class, new AsmOperatorBitwiseUnsignedRightShift());
        defaultOperatorMap.put(OperatorBitwiseComplement.class, new AsmOperatorBitwiseComplement());
        defaultOperatorMap.put(OperatorGreater.class, new AsmOperatorGreater());
        defaultOperatorMap.put(OperatorStrictGreater.class, new AsmOperatorStrictGreater());
        defaultOperatorMap.put(OperatorLess.class, new AsmOperatorLess());
        defaultOperatorMap.put(OperatorStrictLess.class, new AsmOperatorStrictLess());
        defaultOperatorMap.put(OperatorAnd.class, new AsmOperatorAnd());
        defaultOperatorMap.put(OperatorOr.class, new AsmOperatorOr());

        DEFAULT_OPERATOR_MAP = Collections.unmodifiableMap(defaultOperatorMap);
    }

    public static @Unmodifiable @NotNull Map<Class<? extends Operator>, AsmOperator> getDefaultOperatorMap() {
        return DEFAULT_OPERATOR_MAP;
    }

    public static @NotNull AsmOperatorRegistry createDefault() {
        return new AsmOperatorRegistry(DEFAULT_OPERATOR_MAP);
    }

    public static @NotNull AsmOperatorRegistry create(
            final @NotNull Map<Class<? extends Operator>, AsmOperator> operatorCompilerMap
    ) {
        return new AsmOperatorRegistry(Collections.unmodifiableMap(new HashMap<>(operatorCompilerMap)));
    }

    public @NotNull AsmOperator getOperator(final @NotNull Class<? extends Operator> type) {
        val operator = operatorMap.get(type);

        if (operator == null) {
            throw new IllegalArgumentException("No such operator compiler for operator " + type.getName());
        }

        return operator;
    }

}
