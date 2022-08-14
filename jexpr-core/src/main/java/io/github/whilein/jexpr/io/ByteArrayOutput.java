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

package io.github.whilein.jexpr.io;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author whilein
 */
@Accessors(fluent = true)
public final class ByteArrayOutput {

    @Getter
    byte[] array;

    @Getter
    int position;

    public ByteArrayOutput(final int capacity) {
        this.array = new byte[capacity];
    }

    public ByteArrayOutput() {
        this(8192);
    }

    public void reset() {
        position = 0;
    }

    public @NotNull String getString() {
        return new String(array, 0, position, StandardCharsets.UTF_8);
    }

    public byte @NotNull [] getBytes() {
        return Arrays.copyOf(array, position);
    }

    public boolean isEmpty() {
        return position == 0;
    }

    public void put(final int value) {
        val position = this.position++;

        if (position >= array.length) {
            array = Arrays.copyOf(array, array.length * 2);
        }

        array[position] = (byte) value;
    }

}
