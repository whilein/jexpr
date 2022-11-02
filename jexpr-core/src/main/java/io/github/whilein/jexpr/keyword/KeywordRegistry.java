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

package io.github.whilein.jexpr.keyword;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.constant.OperandBoolean;
import io.github.whilein.jexpr.operand.constant.OperandObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author whilein
 */
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeywordRegistry {

    @Getter
    Map<String, Operand> keywords;

    private static final Map<String, Operand> DEFAULT_KEYWORDS = Collections.unmodifiableMap(
            new HashMap<String, Operand>() {
                {
                    put("true", OperandBoolean.TRUE);
                    put("false", OperandBoolean.FALSE);
                    put("null", OperandObject.valueOf(null));
                }
            }
    );

    public static @Unmodifiable @NotNull Map<@NotNull String, @NotNull Operand> getDefaultKeywords() {
        return DEFAULT_KEYWORDS;
    }

    public static @NotNull KeywordRegistry createDefault() {
        return new KeywordRegistry(DEFAULT_KEYWORDS);
    }

    public static @NotNull KeywordRegistry create(final @NotNull Map<@NotNull String, @NotNull Operand> keywords) {
        return new KeywordRegistry(Collections.unmodifiableMap(new HashMap<>(keywords)));
    }

    public @NotNull Operand getKeyword(final @NotNull String name) {
        return Objects.requireNonNull(keywords.get(name), () -> "Unknown keyword: " + name);
    }

    public boolean hasKeyword(final @NotNull String name) {
        return keywords.containsKey(name);
    }

}
