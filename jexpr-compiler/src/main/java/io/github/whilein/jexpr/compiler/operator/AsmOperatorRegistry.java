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
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorEquals;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorMinus;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorMultiply;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorNegate;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorOr;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorPlus;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorRemainder;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorUnaryMinus;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorUnaryPlus;
import io.github.whilein.jexpr.operator.BinaryOperator;
import io.github.whilein.jexpr.operator.UnaryOperator;
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
import io.github.whilein.jexpr.operator.type.OperatorUnaryMinus;
import io.github.whilein.jexpr.operator.type.OperatorUnaryPlus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.objectweb.asm.Opcodes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AsmOperatorRegistry {

    Map<Class<? extends UnaryOperator>, AsmUnaryOperator> unaryOperatorMap;
    Map<Class<? extends BinaryOperator>, AsmBinaryOperator> binaryOperatorMap;

    private static final Map<Class<? extends UnaryOperator>, AsmUnaryOperator> DEFAULT_UNARY_OPERATOR_MAP;
    private static final Map<Class<? extends BinaryOperator>, AsmBinaryOperator> DEFAULT_BINARY_OPERATOR_MAP;

    private static final AsmOperatorRegistry DEFAULT;

    static {
        val defaultUnaryOperatorMap = new HashMap<Class<? extends UnaryOperator>, AsmUnaryOperator>();
        defaultUnaryOperatorMap.put(OperatorUnaryMinus.class, new AsmOperatorUnaryMinus());
        defaultUnaryOperatorMap.put(OperatorUnaryPlus.class, new AsmOperatorUnaryPlus());
        defaultUnaryOperatorMap.put(OperatorBitwiseComplement.class, new AsmOperatorBitwiseComplement());
        defaultUnaryOperatorMap.put(OperatorNegate.class, new AsmOperatorNegate());

        val defaultBinaryOperatorMap = new HashMap<Class<? extends BinaryOperator>, AsmBinaryOperator>();
        defaultBinaryOperatorMap.put(OperatorMinus.class, new AsmOperatorMinus());
        defaultBinaryOperatorMap.put(OperatorRemainder.class, new AsmOperatorRemainder());
        defaultBinaryOperatorMap.put(OperatorMultiply.class, new AsmOperatorMultiply());
        defaultBinaryOperatorMap.put(OperatorDivide.class, new AsmOperatorDivide());
        defaultBinaryOperatorMap.put(OperatorPlus.class, new AsmOperatorPlus());
        defaultBinaryOperatorMap.put(OperatorBitwiseAnd.class, new AsmOperatorBitwiseAnd());
        defaultBinaryOperatorMap.put(OperatorBitwiseOr.class, new AsmOperatorBitwiseOr());
        defaultBinaryOperatorMap.put(OperatorBitwiseXor.class, new AsmOperatorBitwiseXor());
        defaultBinaryOperatorMap.put(OperatorBitwiseLeftShift.class, new AsmOperatorBitwiseLeftShift());
        defaultBinaryOperatorMap.put(OperatorBitwiseRightShift.class, new AsmOperatorBitwiseRightShift());
        defaultBinaryOperatorMap.put(OperatorBitwiseUnsignedRightShift.class, new AsmOperatorBitwiseUnsignedRightShift());

        defaultBinaryOperatorMap.put(OperatorGreater.class,
                new AsmOperatorCompare(Opcodes.IF_ICMPLT, Opcodes.IFLT, Opcodes.DCMPL, Opcodes.FCMPL));

        defaultBinaryOperatorMap.put(OperatorStrictGreater.class,
                new AsmOperatorCompare(Opcodes.IF_ICMPLE, Opcodes.IFLE, Opcodes.DCMPL, Opcodes.FCMPL));

        defaultBinaryOperatorMap.put(OperatorLess.class,
                new AsmOperatorCompare(Opcodes.IF_ICMPGT, Opcodes.IFGT, Opcodes.DCMPG, Opcodes.FCMPG));

        defaultBinaryOperatorMap.put(OperatorStrictLess.class,
                new AsmOperatorCompare(Opcodes.IF_ICMPGE, Opcodes.IFGE, Opcodes.DCMPG, Opcodes.FCMPG));

        defaultBinaryOperatorMap.put(OperatorAnd.class, new AsmOperatorAnd());
        defaultBinaryOperatorMap.put(OperatorOr.class, new AsmOperatorOr());

        defaultBinaryOperatorMap.put(OperatorEquals.class, new AsmOperatorEquals(false));
        defaultBinaryOperatorMap.put(OperatorNotEquals.class, new AsmOperatorEquals(true));

        DEFAULT_UNARY_OPERATOR_MAP = Collections.unmodifiableMap(defaultUnaryOperatorMap);
        DEFAULT_BINARY_OPERATOR_MAP = Collections.unmodifiableMap(defaultBinaryOperatorMap);
        DEFAULT = new AsmOperatorRegistry(DEFAULT_UNARY_OPERATOR_MAP, DEFAULT_BINARY_OPERATOR_MAP);
    }

    public static @Unmodifiable @NotNull Map<Class<? extends BinaryOperator>, AsmBinaryOperator> getDefaultBinaryOperatorMap() {
        return DEFAULT_BINARY_OPERATOR_MAP;
    }

    public static @Unmodifiable @NotNull Map<Class<? extends UnaryOperator>, AsmUnaryOperator> getDefaultUnaryOperatorMap() {
        return DEFAULT_UNARY_OPERATOR_MAP;
    }

    public static @NotNull AsmOperatorRegistry getDefault() {
        return DEFAULT;
    }

    public static @NotNull AsmOperatorRegistry create(
            final @NotNull Map<Class<? extends UnaryOperator>, AsmUnaryOperator> unaryOperatorMap,
            final @NotNull Map<Class<? extends BinaryOperator>, AsmBinaryOperator> binaryOperatorMap
    ) {
        return new AsmOperatorRegistry(
                Collections.unmodifiableMap(new HashMap<>(unaryOperatorMap)),
                Collections.unmodifiableMap(new HashMap<>(binaryOperatorMap))
        );
    }

    public @NotNull AsmUnaryOperator getUnaryOperator(final @NotNull Class<? extends UnaryOperator> type) {
        val operator = unaryOperatorMap.get(type);

        if (operator == null) {
            throw new IllegalArgumentException("No such unary operator for " + type.getName());
        }

        return operator;
    }

    public @NotNull AsmBinaryOperator getBinaryOperator(final @NotNull Class<? extends BinaryOperator> type) {
        val operator = binaryOperatorMap.get(type);

        if (operator == null) {
            throw new IllegalArgumentException("No such binary operator for " + type.getName());
        }

        return operator;
    }

}
