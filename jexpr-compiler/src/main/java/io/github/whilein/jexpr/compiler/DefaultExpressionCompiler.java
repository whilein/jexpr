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

import io.github.whilein.jexpr.compiler.operand.Analyzer;
import io.github.whilein.jexpr.compiler.operand.TypedDefined;
import io.github.whilein.jexpr.compiler.operand.TypedMember;
import io.github.whilein.jexpr.compiler.operand.TypedOperand;
import io.github.whilein.jexpr.compiler.operand.TypedReference;
import io.github.whilein.jexpr.compiler.operand.TypedSequence;
import io.github.whilein.jexpr.operand.Operand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
        compile0(new RootOperandOrigin(), analyzer.analyze(operand, localMap));

        asmMethodCompiler.endConcat();
    }

    private void compile0(final OperandOrigin origin, final TypedOperand operand) {
        if (operand instanceof TypedDefined) {
            val defined = (TypedDefined) operand;
            asmMethodCompiler.writeDefinedOperand(defined.getValue());
        } else if (operand instanceof TypedReference) {
            val reference = (TypedReference) operand;

            val local = reference.getLocal();
            val localType = local.getType();

            asmMethodCompiler.visitVarInsn(localType.getOpcode(ILOAD), local.getIndex());
        } else if (operand instanceof TypedMember) {
            val member = (TypedMember) operand;
            val memberOperand = new StackLazyOperandImpl(member.getMember());

            val operator = member.getOperator();
            operator.compile(asmMethodCompiler, origin, memberOperand);
        } else if (operand instanceof TypedSequence) {
            val sequence = (TypedSequence) operand;

            val left = new StackLazyOperandImpl(sequence.getLeft());
            val right = new StackLazyOperandImpl(sequence.getRight());

            val operator = sequence.getOperator();
            operator.compile(asmMethodCompiler, origin, left, right);
        }
    }

    private static final class RootOperandOrigin implements OperandOrigin {
        @Getter
        @Setter
        boolean concatenated;
    }

    @FieldDefaults(makeFinal = true)
    @RequiredArgsConstructor
    private final class StackLazyOperandImpl implements StackLazyOperand {

        @Getter
        @Setter
        @NonFinal
        boolean concatenated;

        @Getter
        TypedOperand operand;

        @Override
        public @Nullable Type getType() {
            return operand.getType();
        }

        @Override
        public void load() {
            compile0(this, operand);
        }

    }

}
