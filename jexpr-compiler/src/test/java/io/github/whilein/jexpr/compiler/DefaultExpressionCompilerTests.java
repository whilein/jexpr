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

import io.github.whilein.jexpr.DefaultExpressionParser;
import io.github.whilein.jexpr.ExpressionParser;
import io.github.whilein.jexpr.compiler.analyzer.DefaultAnalyzer;
import io.github.whilein.jexpr.compiler.operator.AsmOperatorRegistry;
import io.github.whilein.jexpr.operand.defined.OperandBoolean;
import io.github.whilein.jexpr.operand.defined.OperandDouble;
import io.github.whilein.jexpr.operand.defined.OperandFloat;
import io.github.whilein.jexpr.operand.defined.OperandInteger;
import io.github.whilein.jexpr.operand.defined.OperandLong;
import io.github.whilein.jexpr.operand.defined.OperandString;
import io.github.whilein.jexpr.token.SequenceTokenParser;
import io.github.whilein.jexpr.token.TokenParser;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import sun.misc.Unsafe;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
final class DefaultExpressionCompilerTests {

    String testType;

    TokenParser tokenParser;
    ExpressionParser expressionParser;
    ExpressionCompilerFactory compilerFactory;

    @BeforeEach
    void setup(final TestInfo testInfo) {
        testType = "io/github/whilein/jexpr/compiler/Test_" + testInfo.getTestMethod()
                .orElseThrow(RuntimeException::new)
                .getName();

        val asmOperatorRegistry = AsmOperatorRegistry.createDefault();

        tokenParser = SequenceTokenParser.createDefault(expressionParser = DefaultExpressionParser.create());
        compilerFactory = new DefaultExpressionCompilerFactory(DefaultAnalyzer.create(asmOperatorRegistry));
    }

    @Test
    void testComplexSequence() {
        tokenParser.submit("(a + b - (c - d)) * e");
        val operand = expressionParser.getResult();

        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "(IIIII)I", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create()
                .add("a", 0, Type.INT_TYPE)
                .add("b", 1, Type.INT_TYPE)
                .add("c", 2, Type.INT_TYPE)
                .add("d", 3, Type.INT_TYPE)
                .add("e", 4, Type.INT_TYPE));

        compiler.compile(operand);

        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals(((10 + 15 - (3 - 123)) * 666), call(cw, 10, 15, 3, 123, 666));
    }

    @Test
    void testTwoOperand() {
        tokenParser.submit("-x - y ");

        val operand = expressionParser.getResult();

        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "(Ljava/lang/Double;I)D", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create()
                .add("x", 0, Type.getObjectType("java/lang/Double"))
                .add("y", 1, Type.INT_TYPE));

        compiler.compile(operand);

        mv.visitInsn(Opcodes.DRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals(-10D - 30, call(cw, 10D, 30));
    }

    @Test
    void testOneOperand() {
        tokenParser.submit("-x");

        val operand = expressionParser.getResult();

        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "(Ljava/lang/Integer;)I", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create()
                .add("x", 0, Type.getObjectType("java/lang/Integer")));

        compiler.compile(operand);

        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals(-10, call(cw, 10));
    }

    @Test
    void testReference() {
        tokenParser.submit("x");

        val operand = expressionParser.getResult();

        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "(I)I", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create()
                .add("x", 0, Type.INT_TYPE));

        compiler.compile(operand);

        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals(10, call(cw, 10));
    }

    @Test
    void testDefinedString() {
        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "()Ljava/lang/String;", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create());
        compiler.compile(OperandString.valueOf("Hello world!"));

        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals("Hello world!", call(cw));
    }

    @Test
    void testDefinedBoolean() {
        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "()Z", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create());
        compiler.compile(OperandBoolean.valueOf(true));

        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals(true, call(cw));
    }

    @Test
    void testDefinedLong() {
        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "()J", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create());
        compiler.compile(OperandLong.valueOf(1L));

        mv.visitInsn(Opcodes.LRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals(1L, call(cw));
    }

    @Test
    void testDefinedDouble() {
        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "()D", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create());
        compiler.compile(OperandDouble.valueOf(1.1d));

        mv.visitInsn(Opcodes.DRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals(1.1d, call(cw));
    }

    @Test
    void testDefinedFloat() {
        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "()F", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create());
        compiler.compile(OperandFloat.valueOf(1.1f));

        mv.visitInsn(Opcodes.FRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals(1.1f, call(cw));
    }

    @Test
    void testDefinedInteger() {
        val cw = createTestClass();

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                "()I", null, null);

        val compiler = compilerFactory.create(mv, SimpleLocalMap.create());
        compiler.compile(OperandInteger.valueOf(3));

        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        assertEquals(3, call(cw));
    }

    @SneakyThrows
    private Object call(final ClassWriter cw, final Object... params) {
        val bytes = cw.toByteArray();

        {
            val path = Paths.get(testType.replace('/', '.') + ".class");
            Files.write(path, bytes);

            val process = new ProcessBuilder("javap", "-v", path.toAbsolutePath().toString())
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .start();

            process.waitFor();

            Files.delete(path);
        }


        val unsafe = Unsafe.class.getDeclaredField("theUnsafe");
        unsafe.setAccessible(true);

        val type = ((Unsafe) unsafe.get(null))
                .defineClass(testType, bytes, 0, bytes.length, null, null);

        for (val method : type.getDeclaredMethods()) {
            if (method.getName().equals("perform")) {
                return method.invoke(null, params);
            }
        }

        throw new IllegalStateException("Unable to find perform in " + type);
    }

    private ClassWriter createTestClass() {
        val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, testType, null,
                "java/lang/Object", null);

        return cw;
    }

}