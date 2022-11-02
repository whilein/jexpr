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
import io.github.whilein.jexpr.operand.constant.OperandInteger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whilein
 */
public class VariablesMap implements OperandVariableResolver {

    @Getter
    private final List<String> history = new ArrayList<>();

    private final Map<String, Operand> delegate = new HashMap<>();


    public VariablesMap put(final String key, final Operand value) {
        delegate.put(key, value);

        return this;
    }

    public VariablesMap put(final String key, final int value) {
        return put(key, OperandInteger.valueOf(value));
    }

    public Operand get(final String key) {
        history.add(key);

        return delegate.get(key);
    }

    @Override
    public @NotNull Operand resolve(final @NotNull String reference) {
        return get(reference);
    }
}
