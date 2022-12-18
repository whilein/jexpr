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

package io.github.whilein.jexpr.compiler.operator;

import io.github.whilein.jexpr.api.token.operator.Operator;
import io.github.whilein.jexpr.compiler.util.TypeUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractAsmOperator<T extends Operator> implements AsmOperator<T> {

    @Getter
    Class<? extends T> operatorType;

    protected static Type getNumberType(final Type left, final Type right) {
        try {
            val leftNumber = getNumberType(left);
            val rightNumber = getNumberType(right);

            return TypeUtils.getPreferredNumber(leftNumber, rightNumber);
        } catch (final UnsupportedOperationException e) {
            throw new UnsupportedOperationException("operator is not applicable to " + left + " & " + right, e);
        }
    }

    protected static void ensureBooleanType(final @Nullable Type type) {
        if (type != null && (type.getSort() == Type.BOOLEAN || type.getInternalName().equals("java/lang/Boolean"))) {
            return;
        }

        throw new UnsupportedOperationException("operator is not applicable to " + type);
    }

    protected static Type getBooleanType(final Type left, final Type right) {
        try {
            ensureBooleanType(left);
            ensureBooleanType(right);

            return Type.BOOLEAN_TYPE;
        } catch (final UnsupportedOperationException e) {
            throw new UnsupportedOperationException("operator is not applicable to " + left + " & " + right, e);
        }
    }

    protected static Type getIntegralType(final @Nullable Type left, final @Nullable Type right) {
        try {
            val leftNumber = getIntegralType(left);
            val rightNumber = getIntegralType(right);

            return TypeUtils.getPreferredNumber(leftNumber, rightNumber);
        } catch (final UnsupportedOperationException e) {
            throw new UnsupportedOperationException("operator is not applicable to " + left + " & " + right, e);
        }
    }

    protected static void ensureIntegralType(final @Nullable Type type) {
        if (type != null && (TypeUtils.isPrimitiveIntegral(type) || TypeUtils.isIntegralWrapper(type))) {
            return;
        }

        throw new UnsupportedOperationException("operator is not applicable to " + type);
    }

    protected static Type getIntegralType(final Type type) {
        if (type != null) {
            if (TypeUtils.isPrimitiveIntegral(type)) {
                return type;
            }

            if (TypeUtils.isIntegralWrapper(type)) {
                return TypeUtils.getPrimitive(type);
            }
        }

        throw new UnsupportedOperationException("operator is not applicable to " + type);
    }

    protected static void ensureNumberType(final Type type) {
        if (TypeUtils.isPrimitiveNumber(type) || TypeUtils.isNumberWrapper(type)) {
            return;
        }

        throw new UnsupportedOperationException("operator is not applicable to " + type);
    }

    protected static Type getNumberType(final Type type) {
        if (type != null) {
            if (TypeUtils.isPrimitiveNumber(type)) {
                return type;
            }

            if (TypeUtils.isNumberWrapper(type)) {
                return TypeUtils.getPrimitive(type);
            }
        }

        throw new UnsupportedOperationException("operator is not applicable to " + type);
    }

}
