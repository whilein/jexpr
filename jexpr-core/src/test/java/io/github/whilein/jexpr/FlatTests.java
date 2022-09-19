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

package io.github.whilein.jexpr;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whilein
 */
final class FlatTests {
    static ExpressionParser expressionParser;

    @BeforeAll
    static void setup() {
        expressionParser = SimpleExpressionParser.createDefault();
    }

    @Test
    void testFlatBinary() {
        val result = expressionParser.parse("(x + 2) - (y * -~z) / (a ^ 0b111101)");
        val flatStream = result.flat();

        val map = new VariablesMap()
                .put("x", 2)
                .put("y", 3)
                .put("z", 4)
                .put("a", 0b111111);
        assertEquals((2 + 2) - (3 * -~4) / (0b111111 ^ 0b111101), flatStream.solve(map::get).getValue());
    }

}
