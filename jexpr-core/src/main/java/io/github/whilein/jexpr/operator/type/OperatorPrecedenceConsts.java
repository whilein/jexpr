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

package io.github.whilein.jexpr.operator.type;

import lombok.experimental.UtilityClass;

/**
 * @author whilein
 */
@UtilityClass
class OperatorPrecedenceConsts {

    public final int OR = 30;
    public final int AND = 40;
    public final int BITWISE_OR = 50;
    public final int BITWISE_XOR = 60;
    public final int BITWISE_AND = 70;
    public final int EQUALITY = 80;
    public final int COMPARE = 90;
    public final int BITWISE_SHIFT = 100;
    public final int ADDITIVE = 110;
    public final int MULTIPLICATIVE = 120;
    public final int UNARY = 140;

}
