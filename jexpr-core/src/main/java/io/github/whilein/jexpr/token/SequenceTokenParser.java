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

package io.github.whilein.jexpr.token;

import io.github.whilein.jexpr.io.ByteArrayOutput;
import io.github.whilein.jexpr.keyword.KeywordRegistry;
import io.github.whilein.jexpr.operator.OperatorRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SequenceTokenParser extends AbstractTokenParser {

    @NonFinal
    SequenceTokenParser parent;

    @NonFinal
    SequenceTokenParser nestedSequencesParser;

    TokenVisitor tokenVisitor;

    List<TokenParser> parsers;

    private static final int STATE_LEADING_BRACKET = 0, STATE_CONTENT = 1, STATE_FINAL_BRACKET = 2;

    @NonFinal
    int state = STATE_LEADING_BRACKET;

    @NonFinal
    TokenParser activeParser;

    public static @NotNull TokenParser createDefault(
            final @NotNull TokenVisitor tokenVisitor
    ) {
        val buffer = new ByteArrayOutput();

        return new SequenceTokenParser(tokenVisitor, Arrays.asList(
                new NumberTokenParser(tokenVisitor, buffer),
                new StringTokenParser(tokenVisitor, buffer),
                new OperatorTokenParser(tokenVisitor, OperatorRegistry.createDefault()),
                new ReferenceTokenParser(tokenVisitor, KeywordRegistry.createDefault(), buffer)
        ));
    }

    public static @NotNull TokenParser createDefault(
            final @NotNull TokenVisitor tokenVisitor,
            final @NotNull OperatorRegistry operatorRegistry,
            final @NotNull KeywordRegistry keywordRegistry
    ) {
        val buffer = new ByteArrayOutput();

        return new SequenceTokenParser(tokenVisitor, Arrays.asList(
                new NumberTokenParser(tokenVisitor, buffer),
                new StringTokenParser(tokenVisitor, buffer),
                new OperatorTokenParser(tokenVisitor, operatorRegistry),
                new ReferenceTokenParser(tokenVisitor, keywordRegistry, buffer)
        ));
    }

    public static @NotNull TokenParser create(
            final @NotNull TokenVisitor tokenVisitor,
            final @NotNull List<@NotNull TokenParser> parsers
    ) {
        return new SequenceTokenParser(tokenVisitor, new ArrayList<>(parsers));
    }

    @Override
    public boolean shouldStayActive(final int ch) {
        return state != STATE_FINAL_BRACKET;
    }

    @Override
    public boolean shouldActivate(final int ch) {
        return parent == null || ch == '(';
    }

    @Override
    public void update(final int ch) {
        if (state == STATE_LEADING_BRACKET) {
            state = STATE_CONTENT;

            tokenVisitor.visitParenthesesOpen();

            // ignoring leading bracket
            if (parent != null) {
                return;
            }
        }

        TokenParser activeParser = this.activeParser;

        if (activeParser != null && !activeParser.shouldStayActive(ch)) {
            activeParser.doFinal();

            this.activeParser = activeParser = null;
        }

        if (activeParser == null) {
            if (isControl(ch)) {
                return;
            }

            if (ch == ')') {
                state = STATE_FINAL_BRACKET;

                return;
            }

            this.activeParser = activeParser = initActiveParser(ch);
        }

        activeParser.update(ch);
    }

    @Override
    public void doFinal() {
        state = STATE_LEADING_BRACKET;

        if (activeParser != null) {
            activeParser.doFinal();
            activeParser = null;
        }

        tokenVisitor.visitParenthesesClose();
    }

    private TokenParser initActiveParser(final int ch) {
        for (val parser : parsers) {
            if (parser.shouldActivate(ch)) {
                return parser;
            }
        }

        if (ch == '(') {
            if (nestedSequencesParser == null) {
                nestedSequencesParser = new SequenceTokenParser(tokenVisitor, parsers);
                nestedSequencesParser.parent = this;
            }

            return nestedSequencesParser;
        }

        throw unexpected(ch);
    }

    @Override
    protected void writeSyntaxReport(final Map<String, Object> map) {
        map.put("activeParser", activeParser);
        map.put("parsers", parsers);
        map.put("state", state);
    }

    private static boolean isControl(final int ch) {
        return ch <= 32;
    }

}
