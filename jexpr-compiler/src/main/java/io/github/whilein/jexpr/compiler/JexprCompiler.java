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

package io.github.whilein.jexpr.compiler;

import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.compiler.local.LocalMap;
import io.github.whilein.jexpr.compiler.operand.ToTypedOperandMapperFactory;
import io.github.whilein.jexpr.compiler.operator.AsmBinaryOperator;
import io.github.whilein.jexpr.compiler.operator.AsmOperatorRegistry;
import io.github.whilein.jexpr.compiler.operator.AsmUnaryOperator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.MethodVisitor;

public interface JexprCompiler {

    @Contract(pure = true)
    @NotNull AsmOperatorRegistry<AsmBinaryOperator, BinaryOperator> getBinaryOperatorRegistry();

    @Contract(pure = true)
    @NotNull AsmOperatorRegistry<AsmUnaryOperator, UnaryOperator> getUnaryOperatorRegistry();

    @Contract(pure = true)
    @NotNull ToTypedOperandMapperFactory getToTypedOperandMapperFactory();

    void setToTypedOperandMapperFactory(@NotNull ToTypedOperandMapperFactory toTypedOperandMapperFactory);

    @Contract(pure = true)
    @NotNull OperandCompilerFactory getOperandCompilerFactory();

    void setOperandCompilerFactory(@NotNull OperandCompilerFactory operandCompilerFactory);

    @Contract(pure = true)
    @NotNull OperandInterfaceImplementorFactory getOperandInterfaceImplementorFactory();

    void setOperandInterfaceImplementorFactory(@NotNull OperandInterfaceImplementorFactory operandInterfaceImplementorFactory);

    <T> @NotNull OperandInterfaceImplementor<T> implementInterface(@NotNull Class<T> interfaceType);

    void compile(@NotNull MethodVisitor mv, @NotNull LocalMap localMap, @NotNull Operand operand);

    @NotNull OperandCompiler toMethod(@NotNull MethodVisitor mv);

}