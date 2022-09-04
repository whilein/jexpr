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

package io.github.whilein.jexpr.token;

/**
 * @author whilein
 */
public interface SelectableTokenParser extends TokenParser {
    /**
     * Требуется для обработки целых выражений, во время парсинга которых нужно переключаться
     * между разными реализациями парсеров.
     * <p>
     * Данный метод определяет, следует ли парсеру продолжать обрабатывать входящий поток данных. Если метод вернёт
     * {@code false}, то вслед за этим должен вызваться завершающий метод {@link #doFinal()}.
     * <p>
     * Также, этот метод вызывается сразу же после {@link #shouldActivate(int)}, поэтому следует, чтобы
     * этот метод не противоречил {@link #shouldActivate(int)} и также вернул {@code true}.
     *
     * @param ch символ
     * @return {@code true}, следует ли продолжать обработку
     */
    boolean shouldStayActive(int ch);

    /**
     * Требуется для обработки целых выражений, во время парсинга которых нужно переключаться
     * между разными реализациями парсеров.
     * <p>
     * Парсер выражений определяет соответствующий парсер по символу используя этот метод.
     *
     * @param ch символ
     * @return {@code true}, если следует использовать этот парсер
     */
    boolean shouldActivate(int ch);
}
