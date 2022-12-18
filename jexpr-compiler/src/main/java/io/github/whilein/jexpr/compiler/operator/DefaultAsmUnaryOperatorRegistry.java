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

import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorBitwiseComplement;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorNegate;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorUnaryMinus;
import io.github.whilein.jexpr.compiler.operator.type.AsmOperatorUnaryPlus;

import static io.github.whilein.jexpr.compiler.util.ClassUtils.findClassAsSubclass;

public class DefaultAsmUnaryOperatorRegistry extends SimpleAsmUnaryOperatorRegistry {

    {
        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorUnaryMinus", UnaryOperator.class)
                .ifPresent(cls -> register(new AsmOperatorUnaryMinus(cls)));

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorUnaryPlus", UnaryOperator.class)
                .ifPresent(cls -> register(new AsmOperatorUnaryPlus(cls)));

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorBitwiseComplement", UnaryOperator.class)
                .ifPresent(cls -> register(new AsmOperatorBitwiseComplement(cls)));

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorNegate", UnaryOperator.class)
                .ifPresent(cls -> register(new AsmOperatorNegate(cls)));
    }

}
