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

import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.compiler.operator.type.*;
import org.objectweb.asm.Opcodes;

import static io.github.whilein.jexpr.compiler.util.ClassUtils.findClassAsSubclass;

public class DefaultAsmBinaryOperatorRegistry extends SimpleAsmBinaryOperatorRegistry {

    {
        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorPlus", BinaryOperator.class)
                .map(AsmOperatorPlus::new)
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorMinus", BinaryOperator.class)
                .map(cls -> new AsmOperatorArithmetic(cls, Opcodes.ISUB))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorMultiply", BinaryOperator.class)
                .map(cls -> new AsmOperatorArithmetic(cls, Opcodes.IMUL))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorDivide", BinaryOperator.class)
                .map(cls -> new AsmOperatorArithmetic(cls, Opcodes.IDIV))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorRemainder", BinaryOperator.class)
                .map(cls -> new AsmOperatorArithmetic(cls, Opcodes.IREM))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorBitwiseAnd", BinaryOperator.class)
                .map(cls -> new AsmOperatorBitwise(cls, Opcodes.IAND))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorBitwiseOr", BinaryOperator.class)
                .map(cls -> new AsmOperatorBitwise(cls, Opcodes.IOR))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorBitwiseXor", BinaryOperator.class)
                .map(cls -> new AsmOperatorBitwise(cls, Opcodes.IXOR))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorBitwiseLeftShift", BinaryOperator.class)
                .map(cls -> new AsmOperatorBitwiseShift(cls, Opcodes.ISHL))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorBitwiseRightShift", BinaryOperator.class)
                .map(cls -> new AsmOperatorBitwiseShift(cls, Opcodes.ISHR))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorBitwiseUnsignedRightShift", BinaryOperator.class)
                .map(cls -> new AsmOperatorBitwiseShift(cls, Opcodes.IUSHR))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorAnd", BinaryOperator.class)
                .map(AsmOperatorAnd::new)
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorOr", BinaryOperator.class)
                .map(AsmOperatorOr::new)
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorEquals", BinaryOperator.class)
                .map(cls -> new AsmOperatorEquals(cls, false))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorNotEquals", BinaryOperator.class)
                .map(cls -> new AsmOperatorEquals(cls, true))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorGreater", BinaryOperator.class)
                .map(cls -> new AsmOperatorCompare(cls, Opcodes.IF_ICMPLT, Opcodes.IFLT, Opcodes.DCMPL, Opcodes.FCMPL))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorStrictGreater", BinaryOperator.class)
                .map(cls -> new AsmOperatorCompare(cls, Opcodes.IF_ICMPLE, Opcodes.IFLE, Opcodes.DCMPL, Opcodes.FCMPL))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorLess", BinaryOperator.class)
                .map(cls -> new AsmOperatorCompare(cls, Opcodes.IF_ICMPGT, Opcodes.IFGT, Opcodes.DCMPG, Opcodes.FCMPG))
                .ifPresent(this::register);

        findClassAsSubclass("io.github.whilein.jexpr.token.operator.type.OperatorStrictLess", BinaryOperator.class)
                .map(cls -> new AsmOperatorCompare(cls, Opcodes.IF_ICMPGE, Opcodes.IFGE, Opcodes.DCMPG, Opcodes.FCMPG))
                .ifPresent(this::register);
    }

}
