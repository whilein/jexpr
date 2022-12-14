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

package io.github.whilein.jexpr.compiler.local;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleLocalMap implements LocalMap {

    private final Map<String, Local> localMap;

    public static @NotNull LocalMap create() {
        return new SimpleLocalMap(new HashMap<>());
    }

    public @NotNull LocalMap add(final @NotNull String name, final int index, final @NotNull Type type) {
        localMap.put(name, new LocalImpl(index, type));

        return this;
    }

    @Override
    public @NotNull Local get(final @NotNull String name) {
        val local = localMap.get(name);

        if (local == null) {
            throw new IllegalArgumentException("Unknown reference: " + name);
        }

        return local;
    }

    @Value
    private static class LocalImpl implements Local {
        int index;
        Type type;
    }

}
