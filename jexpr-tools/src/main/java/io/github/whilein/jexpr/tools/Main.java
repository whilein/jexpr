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

package io.github.whilein.jexpr.tools;

import io.github.whilein.jexpr.DefaultJexpr;
import io.github.whilein.jexpr.api.token.operand.Operand;
import io.github.whilein.jexpr.api.token.operand.OperandVariableResolver;
import io.github.whilein.jexpr.token.operand.Operands;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author whilein
 */
@UtilityClass
public class Main {

    public void main(final String[] args) throws IOException {
        val jexpr = DefaultJexpr.create();
        Operand expression = jexpr.parse(args[0]);

        val solvedMap = new HashMap<String, Operand>();
        val in = new Scanner(System.in);

        val graph = Graph.create();
        val graphMapper = new GraphMapper();

        double y = 0;

        while (true) {
            graphMapper.loc(0, y);

            val tree = expression.apply(graphMapper);

            tree.add(graph);

            if (expression.isConstant()) {
                break;
            }

            expression = expression.solve(new OperandVariableResolver() {

                boolean solved;

                public Operand input(final String reference) {
                    try {
                        System.out.print(reference + ": ");

                        return jexpr.parse(in.nextLine());
                    } finally {
                        solved = true;
                    }
                }

                @Override
                public @NotNull Operand resolve(final @NotNull String reference) {
                    if (solved) {
                        return solvedMap.getOrDefault(reference, Operands.reference(reference));
                    }

                    return solvedMap.computeIfAbsent(reference, this::input);
                }
            });

            y += tree.getHeight() + 100;
        }

        graph.save(Paths.get("graph.xgml"));
    }

}
