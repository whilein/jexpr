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

package io.github.whilein.jexpr.api.registry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractRegistry<I extends RegistryItem<V>, V> implements Registry<I, V> {

    Map<V, I> map = new HashMap<>();

    @Override
    public void unregisterAll() {
        val itemIterator = map.values().iterator();

        while (itemIterator.hasNext()) {
            onUnregister(itemIterator.next());

            itemIterator.remove();
        }
    }

    protected void onRegister(I item) {
    }

    protected void onUnregister(I item) {
    }

    @Override
    public void register(@NotNull I item) {
        val oldItem = map.put(item.getValue(), item);

        if (oldItem != null) {
            onUnregister(oldItem);
        }

        onRegister(item);
    }

    @Override
    public void unregister(@NotNull I item) {
        if (map.remove(item.getValue(), item)) {
            onUnregister(item);
        }
    }

    @Override
    public @Nullable I unregister(@NotNull V value) {
        val item = map.remove(value);

        if (item != null) {
            onUnregister(item);
        }

        return item;
    }

    @Override
    public @Unmodifiable @NotNull List<I> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(map.values()));
    }

    @Override
    public @Nullable I get(@NotNull V value) {
        return map.get(value);
    }
}
