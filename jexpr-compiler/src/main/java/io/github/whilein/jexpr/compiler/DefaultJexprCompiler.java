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

import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.compiler.operand.SimpleToTypedOperandMapperFactory;
import io.github.whilein.jexpr.compiler.operand.ToTypedOperandMapperFactory;
import io.github.whilein.jexpr.compiler.operator.*;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class DefaultJexprCompiler extends AbstractJexprCompiler {

    private DefaultJexprCompiler(
            AsmOperatorRegistry<AsmBinaryOperator, BinaryOperator> binaryOperatorRegistry,
            AsmOperatorRegistry<AsmUnaryOperator, UnaryOperator> unaryOperatorRegistry,
            ToTypedOperandMapperFactory toTypedOperandMapperFactory,
            OperandInterfaceImplementorFactory operandInterfaceImplementorFactory,
            OperandCompilerFactory operandCompilerFactory
    ) {
        super(
                binaryOperatorRegistry,
                unaryOperatorRegistry,
                toTypedOperandMapperFactory,
                operandInterfaceImplementorFactory,
                operandCompilerFactory
        );
    }

    public static @NotNull JexprCompiler create() {
        val binaryOperatorRegistry = new DefaultAsmBinaryOperatorRegistry();
        val unaryOperatorRegistry = new DefaultAsmUnaryOperatorRegistry();

        val toTypedOperandFactory = new SimpleToTypedOperandMapperFactory(
                binaryOperatorRegistry,
                unaryOperatorRegistry
        );

        val operandCompilerFactory = new SimpleOperandCompilerFactory();

        val operandInterfaceImplementorFactory = new SimpleOperandInterfaceImplementorFactory(
                toTypedOperandFactory,
                operandCompilerFactory
        );

        return new DefaultJexprCompiler(
                binaryOperatorRegistry,
                unaryOperatorRegistry,
                toTypedOperandFactory,
                operandInterfaceImplementorFactory,
                operandCompilerFactory
        );
    }

}
