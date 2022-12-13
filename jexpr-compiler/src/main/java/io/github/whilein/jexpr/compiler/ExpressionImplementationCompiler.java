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

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.compiler.operand.SimpleTypedOperandResolver;
import io.github.whilein.jexpr.compiler.operand.TypedOperandResolver;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExpressionImplementationCompiler<T> {

    Operand operand;

    Class<T> type;

    Method abstractMethod;

    TypedOperandResolver typedOperandResolver;

    Parameter[] parameters;

    @NonFinal
    Class<?> returnType;

    @NonFinal
    ClassLoader classLoader;

    public static <T> @NotNull ExpressionImplementationCompiler<T> create(
            final @NotNull Operand operand,
            final @NotNull Class<T> type,
            final @NotNull TypedOperandResolver typedOperandResolver
    ) {
        val abstractMethod = getAbstractMethod(type);

        return new ExpressionImplementationCompiler<>(
                operand,
                type,
                abstractMethod,
                typedOperandResolver,
                Arrays.stream(abstractMethod.getParameters())
                        .map(parameter -> new Parameter(parameter.getName(), parameter.getType()))
                        .toArray(Parameter[]::new),
                abstractMethod.getReturnType(),
                ExpressionImplementationCompiler.class.getClassLoader()
        );
    }


    public static <T> @NotNull ExpressionImplementationCompiler<T> create(
            final @NotNull Operand operand,
            final @NotNull Class<T> type
    ) {
        return create(operand, type, SimpleTypedOperandResolver.getDefault());
    }

    private static Method getAbstractMethod(final Class<?> type) {
        if (!type.isInterface()) {
            throw new IllegalStateException("Type " + type.getName() + " is not interface!");
        }

        Method abstractMethod = null;

        for (val method : type.getMethods()) {
            if (Modifier.isAbstract(method.getModifiers())) {
                if (abstractMethod != null) {
                    throw new IllegalStateException("Type " + type.getName()
                            + " has more than one abstract methods");
                }

                abstractMethod = method;
            }
        }

        if (abstractMethod == null) {
            throw new IllegalStateException("Type " + type.getName() + " has no abstract methods");
        }

        return abstractMethod;
    }

    public @NotNull ExpressionImplementationCompiler<T> name(final int index, final @NotNull String name) {
        parameters[index].setName(name);
        return this;
    }

    public @NotNull ExpressionImplementationCompiler<T> returnType(final @NotNull Class<?> type) {
        returnType = type;
        return this;
    }

    public @NotNull ExpressionImplementationCompiler<T> type(final int index, final @NotNull Class<?> type) {
        parameters[index].setType(type);
        return this;
    }

    public @NotNull ExpressionImplementationCompiler<T> parameter(final int index,
                                                                  final @NotNull String name,
                                                                  final @NotNull Class<?> type) {
        val parameter = parameters[index];
        parameter.setType(type);
        parameter.setName(name);
        return this;
    }

    public @NotNull ExpressionImplementationCompiler<T> classLoader(final @NotNull ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    private String getDescriptor() {
        return Type.getMethodDescriptor(Type.getType(returnType), Arrays.stream(parameters)
                .map(Parameter::getAsmType)
                .toArray(Type[]::new));
    }

    private String getOriginalDescriptor() {
        return Type.getMethodDescriptor(abstractMethod);
    }

    public <U extends T> @NotNull U compile() {
        val interfaceType = Type.getType(this.type);
        val interfaceTypeName = interfaceType.getInternalName();
        val typeName = "io/github/whilein/jexpr/compiler/generated/ExpressionImplementation_" + this.type.getSimpleName();

        val classLoader = new OpenClassLoader(this.classLoader);

        val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC,
                typeName, null,
                "java/lang/Object", new String[]{interfaceTypeName});

        { // empty constructor
            val mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V",
                    null, null);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>",
                    "()V", false);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        val descriptor = getDescriptor();
        val originalDescriptor = getOriginalDescriptor();

        if (!descriptor.equals(originalDescriptor)) {
            { // bridge
                val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_BRIDGE,
                        abstractMethod.getName(), originalDescriptor, null, null);

                val compiler = new AsmMethodCompiler(mv);
                compiler.visitVarInsn(Opcodes.ALOAD, 0);

                int index = 1;

                for (val parameter : parameters) {
                    compiler.visitVarInsn(Opcodes.ALOAD, index++);

                    val from = parameter.getOriginalAsmType();
                    val to = parameter.getAsmType();

                    compiler.migrateType(classLoader, from, to);
                }

                compiler.visitMethodInsn(Opcodes.INVOKEVIRTUAL, typeName, abstractMethod.getName(),
                        descriptor, false);

                val originalReturnType = Type.getType(abstractMethod.getReturnType());

                compiler.migrateType(Type.getType(returnType), originalReturnType);

                mv.visitInsn(originalReturnType.getOpcode(Opcodes.IRETURN));

                mv.visitMaxs(0, 0);
                mv.visitEnd();
            }
        }

        { // abstract method implementation
            val mv = cw.visitMethod(Opcodes.ACC_PUBLIC, abstractMethod.getName(),
                    descriptor, null, null);

            val compiler = new AsmMethodCompiler(mv);

            val localMap = SimpleLocalMap.create();

            int local = 1;

            for (val parameter : parameters) {
                val parameterType = parameter.getAsmType();

                localMap.add(parameter.getName(), local, parameterType);
                local += parameterType.getSize();
            }

            val operand = typedOperandResolver.resolve(this.operand, localMap);

            val expressionCompiler = new DefaultExpressionCompiler(mv);
            expressionCompiler.compile(operand);

            val returnType = Type.getType(this.returnType);

            if (operand.getType() != null) {
                compiler.migrateType(classLoader, operand.getType(), returnType);
            }

            mv.visitInsn(returnType.getOpcode(Opcodes.IRETURN));

            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }


        cw.visitEnd();


        try {
            //noinspection unchecked
            return (U) classLoader.defineClass(typeName.replace('/', '.'), cw.toByteArray())
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Getter
    private static final class Parameter {

        @Setter
        private String name;

        private Class<?> type;

        private Type asmType;

        private final Class<?> originalType;

        private final Type originalAsmType;

        public Parameter(final String name, final Class<?> type) {
            setName(name);
            setType(type);

            this.originalType = this.type;
            this.originalAsmType = this.asmType;
        }

        public void setType(final Class<?> type) {
            this.type = type;
            this.asmType = Type.getType(type);
        }
    }

    private static final class OpenClassLoader extends ClassLoader {
        public OpenClassLoader(final ClassLoader parent) {
            super(parent);
        }

        public Class<?> defineClass(final String name, final byte[] data) {
            return defineClass(name, data, 0, data.length, null);
        }
    }
}
