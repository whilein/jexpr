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

package io.github.whilein.jexpr.operand.constant;

import io.github.whilein.jexpr.operand.OperandDelegate;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
public abstract class OperandNumber extends OperandDelegate<@NotNull Number> implements OperandConstant {

    protected OperandNumber(final Number number) {
        super(number);
    }

    @Override
    public @NotNull Number toNumber() {
        return delegatedValue;
    }

    @Override
    public boolean toBoolean() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

}
