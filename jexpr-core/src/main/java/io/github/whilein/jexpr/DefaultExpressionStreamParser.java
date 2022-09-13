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

import io.github.whilein.jexpr.token.SelectableTokenParser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whilein
 */
public class DefaultExpressionStreamParser extends AbstractExpressionStreamParser {

    private DefaultExpressionStreamParser(final List<SelectableTokenParser> parsers) {
        super(parsers);

        state = STATE_CONTENT;
    }

    @Override
    public void update(final int ch) throws SyntaxException {
        super.update(ch);
    }

    public static @NotNull ExpressionStreamParser create(final @NotNull List<@NotNull SelectableTokenParser> parsers) {
        return new DefaultExpressionStreamParser(new ArrayList<>(parsers));
    }
}
