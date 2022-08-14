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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class ByteArrayInput {

    @Getter
    byte[] array;

    @Getter
    int offset;

    @Getter
    int length;

    @Setter
    @Getter
    @NonFinal
    int position;

    public ByteArrayInput(final byte @NotNull [] array) {
        this(array, 0, array.length);
    }

    public boolean hasNext() {
        return remaining() > 0;
    }

    public int remaining() {
        return length - position;
    }

    public int next() {
        val position = this.position++;

        if (position >= length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return array[offset + position] & 0xFF;
    }

}
