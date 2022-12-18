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

package io.github.whilein.jexpr.api.token;

import io.github.whilein.jexpr.api.exception.SyntaxException;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public interface TokenParser {

    void reset();


    /**
     * Обработать символ токена. Из этих символов парсер составляет целый "токен",
     * окончательный результат можно получить используя {@link #doFinal(TokenVisitor)} ()}.
     *
     * @param ch символа
     * @throws SyntaxException при неожиданных входных данных
     */
    void update(int ch) throws SyntaxException;

    /**
     * Завершить обработку токена.
     *
     * @param tokenVisitor реципиент окончательного результата
     * @throws SyntaxException при неожиданном завершении
     */
    void doFinal(@NotNull TokenVisitor tokenVisitor) throws SyntaxException;

}
