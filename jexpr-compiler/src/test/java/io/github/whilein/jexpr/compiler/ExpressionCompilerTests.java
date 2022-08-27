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
import io.github.whilein.jexpr.token.SequenceTokenParser;
import io.github.whilein.jexpr.token.TokenParser;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import sun.misc.Unsafe;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
final class ExpressionCompilerTests {

    String testType;

    static TokenParser tokenParser;
    static ExpressionParser expressionParser;
    static ExpressionCompilerFactory compilerFactory;

    @BeforeAll
    static void setup() {
        val asmOperatorRegistry = AsmOperatorRegistry.createDefault();

        tokenParser = SequenceTokenParser.createDefault(expressionParser = DefaultExpressionParser.create());
        compilerFactory = new DefaultExpressionCompilerFactory(DefaultAnalyzer.create(asmOperatorRegistry));
    }

    @BeforeEach
    void setup(final TestInfo testInfo) {
        testType = "io/github/whilein/jexpr/compiler/Test_" + testInfo.getTestMethod()
                .orElseThrow(RuntimeException::new)
                .getName();
    }

    @Test
    void testDifficultExpression() {
        val test = createTest("(a + b - (c - d)) * e",
                int.class, int.class, int.class,
                int.class, int.class, int.class);
        assertEquals((2 + 3 - (4 - 5)) * 6, call(test, 2, 3, 4, 5, 6));
    }

    @Test
    void testStrictGreaterFloat() {
        val test = createTest("a > b", boolean.class, float.class, int.class);
        assertEquals(false, call(test, 1F, 2));
        assertEquals(true, call(test, 2F, 1));
    }

    @Test
    void testStrictLessFloat() {
        val test = createTest("a < b", boolean.class, float.class, int.class);
        assertEquals(true, call(test, 1F, 2));
        assertEquals(false, call(test, 2F, 1));
    }

    @Test
    void testGreaterFloat() {
        val test = createTest("a >= b", boolean.class, float.class, int.class);
        assertEquals(false, call(test, 1F, 2));
        assertEquals(true, call(test, 2F, 1));
    }

    @Test
    void testLessFloat() {
        val test = createTest("a <= b", boolean.class, float.class, int.class);
        assertEquals(true, call(test, 1F, 2));
        assertEquals(false, call(test, 2F, 1));
    }

    @Test
    void testStrictGreaterDouble() {
        val test = createTest("a > b", boolean.class, double.class, int.class);
        assertEquals(false, call(test, 1D, 2));
        assertEquals(true, call(test, 2D, 1));
    }

    @Test
    void testStrictLessDouble() {
        val test = createTest("a < b", boolean.class, double.class, int.class);
        assertEquals(true, call(test, 1D, 2));
        assertEquals(false, call(test, 2D, 1));
    }

    @Test
    void testGreaterDouble() {
        val test = createTest("a >= b", boolean.class, double.class, int.class);
        assertEquals(false, call(test, 1D, 2));
        assertEquals(true, call(test, 2D, 1));
    }

    @Test
    void testLessDouble() {
        val test = createTest("a <= b", boolean.class, double.class, int.class);
        assertEquals(true, call(test, 1D, 2));
        assertEquals(false, call(test, 2D, 1));
    }

    @Test
    void testStrictGreaterLong() {
        val test = createTest("a > b", boolean.class, long.class, int.class);
        assertEquals(false, call(test, 1L, 2));
        assertEquals(true, call(test, 2L, 1));
    }

    @Test
    void testStrictLessLong() {
        val test = createTest("a < b", boolean.class, long.class, int.class);
        assertEquals(true, call(test, 1L, 2));
        assertEquals(false, call(test, 2L, 1));
    }

    @Test
    void testGreaterLong() {
        val test = createTest("a >= b", boolean.class, long.class, int.class);
        assertEquals(false, call(test, 1L, 2));
        assertEquals(true, call(test, 2L, 1));
    }

    @Test
    void testLessLong() {
        val test = createTest("a <= b", boolean.class, long.class, int.class);
        assertEquals(true, call(test, 1L, 2));
        assertEquals(false, call(test, 2L, 1));
    }

    @Test
    void testStrictGreaterInt() {
        val test = createTest("a > b", boolean.class, int.class, int.class);
        assertEquals(false, call(test, 1, 2));
        assertEquals(true, call(test, 2, 1));
    }

