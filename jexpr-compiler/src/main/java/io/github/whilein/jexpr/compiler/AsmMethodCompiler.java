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

import io.github.whilein.jexpr.compiler.util.TypeUtils;
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.defined.OperandBoolean;
import io.github.whilein.jexpr.operand.defined.OperandNumber;
import io.github.whilein.jexpr.operand.defined.OperandObject;
import io.github.whilein.jexpr.operand.defined.OperandString;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.D2F;
import static org.objectweb.asm.Opcodes.D2I;
import static org.objectweb.asm.Opcodes.D2L;
import static org.objectweb.asm.Opcodes.F2D;
import static org.objectweb.asm.Opcodes.F2I;
import static org.objectweb.asm.Opcodes.F2L;
import static org.objectweb.asm.Opcodes.I2B;
import static org.objectweb.asm.Opcodes.I2C;
import static org.objectweb.asm.Opcodes.I2D;
import static org.objectweb.asm.Opcodes.I2F;
import static org.objectweb.asm.Opcodes.I2L;
import static org.objectweb.asm.Opcodes.I2S;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.L2D;
import static org.objectweb.asm.Opcodes.L2F;
import static org.objectweb.asm.Opcodes.L2I;
import static org.objectweb.asm.Opcodes.SIPUSH;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class AsmMethodCompiler extends MethodVisitor {

    AsmMethodCompiler(final MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    @NonFinal
    boolean concat;

    public void beginConcat() {
        if (!concat) {
            concat = true;

            visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");

            visitInsn(Opcodes.DUP);

            visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>",
                    "()V", false);
        }
    }

    public void concat(final Type type) {
        if (!concat) {
            throw new IllegalStateException("Cannot add " + type + " to concatenation, because concat = false");
        }

        val descriptor = type == null || (!TypeUtils.isPrimitive(type) && !type.equals(TypeUtils.STRING_TYPE))
                ? "(Ljava/lang/Object;)Ljava/lang/StringBuilder;"
                : "(" + type.getDescriptor() + ")Ljava/lang/StringBuilder;";

        visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", descriptor, false);
    }

    public void endConcat() {
        if (concat) {
            concat = false;

            visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString",
                    "()Ljava/lang/String;", false);
        }
    }

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

    public Type box(final Type type) {
        if (TypeUtils.isWrapper(type)) {
            return type;
        }

        if (!TypeUtils.isPrimitive(type)) {
            throw new IllegalStateException("Unable to unbox not primitive type");
        }

        val wrapper = TypeUtils.getWrapper(type);

        mv.visitMethodInsn(
                INVOKESTATIC, wrapper.getInternalName(), "valueOf",
                Type.getMethodDescriptor(wrapper, type),
                false
        );

        return wrapper;
    }

    public Type unbox(final Type type) {
        if (TypeUtils.isPrimitive(type)) {
            return type;
        }

        val method = getWrapperToPrimitiveMethod(type);

        val unboxType = TypeUtils.getPrimitive(type);

        mv.visitMethodInsn(
                INVOKEVIRTUAL, type.getInternalName(), method,
                Type.getMethodDescriptor(unboxType),
                false
        );

        return unboxType;
    }

    public void migrateType(final ClassLoader cl, Type from, final Type to) {
        if (from.equals(to)) {
            return;
        }

        if (TypeUtils.isPrimitive(to) && !TypeUtils.isPrimitive(from)) {
            from = unbox(from);
        } else if (TypeUtils.isPrimitive(from) && !TypeUtils.isPrimitive(to)) {
            from = box(from);
        }

        if (!from.equals(to)) {
            cast(cl, from, to);
        }
    }

    public void migrateType(final Type from, final Type to) {
        migrateType(AsmMethodCompiler.class.getClassLoader(), from, to);
    }

    public void cast(final Type from, final Type to) {
        cast(AsmMethodCompiler.class.getClassLoader(), from, to);
    }

    public void cast(final ClassLoader cl, final Type from, final Type to) {
        if (from.equals(to)) {
            return;
        }

        if (TypeUtils.isPrimitive(from) && TypeUtils.isPrimitiveNumber(to)) {
            switch (from.getSort()) {
                case Type.BOOLEAN:
                case Type.BYTE:
                case Type.SHORT:
                case Type.CHAR:
                case Type.INT:
                    switch (to.getSort()) {
                        case Type.FLOAT:
                            mv.visitInsn(I2F);
                            break;
                        case Type.DOUBLE:
                            mv.visitInsn(I2D);
                            break;
                        case Type.LONG:
                            mv.visitInsn(I2L);
                            break;
                    }
                    break;
                case Type.FLOAT:
                    switch (to.getSort()) {
                        case Type.BYTE:
                            mv.visitInsn(F2I);
                            mv.visitInsn(I2B);
                            break;
                        case Type.CHAR:
                            mv.visitInsn(F2I);
                            mv.visitInsn(I2C);
                            break;
                        case Type.SHORT:
                            mv.visitInsn(F2I);
                            mv.visitInsn(I2S);
                            break;
                        case Type.BOOLEAN:
                        case Type.INT:
                            mv.visitInsn(F2I);
                            break;
                        case Type.DOUBLE:
                            mv.visitInsn(F2D);
                            break;
                        case Type.LONG:
                            mv.visitInsn(F2L);
                            break;
                    }
                    break;
                case Type.DOUBLE:
                    switch (to.getSort()) {
                        case Type.BYTE:
                            mv.visitInsn(D2I);
                            mv.visitInsn(I2B);
                            break;
                        case Type.CHAR:
                            mv.visitInsn(D2I);
                            mv.visitInsn(I2C);
                            break;
                        case Type.SHORT:
                            mv.visitInsn(D2I);
                            mv.visitInsn(I2S);
                            break;
                        case Type.BOOLEAN:
                        case Type.INT:
                            mv.visitInsn(D2I);
                            break;
                        case Type.FLOAT:
                            mv.visitInsn(D2F);
                            break;
                        case Type.LONG:
                            mv.visitInsn(D2L);
                            break;
                    }
                    break;
                case Type.LONG:
                    switch (to.getSort()) {
                        case Type.BYTE:
                            mv.visitInsn(L2I);
                            mv.visitInsn(I2B);
                            break;
                        case Type.CHAR:
                            mv.visitInsn(L2I);
                            mv.visitInsn(I2C);
                            break;
                        case Type.SHORT:
                            mv.visitInsn(L2I);
                            mv.visitInsn(I2S);
                            break;
                        case Type.BOOLEAN:
                        case Type.INT:
                            mv.visitInsn(L2I);
                            break;
                        case Type.FLOAT:
                            mv.visitInsn(L2F);
                            break;
                        case Type.DOUBLE:
                            mv.visitInsn(L2D);
                            break;
                    }
                    break;
            }
        } else if (!TypeUtils.isPrimitive(from) && !TypeUtils.isPrimitive(to)) {
            try {
                val fromClass = cl.loadClass(from.getClassName());
                val toClass = cl.loadClass(to.getClassName());

                if (toClass.isAssignableFrom(fromClass)) {
                    return;
                }
            } catch (final ClassNotFoundException ignored) {
            }

            mv.visitTypeInsn(CHECKCAST, to.getSort() == Type.ARRAY ? to.getDescriptor() : to.getInternalName());
        } else {
            throw new IllegalStateException("Cannot cast " + from + " to " + to);
        }
    }

    public void writeDefinedOperand(final Operand operand) {
        if (!operand.isDefined()) {
            return;
        }

        val mv = this.mv;

        if (operand instanceof OperandNumber) {
            val number = operand.toNumber();

            if (number instanceof Integer) {
                val i = (int) number;

                if (i >= -1 && i <= 5) {
                    mv.visitInsn(Opcodes.ICONST_0 + i);
                    return;
                } else if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
                    mv.visitIntInsn(BIPUSH, i);
                    return;
                } else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
                    mv.visitIntInsn(SIPUSH, i);
                    return;
                }
            } else if (number instanceof Long) {
                val i = (long) number;

                if (i == 0) {
                    mv.visitInsn(Opcodes.LCONST_0);
                    return;
                } else if (i == 1) {
                    mv.visitInsn(Opcodes.LCONST_1);
                    return;
                }
            } else if (number instanceof Double) {
                val i = (double) number;

                if (i == 0) {
                    mv.visitInsn(Opcodes.DCONST_0);
                    return;
                } else if (i == 1) {
                    mv.visitInsn(Opcodes.DCONST_1);
                    return;
                }
            } else if (number instanceof Float) {
                val i = (float) number;

                if (i == 0) {
                    mv.visitInsn(Opcodes.FCONST_0);
                    return;
                } else if (i == 1) {
                    mv.visitInsn(Opcodes.FCONST_1);
                    return;
                } else if (i == 2) {
                    mv.visitInsn(Opcodes.FCONST_2);
                    return;
                }
            }

            mv.visitLdcInsn(number);
        } else if (operand instanceof OperandBoolean) {
            mv.visitInsn(operand.toBoolean() ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
        } else if (operand instanceof OperandString) {
            mv.visitLdcInsn(operand.toString());
        } else if (operand instanceof OperandObject) {
            mv.visitInsn(ACONST_NULL);
        } else {
            throw new UnsupportedOperationException("Unable to compile unknown operand: "
                    + operand.getClass().getName() + " (not in standard library?)");
        }
    }

}
