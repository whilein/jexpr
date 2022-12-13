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

package io.github.whilein.jexpr.compiler.operand;

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.compiler.LocalMap;
import io.github.whilein.jexpr.compiler.operator.AsmOperatorRegistry;
import io.github.whilein.jexpr.token.operand.constant.*;
import io.github.whilein.jexpr.token.operand.variable.OperandBinaryNode;
import io.github.whilein.jexpr.token.operand.variable.OperandReference;
import io.github.whilein.jexpr.token.operand.variable.OperandUnaryNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleTypedOperandResolver implements TypedOperandResolver {

    private static final Map<Class<? extends Operand>, Type> TYPE_MAP = new HashMap<Class<? extends Operand>, Type>() {
        {
            put(OperandString.class, Type.getType(String.class));
            put(OperandInteger.class, Type.INT_TYPE);
            put(OperandLong.class, Type.LONG_TYPE);
            put(OperandFloat.class, Type.FLOAT_TYPE);
            put(OperandDouble.class, Type.DOUBLE_TYPE);
            put(OperandBoolean.class, Type.BOOLEAN_TYPE);
        }
    };

    private static final TypedOperandResolver DEFAULT
            = new SimpleTypedOperandResolver(AsmOperatorRegistry.getDefault());

    AsmOperatorRegistry asmOperatorRegistry;

    public static @NotNull TypedOperandResolver create(final @NotNull AsmOperatorRegistry asmOperatorRegistry) {
        return new SimpleTypedOperandResolver(asmOperatorRegistry);
    }

    public static @NotNull TypedOperandResolver getDefault() {
        return DEFAULT;
    }

    @Override
    public @NotNull TypedOperand resolve(final @NotNull Operand operand, final @NotNull LocalMap map) {
        if (operand.isConstant()) {
            if (operand instanceof OperandObject) {
                if (operand.getValue() != null) {
                    throw new UnsupportedOperationException("Cannot compile object into bytecode");
                }

                return new TypedConstant(operand, null);
            }

            return new TypedConstant(operand, TYPE_MAP.get(operand.getClass()));
        } else if (operand instanceof OperandReference) {
            return new TypedReference(map.get((String) operand.getValue()));
        } else if (operand instanceof OperandUnaryNode) {
            val member = (OperandUnaryNode) operand;

            val operator = member.getOperator();
            val asmOperator = asmOperatorRegistry.getUnaryOperator(operator.getClass());

            val analyzedMember = resolve(member.getMember(), map);

            return new TypedUnary(
                    analyzedMember,
                    asmOperator,
                    asmOperator.getOutputType(analyzedMember.getType())
            );
        } else if (operand instanceof OperandBinaryNode) {
            val sequence = (OperandBinaryNode) operand;
            val left = resolve(sequence.getLeftMember(), map);
            val right = resolve(sequence.getRightMember(), map);

            val operator = sequence.getOperator();
            val asmOperator = asmOperatorRegistry.getBinaryOperator(operator.getClass());

            return new TypedBinary(
                    left,
                    right,
                    asmOperator,
                    asmOperator.getOutputType(left.getType(), right.getType())
            );
        }

        throw new IllegalStateException(operand.getClass().getName());
    }

}
