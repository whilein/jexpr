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

import io.github.whilein.jexpr.api.exception.SyntaxException;
import io.github.whilein.jexpr.api.token.TokenParser;
import lombok.val;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author whilein
 */
public abstract class AbstractTokenParser implements TokenParser {

    protected abstract void writeSyntaxReport(Map<String, Object> map);

    private String createSyntaxReport() {
        val map = new LinkedHashMap<String, Object>();

        writeSyntaxReport(map);

        return map.toString();
    }

    protected final SyntaxException unexpected(final int ch) {
        return invalidSyntax("Unexpected character '" + (char) ch + "'");
    }

    protected final SyntaxException invalidSyntax(final String message) {
        return invalidSyntax(message, null);
    }

    protected final SyntaxException invalidSyntax(final String message, final Throwable cause) {
        return new SyntaxException(message + " " + createSyntaxReport(), cause);
    }
}
