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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface Registry<I extends RegistryItem<V>, V> {
    void register(@NotNull I item);

    void unregister(@NotNull I item);

    @Nullable I unregister(@NotNull V value);

    /**
     * Get list of registered items
     *
     * @return list of items
     */
    @Unmodifiable @NotNull List<I> getItems();

    /**
     * Find registered item by its value
     *
     * @param value value of entry
     * @return entry by value
     */
    @Nullable I get(@NotNull V value);

}
