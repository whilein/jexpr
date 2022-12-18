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

package io.github.whilein.jexpr.compiler.util;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

// без ломбока ибо статик импорт =(
public final class ClassUtils {

    private ClassUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <T> @NotNull Optional<@NotNull Class<? extends T>> findClassAsSubclass(
            @NotNull String name,
            @NotNull Class<T> subclassOf
    ) {
        return findClassAsSubclass(ClassUtils.class.getClassLoader(), name, subclassOf);
    }

    public static <T> @NotNull Optional<@NotNull Class<? extends T>> findClassAsSubclass(
            @NotNull ClassLoader cl,
            @NotNull String name,
            @NotNull Class<T> subclassOf
    ) {
        return findClass(cl, name).map(cls -> cls.asSubclass(subclassOf));
    }

    public static @NotNull Optional<@NotNull Class<?>> findClass(@NotNull String name) {
        return findClass(ClassUtils.class.getClassLoader(), name);
    }

    public static @NotNull Optional<@NotNull Class<?>> findClass(@NotNull ClassLoader cl, @NotNull String name) {
        try {
            return Optional.of(cl.loadClass(name));
        } catch (final ClassNotFoundException e) {
            return Optional.empty();
        }
    }

}
