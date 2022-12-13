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

package io.github.whilein.jexpr.api;

import io.github.whilein.jexpr.api.keyword.KeywordRegistry;
import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandParser;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.OperatorRegistry;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.Reader;

public interface Jexpr {

    @NotNull OperatorRegistry<@NotNull BinaryOperator> getBinaryOperatorRegistry();

    @NotNull OperatorRegistry<@NotNull UnaryOperator> getUnaryOperatorRegistry();

    @NotNull KeywordRegistry getKeywordRegistry();

    @NotNull OperandParser getOperandParser();

    @NotNull Operand parse(@NotNull String value);

    @NotNull Operand parse(byte @NotNull [] value);

    @NotNull Operand parse(@NotNull InputStream stream);

    @NotNull Operand parse(@NotNull Reader reader);

}
