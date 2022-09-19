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

package io.github.whilein.jexpr.operand;

import io.github.whilein.jexpr.operand.flat.FlatStream;
import io.github.whilein.jexpr.operand.flat.StackFlatStream;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public abstract class OperandBase implements Operand {

    @Override
    public @NotNull FlatStream flat() {
        val flatStream = StackFlatStream.create();
        flat(flatStream);
        return flatStream;
    }

    @Override
    public void flat(final @NotNull FlatStream flatStream) {
        flatStream.put(this);
    }

    @Override
    public String toString() {
        val result = new StringBuilder();
        toString(result);
        return result.toString();
    }

}