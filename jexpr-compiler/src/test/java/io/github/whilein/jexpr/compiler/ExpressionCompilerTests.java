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

import io.github.whilein.jexpr.ExpressionParser;
import io.github.whilein.jexpr.SimpleExpressionParser;
import io.github.whilein.jexpr.compiler.operand.SimpleTypedOperandResolver;
import io.github.whilein.jexpr.compiler.operand.TypedOperandResolver;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author whilein
 */
final class ExpressionCompilerTests {

    String testType;

    static ExpressionParser expressionParser;
    static TypedOperandResolver typedOperandResolver;

    @BeforeAll
    static void setup() {
        expressionParser = SimpleExpressionParser.createDefault();
        typedOperandResolver = SimpleTypedOperandResolver.getDefault();
    }

    @BeforeEach
    void setup(final TestInfo testInfo) {
        testType = "io/github/whilein/jexpr/compiler/Test_" + testInfo.getTestMethod()
                .orElseThrow(RuntimeException::new)
                .getName();
    }

    @Test
    void testConcatenation1() {
        val test = createTest("(a + ';' + (5 == b) + ';' + c) == (a + ';' + (b == 5) + ';' + c)",
                boolean.class, String.class, int.class, String.class);
        assertEquals(true, call(test, "Text", 5, "text"));
    }

    @Test
    void testConcatenation0() {
        val test = createTest("a + b",
                String.class, String.class, String.class);
        assertEquals("Hello world!", call(test, "Hello ", "world!"));
    }

    @Test
    void testConcatenation2() {
        val test = createTest("a == (b + c)",
                boolean.class, String.class, String.class, String.class);
        assertEquals(true, call(test, "123321", "123", "321"));
    }

    @Test
    void testDifficultArithmeticalExpression() {
        val test = createTest("(a + b - (c - d)) * e",
                int.class, int.class, int.class,
                int.class, int.class, int.class);
        assertEquals((2 + 3 - (4 - 5)) * 6, call(test, 2, 3, 4, 5, 6));
    }

    @Test
    void testDifficultLogicalExpression() {
        val test = createTest("!(a != b) || a + b > c && d",
                boolean.class, int.class, int.class,
                int.class, boolean.class);
        assertEquals(true, call(test, 10, 20, 15, true));
    }

    @Test
    void testEquals() {
        val test = createTest("a == b", boolean.class, int.class, int.class);
        assertEquals(true, call(test, 1, 1));
        assertEquals(false, call(test, 1, 2));
    }

    @Test
    void testAnd() {
        val test = createTest("a && b", boolean.class, boolean.class, boolean.class);
        assertEquals(true, call(test, true, true));
        assertEquals(false, call(test, false, true));
        assertEquals(false, call(test, true, false));
        assertEquals(false, call(test, false, false));
    }

    @Test
    void testOr() {
        val test = createTest("a || b", boolean.class, boolean.class, boolean.class);
        assertEquals(true, call(test, true, true));
        assertEquals(true, call(test, false, true));
        assertEquals(true, call(test, true, false));
        assertEquals(false, call(test, false, false));
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
    void testConstantString() {
        val test = createTest("'Hello world!'", String.class);
        assertEquals("Hello world!", call(test));
    }

    @Test
    void testConstantBoolean() {
        val test = createTest("true", boolean.class);
        assertEquals(true, call(test));
    }

    @Test
    void testConstantLong() {
        val test = createTest("1L", long.class);
        assertEquals(1L, call(test));
    }

    @Test
    void testConstantDouble() {
        val test = createTest("1.1d", double.class);
        assertEquals(1.1d, call(test));
    }

    @Test
    void testConstantFloat() {
        val test = createTest("1.1f", float.class);
        assertEquals(1.1f, call(test));
    }

    @Test
    void testConstantInteger() {
        val test = createTest("3", int.class);
        assertEquals(3, call(test));
    }

    @Test
    void testConstantNull() {
        val test = createTest("null", Object.class);
        assertNull(call(test));
    }

    @Test
    void testVariableCompare() {
        val test = createTest("a == b", boolean.class, Object.class, Object.class);
        assertEquals(true, call(test, 1, 1));
        assertEquals(false, call(test, 1, 2));
    }

    @Test
    void testVariableNullCheck() {
        val test = createTest("a != null", boolean.class, List.class);
        assertEquals(false, call(test, new ArrayList<>()));
        assertEquals(true, call(test, (Object) null));
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
        val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, testType, null,
                "java/lang/Object", null);

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "perform",
                Type.getMethodDescriptor(Type.getType(returnType), Arrays.stream(parameters)
                        .map(Type::getType)
                        .toArray(Type[]::new)), null, null);

        val result = expressionParser.parse(expression);

        val localMap = SimpleLocalMap.create();

        int index = 0;
        int local = 0;

        for (val param : parameters) {
            val paramType = Type.getType(param);
            localMap.add(String.valueOf((char) ('a' + index)), local, paramType);
            index++;
            local += paramType.getSize();
        }

        val compiler = new DefaultExpressionCompiler(mv);
        compiler.compile(typedOperandResolver.resolve(result, localMap));

        mv.visitInsn(Type.getType(returnType).getOpcode(Opcodes.IRETURN));
        mv.visitMaxs(0, 0);

        mv.visitEnd();
        cw.visitEnd();

        val bytes = cw.toByteArray();

//        {
//            val path = Paths.get(testType.replace('/', '.') + ".class");
//            Files.write(path, bytes);
//
//            val process = new ProcessBuilder("javap", "-v", path.toAbsolutePath().toString())
//                    .redirectError(ProcessBuilder.Redirect.INHERIT)
//                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
//                    .start();
//
//            process.waitFor();
//
//            Files.delete(path);
//        }


        val classLoader = new TestClassLoader(ExpressionCompilerTests.class.getClassLoader());
        return classLoader.defineClass(testType.replace('/', '.'), bytes);
    }

    private static final class TestClassLoader extends ClassLoader {
        public TestClassLoader(final ClassLoader parent) {
            super(parent);
        }

        public Class<?> defineClass(final String name, final byte[] data) {
            return defineClass(name, data, 0, data.length, null);
        }
    }

}