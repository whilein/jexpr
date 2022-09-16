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

package io.github.whilein.jexpr.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
@UtilityClass
public class StringEscapeUtils {

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    public void escape(final @NotNull String string, final @NotNull StringBuilder out, final int quote) {
        for (int i = 0, j = string.length(); i < j; i++) {
            val ch = string.charAt(i);

            switch (ch) {
                case '\n':
                    out.append("\\n");
                    break;
                case '\r':
                    out.append("\\r");
                    break;
                case '\t':
                    out.append("\\t");
                    break;
                case '\f':
                    out.append("\\f");
                    break;
                case '\b':
                    out.append("\\b");
                    break;
                case '\\':
                    out.append("\\\\");
                    break;
                default:
                    if (ch < 32 || ch >= 127) {
                        out.append("\\u")
                                .append(HEX[(ch & 0xF000) >> 12])
                                .append(HEX[(ch & 0x0F00) >> 8])
                                .append(HEX[(ch & 0x00F0) >> 4])
                                .append(HEX[ch & 0x000F]);
                        break;
                    }

                    if (ch == quote) {
                        out.append("\\");
                    }

                    out.append(ch);
                    break;
            }
        }
    }


}
