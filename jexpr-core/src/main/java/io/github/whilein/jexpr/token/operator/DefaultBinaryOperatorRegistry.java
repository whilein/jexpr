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

package io.github.whilein.jexpr.token.operator;

import io.github.whilein.jexpr.api.token.operator.BinaryOperator;
import io.github.whilein.jexpr.token.operator.type.*;

public class DefaultBinaryOperatorRegistry extends SimpleOperatorRegistry<BinaryOperator> {

    {
        register(new OperatorPlus());
        register(new OperatorMinus());
        register(new OperatorMultiply());
        register(new OperatorDivide());
        register(new OperatorRemainder());
        register(new OperatorBitwiseAnd());
        register(new OperatorBitwiseOr());
        register(new OperatorBitwiseXor());
        register(new OperatorBitwiseLeftShift());
        register(new OperatorBitwiseRightShift());
        register(new OperatorBitwiseUnsignedRightShift());
        register(new OperatorOr());
        register(new OperatorAnd());
        register(new OperatorStrictGreater());
        register(new OperatorGreater());
        register(new OperatorStrictLess());
        register(new OperatorLess());
        register(new OperatorEquals());
        register(new OperatorNotEquals());
        register(new OperatorMemberSelection());
    }

}
