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

package io.github.whilein.jexpr.operator;

import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public interface Operator extends Token {

    boolean isPredictable(boolean value);

    boolean isPredictable(int value);

    boolean isPredictable(long value);

    boolean isPredictable(double value);

    boolean isPredictable(float value);

    boolean isPredictable(Object value);

    boolean isPredictable(@NotNull String value);

    boolean isTwoOperand();

    boolean isOneOperand();

    @NotNull String getValue();

    int getOrder();

    @NotNull Operand apply(int left, int right);

    @NotNull Operand apply(int left, long right);

    @NotNull Operand apply(int left, float right);

    @NotNull Operand apply(int left, double right);

    @NotNull Operand apply(int left, String right);

    @NotNull Operand apply(int left, boolean right);

    @NotNull Operand apply(int value);

    @NotNull Operand apply(long left, int right);

    @NotNull Operand apply(long left, long right);

    @NotNull Operand apply(long left, float right);

    @NotNull Operand apply(long left, double right);

    @NotNull Operand apply(long left, String right);

    @NotNull Operand apply(long left, boolean right);

    @NotNull Operand apply(long value);

    @NotNull Operand apply(float left, int right);

    @NotNull Operand apply(float left, long right);

    @NotNull Operand apply(float left, float right);

    @NotNull Operand apply(float left, double right);

    @NotNull Operand apply(float left, String right);

    @NotNull Operand apply(float left, boolean right);

    @NotNull Operand apply(float value);

    @NotNull Operand apply(double left, int right);

    @NotNull Operand apply(double left, long right);

    @NotNull Operand apply(double left, float right);

    @NotNull Operand apply(double left, double right);

    @NotNull Operand apply(double left, String right);

    @NotNull Operand apply(double left, boolean right);

    @NotNull Operand apply(double value);

    @NotNull Operand apply(String left, int right);

    @NotNull Operand apply(String left, long right);

    @NotNull Operand apply(String left, float right);

    @NotNull Operand apply(String left, double right);

    @NotNull Operand apply(String left, String right);

    @NotNull Operand apply(String left, boolean right);

    @NotNull Operand apply(boolean left, int right);

    @NotNull Operand apply(boolean left, long right);

    @NotNull Operand apply(boolean left, float right);

    @NotNull Operand apply(boolean left, double right);

    @NotNull Operand apply(boolean left, String right);

    @NotNull Operand apply(boolean left, boolean right);

    @NotNull Operand apply(int left, Object right);

    @NotNull Operand apply(long left, Object right);

    @NotNull Operand apply(float left, Object right);

    @NotNull Operand apply(double left, Object right);

    @NotNull Operand apply(boolean left, Object right);

    @NotNull Operand apply(Object left, int right);

    @NotNull Operand apply(Object left, long right);

    @NotNull Operand apply(Object left, float right);

    @NotNull Operand apply(Object left, double right);

    @NotNull Operand apply(Object left, boolean right);

    @NotNull Operand apply(String left, Object right);

    @NotNull Operand apply(Object left, String right);

    @NotNull Operand apply(Object left, Object right);

    @NotNull Operand apply(Object value);

    @NotNull Operand apply(String value);

    @NotNull Operand apply(boolean value);


}
