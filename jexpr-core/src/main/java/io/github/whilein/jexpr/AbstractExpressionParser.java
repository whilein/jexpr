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

import io.github.whilein.jexpr.operand.Operand;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author whilein
 */
public abstract class AbstractExpressionParser implements ExpressionParser {

    @Override
    public @NotNull Operand parse(final @NotNull String value) {
        return parse(new StringIn(value));
    }

    @Override
    public @NotNull Operand parse(final byte @NotNull [] value) {
        return parse(new ByteArrayIn(value));
    }

    private <E extends Throwable> Operand parse(final In<E> in) throws E {
        try (val stream = getStream()) {
            int n;

            while ((n = in.read()) != -1) {
                stream.update(n);
            }

            return stream.doFinal();
        }
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