    @Test
    void testStrictLessInt() {
        val test = createTest("a < b", boolean.class, int.class, int.class);
        assertEquals(true, call(test, 1, 2));
        assertEquals(false, call(test, 2, 1));
    }

    @Test
    void testGreaterInt() {
        val test = createTest("a >= b", boolean.class, int.class, int.class);
        assertEquals(false, call(test, 1, 2));
        assertEquals(true, call(test, 2, 1));
    }

    @Test
    void testLessInt() {
        val test = createTest("a <= b", boolean.class, int.class, int.class);
        assertEquals(true, call(test, 1, 2));
        assertEquals(false, call(test, 2, 1));
    }

    @Test
    void testBitwiseShiftIntLong() {
        val test = createTest("a << 8L", int.class, int.class);
        assertEquals(32 << 8, call(test, 32));
    }

    @Test
    void testBitwiseShiftLongInt() {
        val test = createTest("8L >> a", long.class, int.class);
        assertEquals(8L >> 32, call(test, 32));
    }

    @Test
    void testBitwiseShiftLongLong() {
        val test = createTest("8L >>> a", long.class, long.class);
        assertEquals(8L >>> 4L, call(test, 4L));
    }

    @Test
    void testBitwise() {
        val test = createTest("~a | b & c ^ 123", long.class, long.class, int.class, int.class);
        assertEquals(~1L | 2 & 3 ^ 123, call(test, 1L, 2, 3));
    }

    @Test
    void testTwoOperand() {
        val test = createTest("a + b", double.class, double.class, int.class);
        assertEquals(5.5, call(test, 2.5, 3));
    }

    @Test
    void testOneOperand() {
        val test = createTest("-a", int.class, int.class);
        assertEquals(-15, call(test, 15));
    }

    @Test
    void testReference() {
        val test = createTest("a", int.class, int.class);
        assertEquals(10, call(test, 10));
    }

    @Test
    void testDefinedString() {
        val test = createTest("'Hello world!'", String.class);
        assertEquals("Hello world!", call(test));
    }

    @Test
    void testDefinedBoolean() {
        val test = createTest("true", boolean.class);
        assertEquals(true, call(test));
    }

    @Test
    void testDefinedLong() {
        val test = createTest("1L", long.class);
        assertEquals(1L, call(test));
    }

    @Test
    void testDefinedDouble() {
        val test = createTest("1.1d", double.class);
        assertEquals(1.1d, call(test));
    }

    @Test
    void testDefinedFloat() {
        val test = createTest("1.1f", float.class);
        assertEquals(1.1f, call(test));
    }

    @Test
    void testDefinedInteger() {
        val test = createTest("3", int.class);
        assertEquals(3, call(test));
    }

    @SneakyThrows
    private Object call(final Class<?> type, final Object... params) {
        for (val method : type.getDeclaredMethods()) {
            if (method.getName().equals("perform")) {
                return method.invoke(null, params);
            }
        }

        throw new IllegalStateException("Unable to find perform in " + type);
    }

    @SneakyThrows
    private Class<?> createTest(final String expression, final Class<?> returnType, final Class<?>... parameters) {
        val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, testType, null,
                "java/lang/Object", null);

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                Type.getMethodDescriptor(Type.getType(returnType), Arrays.stream(parameters)
                        .map(Type::getType)
                        .toArray(Type[]::new)), null, null);

        tokenParser.submit(expression);
        val result = expressionParser.getResult();

        val localMap = SimpleLocalMap.create();

        int index = 0;
        int local = 0;

        for (val param : parameters) {
            val paramType = Type.getType(param);
            localMap.add(String.valueOf((char) ('a' + index)), local, paramType);
            index++;
            local += paramType.getSize();
        }

        val compiler = compilerFactory.create(mv, localMap);
        compiler.compile(result);

        mv.visitInsn(Type.getType(returnType).getOpcode(Opcodes.IRETURN));
        mv.visitMaxs(0, 0);

        mv.visitEnd();
        cw.visitEnd();

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

        return ((Unsafe) unsafe.get(null))
                .defineClass(testType, bytes, 0, bytes.length, null, null);
    }

    private ClassWriter createTestClass() {
        val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, testType, null,
                "java/lang/Object", null);

        return cw;
    }

}