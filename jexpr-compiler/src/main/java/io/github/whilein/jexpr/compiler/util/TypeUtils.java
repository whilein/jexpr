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

package io.github.whilein.jexpr.compiler.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author whilein
 */
@UtilityClass
public class TypeUtils {

    private final Set<String> WRAPPERS = new HashSet<>(Arrays.asList(
            "java/lang/Byte",
            "java/lang/Short",
            "java/lang/Integer",
            "java/lang/Long",
            "java/lang/Float",
            "java/lang/Double",
            "java/lang/Character",
            "java/lang/Boolean"
    ));

    private final Set<String> INTEGRAL_WRAPPERS = new HashSet<>(Arrays.asList(
            "java/lang/Byte",
            "java/lang/Short",
            "java/lang/Integer",
            "java/lang/Long",
            "java/lang/Character"
    ));


    public boolean isIntegralWrapper(final @NotNull Type type) {
        return INTEGRAL_WRAPPERS.contains(type.getInternalName());
    }

    public boolean isNumber(final @NotNull Type type) {
        return isPrimitiveNumber(type) || isNumberWrapper(type);
    }

    public boolean isNumberWrapper(final @NotNull Type type) {
        return isWrapper(type) && !type.getInternalName().equals("java/lang/Boolean");
    }

    public boolean isWrapper(final @NotNull Type type) {
        return WRAPPERS.contains(type.getInternalName());
    }

    public Type getPrimitive(final Type type) {
        switch (type.getInternalName()) {
            case "java/lang/Byte":
                return Type.BYTE_TYPE;
            case "java/lang/Short":
                return Type.SHORT_TYPE;
            case "java/lang/Float":
                return Type.FLOAT_TYPE;
            case "java/lang/Double":
                return Type.DOUBLE_TYPE;
            case "java/lang/Integer":
                return Type.INT_TYPE;
            case "java/lang/Long":
                return Type.LONG_TYPE;
            case "java/lang/Character":
                return Type.CHAR_TYPE;
            case "java/lang/Boolean":
                return Type.BOOLEAN_TYPE;
            default:
                return type;
        }
    }

    public Type getWrapper(final Type type) {
        switch (type.getSort()) {
            case Type.BYTE:
                return Type.getType(Byte.class);
            case Type.SHORT:
                return Type.getType(Short.class);
            case Type.FLOAT:
                return Type.getType(Float.class);
            case Type.DOUBLE:
                return Type.getType(Double.class);
            case Type.INT:
                return Type.getType(Integer.class);
            case Type.LONG:
                return Type.getType(Long.class);
            case Type.CHAR:
                return Type.getType(Character.class);
            case Type.BOOLEAN:
                return Type.getType(Boolean.class);
            default:
                return type;
        }
    }

    public @NotNull Type getPreferredNumber(final @NotNull Type left, final @NotNull Type right) {
        val normLeft = getPrimitive(left);
        val normRight = getPrimitive(right);

        val leftSort = normLeft.getSort();
        val rightSort = normRight.getSort();

        switch (leftSort) {
            case Type.DOUBLE:
                return left;
            case Type.FLOAT:
                return rightSort == Type.DOUBLE ? right : left;
            case Type.LONG:
                return rightSort == Type.DOUBLE || rightSort == Type.FLOAT ? right : left;
            case Type.INT:
                return rightSort == Type.DOUBLE || rightSort == Type.FLOAT || rightSort == Type.LONG ? right : left;
            case Type.CHAR:
            case Type.SHORT:
            case Type.BYTE:
                return Type.INT_TYPE;
            default:
                throw new IllegalArgumentException(left + " " + right);
        }
    }

    public boolean isPrimitiveIntegral(final @NotNull Type type) {
        return isPrimitiveNumber(type) && type.getSort() != Type.FLOAT && type.getSort() != Type.DOUBLE;
    }

    public boolean isPrimitiveNumber(final @NotNull Type type) {
        val sort = type.getSort();
        return sort >= Type.CHAR && sort <= Type.DOUBLE;
    }

    public boolean isPrimitive(final @NotNull Type type) {
        val sort = type.getSort();
        return sort >= Type.BOOLEAN && sort <= Type.DOUBLE;
    }

}
