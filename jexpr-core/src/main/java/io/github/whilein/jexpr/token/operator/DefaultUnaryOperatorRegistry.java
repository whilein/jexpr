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

import io.github.whilein.jexpr.api.token.operator.UnaryOperator;
import io.github.whilein.jexpr.token.operator.type.OperatorBitwiseComplement;
import io.github.whilein.jexpr.token.operator.type.OperatorNegate;
import io.github.whilein.jexpr.token.operator.type.OperatorUnaryMinus;
import io.github.whilein.jexpr.token.operator.type.OperatorUnaryPlus;

public class DefaultUnaryOperatorRegistry extends SimpleOperatorRegistry<UnaryOperator> {

    {
        register(new OperatorUnaryPlus());
        register(new OperatorUnaryMinus());
        register(new OperatorBitwiseComplement());
        register(new OperatorNegate());
    }

}
