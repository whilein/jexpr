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

package io.github.whilein.jexpr;

import io.github.whilein.jexpr.api.Jexpr;
import io.github.whilein.jexpr.api.keyword.KeywordRegistry;
import io.github.whilein.jexpr.api.token.SelectableTokenParser;
import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandParser;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.OperatorRegistry;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.io.ByteArrayOutput;
import io.github.whilein.jexpr.keyword.SimpleKeyword;
import io.github.whilein.jexpr.keyword.SimpleKeywordRegistry;
import io.github.whilein.jexpr.token.*;
import io.github.whilein.jexpr.token.operand.constant.OperandBoolean;
import io.github.whilein.jexpr.token.operand.constant.OperandObject;
import io.github.whilein.jexpr.token.operator.SimpleOperatorRegistry;
import io.github.whilein.jexpr.token.operator.type.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class SimpleJexpr implements Jexpr {

    OperatorRegistry<BinaryOperator> binaryOperatorRegistry =
            new SimpleOperatorRegistry<>();
    OperatorRegistry<UnaryOperator> unaryOperatorRegistry =
            new SimpleOperatorRegistry<>();

    KeywordRegistry keywordRegistry = new SimpleKeywordRegistry();

    OperandParser operandParser;

    public SimpleJexpr() {
        operandParser = DefaultOperandParser.create(init());
    }

    protected List<SelectableTokenParser> init() {
        registerDefaults();

        val buffer = initBuffer();

        return Arrays.asList(
                new NumberTokenParser(buffer),
                new StringTokenParser(buffer),
                new UnaryOperatorTokenParser(unaryOperatorRegistry),
                new BinaryOperatorTokenParser(binaryOperatorRegistry),
                new ReferenceTokenParser(keywordRegistry, buffer)
        );
    }

    protected ByteArrayOutput initBuffer() {
        return new ByteArrayOutput(8192);
    }

    protected void registerDefaults() {
        registerDefaultKeywords();
        registerDefaultOperators();
    }

    protected void registerDefaultKeywords() {
        keywordRegistry.register(new SimpleKeyword("null", OperandObject.nullValue()));
        keywordRegistry.register(new SimpleKeyword("true", OperandBoolean.trueValue()));
        keywordRegistry.register(new SimpleKeyword("false", OperandBoolean.falseValue()));
    }

    protected void registerDefaultOperators() {
        registerDefaultBinaryOperators();
        registerDefaultUnaryOperators();
    }

    protected void registerDefaultUnaryOperators() {
        unaryOperatorRegistry.register(new OperatorUnaryPlus());
        unaryOperatorRegistry.register(new OperatorUnaryMinus());
        unaryOperatorRegistry.register(new OperatorBitwiseComplement());
        unaryOperatorRegistry.register(new OperatorNegate());
    }

    protected void registerDefaultBinaryOperators() {
        binaryOperatorRegistry.register(new OperatorPlus());
        binaryOperatorRegistry.register(new OperatorMinus());
        binaryOperatorRegistry.register(new OperatorMultiply());
        binaryOperatorRegistry.register(new OperatorDivide());
        binaryOperatorRegistry.register(new OperatorRemainder());
        binaryOperatorRegistry.register(new OperatorBitwiseAnd());
        binaryOperatorRegistry.register(new OperatorBitwiseOr());
        binaryOperatorRegistry.register(new OperatorBitwiseXor());
        binaryOperatorRegistry.register(new OperatorBitwiseLeftShift());
        binaryOperatorRegistry.register(new OperatorBitwiseRightShift());
        binaryOperatorRegistry.register(new OperatorBitwiseUnsignedRightShift());
        binaryOperatorRegistry.register(new OperatorOr());
        binaryOperatorRegistry.register(new OperatorAnd());
        binaryOperatorRegistry.register(new OperatorStrictGreater());
        binaryOperatorRegistry.register(new OperatorGreater());
        binaryOperatorRegistry.register(new OperatorStrictLess());
        binaryOperatorRegistry.register(new OperatorLess());
        binaryOperatorRegistry.register(new OperatorEquals());
        binaryOperatorRegistry.register(new OperatorNotEquals());
        binaryOperatorRegistry.register(new OperatorMemberSelection());
    }


    @Override
    public @NotNull Operand parse(final @NotNull String value) {
        return parse(new StringIn(value));
    }

    @Override
    public @NotNull Operand parse(final byte @NotNull [] value) {
        return parse(new ByteArrayIn(value));
    }

    private <E extends Throwable> Operand parse(final In<E> in) throws E {
        val parser = getOperandParser();

        int n;

        while ((n = in.read()) != -1) {
            parser.update(n);
        }

        return parser.doFinal();
    }

    @Override
    public @NotNull Operand parse(final @NotNull InputStream is) {
        try {
            return parse(new StreamIn(is));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Operand parse(final @NotNull Reader reader) {
        try {
            return parse(new ReaderIn(reader));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private interface In<E extends Throwable> {

        int read() throws E;

    }

    @RequiredArgsConstructor
    private static final class StreamIn implements In<IOException> {
        final InputStream is;

        @Override
        public int read() throws IOException {
            return is.read();
        }
    }

    @RequiredArgsConstructor
    private static final class ReaderIn implements In<IOException> {
        final Reader reader;

        @Override
        public int read() throws IOException {
            return reader.read();
        }
    }

    @RequiredArgsConstructor
    private static final class ByteArrayIn implements In<RuntimeException> {

        final byte[] bytes;

        int position;

        @Override
        public int read() throws RuntimeException {
            return position == bytes.length ? -1 : bytes[position++];
        }
    }

    @RequiredArgsConstructor
    private static final class StringIn implements In<RuntimeException> {

        final String text;

        int position;

        @Override
        public int read() throws RuntimeException {
            return position == text.length() ? -1 : text.charAt(position++);
        }
    }

}
