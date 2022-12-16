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

import io.github.whilein.jexpr.api.keyword.KeywordRegistry;
import io.github.whilein.jexpr.api.token.TokenVisitor;
import io.github.whilein.jexpr.io.ByteArrayOutput;
import io.github.whilein.jexpr.token.operand.OperandReference;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class ReferenceTokenParser extends AbstractSelectableTokenParser {

    KeywordRegistry keywordRegistry;

    ByteArrayOutput buffer;

    private static boolean isValidReferenceNameCharacter(final int ch) {
        return ch == '_' || ch == '$' || ch > 47
                && (ch < 58 || ch > 63) && (ch < 91 || ch > 96) && (ch < 123 || ch > 127);
    }

    @Override
    public boolean shouldStaySelected(final int ch) {
        return isValidReferenceNameCharacter(ch);
    }

    @Override
    protected boolean shouldSelect(final int ch) {
        return isValidReferenceNameCharacter(ch);
    }

    @Override
    public void update(final int ch) {
        buffer.put(ch);
    }

    @Override
    public void doFinal(final @NotNull TokenVisitor tokenVisitor) {
        try {
            val reference = buffer.getString();

            val keyword = keywordRegistry.get(reference);

            tokenVisitor.visitOperand(keyword == null
                    ? OperandReference.valueOf(reference)
                    : keyword.getOperandValue());
        } finally {
            buffer.reset();
        }
    }

    @Override
    protected void writeSyntaxReport(final Map<String, Object> map) {
    }
}
