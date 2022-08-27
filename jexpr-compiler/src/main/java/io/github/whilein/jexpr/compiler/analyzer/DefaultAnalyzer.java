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

package io.github.whilein.jexpr.compiler.analyzer;

import io.github.whilein.jexpr.compiler.LocalMap;
import io.github.whilein.jexpr.compiler.operator.AsmOperatorRegistry;
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.defined.OperandBoolean;
import io.github.whilein.jexpr.operand.defined.OperandDouble;
import io.github.whilein.jexpr.operand.defined.OperandFloat;
import io.github.whilein.jexpr.operand.defined.OperandInteger;
import io.github.whilein.jexpr.operand.defined.OperandLong;
import io.github.whilein.jexpr.operand.defined.OperandString;
import io.github.whilein.jexpr.operand.undefined.OperandReference;
import io.github.whilein.jexpr.operand.undefined.OperandUndefinedMember;
import io.github.whilein.jexpr.operand.undefined.OperandUndefinedSequence;
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
public final class DefaultAnalyzer implements Analyzer {

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

    AsmOperatorRegistry asmOperatorRegistry;

    public static @NotNull Analyzer create(final @NotNull AsmOperatorRegistry asmOperatorRegistry) {
        return new DefaultAnalyzer(asmOperatorRegistry);
    }

    @Override
    public @NotNull AnalyzedOperand analyze(final @NotNull Operand operand, final @NotNull LocalMap map) {
        if (operand.isDefined()) {
            return new AnalyzedDefined(operand, TYPE_MAP.get(operand.getClass()));
        } else if (operand instanceof OperandReference) {
            return new AnalyzedReference(map.get((String) operand.getValue()));
        } else if (operand instanceof OperandUndefinedMember) {
            val member = (OperandUndefinedMember) operand;

            val operator = member.getOperator();
            val asmOperator = asmOperatorRegistry.getOperator(operator.getClass());

            val analyzedMember = analyze(member.getMember(), map);

            return new AnalyzedMember(
                    analyzedMember,
                    asmOperator,
                    asmOperator.getOutputType(analyzedMember.getType()));
        } else if (operand instanceof OperandUndefinedSequence) {
            val sequence = (OperandUndefinedSequence) operand;
            val left = analyze(sequence.getLeft(), map);
            val right = analyze(sequence.getRight(), map);

            val operator = sequence.getOperator();
            val asmOperator = asmOperatorRegistry.getOperator(operator.getClass());

            return new AnalyzedSequence(
                    left,
                    right,
                    asmOperator,
                    asmOperator.getOutputType(left.getType(), right.getType())
            );
        }

        throw new IllegalStateException(operand.getClass().getName());
    }

}
