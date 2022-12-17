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

import io.github.whilein.jexpr.compiler.operand.TypedOperand;
import io.github.whilein.jexpr.compiler.operand.TypedOperandVariable;
import io.github.whilein.jexpr.compiler.operand.TypedOperandVisitor;
import io.github.whilein.jexpr.compiler.operator.AsmBinaryOperator;
import io.github.whilein.jexpr.compiler.operator.AsmUnaryOperator;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.ILOAD;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class SimpleOperandCompiler implements OperandCompiler, TypedOperandVisitor {

    AsmMethodCompiler asmMethodCompiler;

    @NonFinal
    OperandOrigin actualOrigin;

    public SimpleOperandCompiler(final AsmMethodCompiler asmMethodCompiler) {
        this.asmMethodCompiler = asmMethodCompiler;
    }

    public SimpleOperandCompiler(final MethodVisitor mv) {
        this(new AsmMethodCompiler(mv));
    }


    @Override
    public void compile(final @NotNull TypedOperand operand) {
        compile0(new RootOperandOrigin(), operand);

        asmMethodCompiler.endConcat();
    }

    @Override
    public void visitInt(int value) {
        asmMethodCompiler.writeInt(value);
    }

    @Override
    public void visitFloat(float value) {
        asmMethodCompiler.writeFloat(value);
    }

    @Override
    public void visitLong(long value) {
        asmMethodCompiler.writeLong(value);
    }

    @Override
    public void visitDouble(double value) {
        asmMethodCompiler.writeDouble(value);
    }

    @Override
    public void visitBoolean(boolean value) {
        asmMethodCompiler.writeBoolean(value);
    }

    @Override
    public void visitString(@NotNull String value) {
        asmMethodCompiler.writeString(value);
    }

    @Override
    public void visitReference(@NotNull Local local) {
        asmMethodCompiler.visitVarInsn(local.getType().getOpcode(ILOAD), local.getIndex());
    }

    @Override
    public void visitNull() {
        asmMethodCompiler.writeNull();
    }

    @Override
    public void visitBinary(@NotNull TypedOperand left, @NotNull TypedOperand right, @NotNull AsmBinaryOperator op, @NotNull Type type) {
        op.compile(asmMethodCompiler, actualOrigin,
                new StackLazyOperandImpl(left),
                new StackLazyOperandImpl(right));
    }

    @Override
    public void visitUnary(@NotNull TypedOperandVariable member, @NotNull AsmUnaryOperator op, @NotNull Type type) {
        op.compile(asmMethodCompiler, actualOrigin, new StackLazyOperandImpl(member));
    }

    private void compile0(final OperandOrigin origin, final TypedOperand operand) {
        val actualOrigin = this.actualOrigin;

        this.actualOrigin = origin;

        operand.accept(this);

        this.actualOrigin = actualOrigin;
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
