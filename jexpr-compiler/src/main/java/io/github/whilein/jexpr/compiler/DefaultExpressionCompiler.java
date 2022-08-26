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

package io.github.whilein.jexpr.compiler;

import io.github.whilein.jexpr.compiler.analyzer.AnalyzedDefined;
import io.github.whilein.jexpr.compiler.analyzer.AnalyzedMember;
import io.github.whilein.jexpr.compiler.analyzer.AnalyzedOperand;
import io.github.whilein.jexpr.compiler.analyzer.AnalyzedReference;
import io.github.whilein.jexpr.compiler.analyzer.AnalyzedSequence;
import io.github.whilein.jexpr.compiler.analyzer.Analyzer;
import io.github.whilein.jexpr.operand.Operand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.ILOAD;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class DefaultExpressionCompiler implements ExpressionCompiler {

    AsmMethodCompiler asmMethodCompiler;

    LocalMap localMap;

    Analyzer analyzer;

    private String getWrapperToPrimitiveMethod(final Type type) {
        switch (type.getInternalName()) {
            case "java/lang/Byte":
                return "byteValue";
            case "java/lang/Short":
                return "shortValue";
            case "java/lang/Float":
                return "floatValue";
            case "java/lang/Double":
                return "doubleValue";
            case "java/lang/Integer":
                return "intValue";
            case "java/lang/Long":
                return "longValue";
            case "java/lang/Character":
                return "charValue";
            case "java/lang/Boolean":
                return "booleanValue";
            default:
                throw new IllegalArgumentException("Unknown wrapper type: " + type);
        }
    }

    @Override
    public void compile(final @NotNull Operand operand) {
        val analyzedOperand = analyzer.analyze(operand, localMap);
        compile0(analyzedOperand);
    }

    private void compile0(final AnalyzedOperand operand) {
        if (operand instanceof AnalyzedDefined) {
            val defined = (AnalyzedDefined) operand;
            asmMethodCompiler.writeDefinedOperand(defined.getValue());
        } else if (operand instanceof AnalyzedReference) {
            val reference = (AnalyzedReference) operand;

            val local = reference.getLocal();
            val localType = local.getType();

            asmMethodCompiler.visitVarInsn(localType.getOpcode(ILOAD), local.getIndex());
        } else if (operand instanceof AnalyzedMember) {
            val member = (AnalyzedMember) operand;
            val memberOperand = new StackLazyOperandImpl(member.getMember());

            val operator = member.getOperator();
            operator.compile(asmMethodCompiler, memberOperand);
        } else if (operand instanceof AnalyzedSequence) {
            val sequence = (AnalyzedSequence) operand;

            val left = new StackLazyOperandImpl(sequence.getLeft());
            val right = new StackLazyOperandImpl(sequence.getRight());

            val operator = sequence.getOperator();
            operator.compile(asmMethodCompiler, left, right);
        }
    }

    @FieldDefaults(makeFinal = true)
    @RequiredArgsConstructor
    private final class StackLazyOperandImpl implements StackLazyOperand {

        @Getter
        AnalyzedOperand operand;

        @Override
        public @NotNull Type getType() {
            return operand.getType();
        }

        @Override
        public void load() {
            compile0(operand);
        }

    }

}
