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

package io.github.whilein.jexpr.token.operator.type;

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.token.operand.constant.OperandBoolean;
import io.github.whilein.jexpr.token.operator.AbstractBinaryOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author whilein
 */
public final class OperatorEquals extends AbstractBinaryOperator {

    public OperatorEquals() {
        super("==", OperatorPrecedenceConsts.EQUALITY);
    }

    @Override
    public String toString() {
        return "EQUALS";
    }

    @Override
    public @NotNull Operand apply(final Object left, final Object right) {
        return OperandBoolean.valueOf(Objects.equals(left, right));
    }

    @Override
    public @NotNull Operand apply(final int left, final int right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final int left, final long right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final int left, final float right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final int left, final double right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final long left, final int right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final long left, final long right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final long left, final float right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final long left, final double right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final float left, final int right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final float left, final long right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final float left, final float right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final float left, final double right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final double left, final int right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final double left, final long right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final double left, final float right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final double left, final double right) {
        return OperandBoolean.valueOf(left == right);
    }

    @Override
    public @NotNull Operand apply(final @NotNull String left, final @NotNull String right) {
        return OperandBoolean.valueOf(left.equals(right));
    }

    @Override
    public @NotNull Operand apply(final boolean left, final boolean right) {
        return OperandBoolean.valueOf(left == right);
    }
}
