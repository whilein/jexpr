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
import io.github.whilein.jexpr.operand.Operand;
import io.github.whilein.jexpr.operand.defined.OperandDouble;
import io.github.whilein.jexpr.operand.defined.OperandFloat;
import io.github.whilein.jexpr.operand.defined.OperandInteger;
import io.github.whilein.jexpr.operand.defined.OperandLong;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class NumberTokenParser extends AbstractTokenParser implements SelectableTokenParser {

    ByteArrayOutput buffer;

    private static final int TYPE_INT = 0, TYPE_LONG = 1, TYPE_FLOAT = 2, TYPE_DOUBLE = 3;
    private static final int STATE_DETERMINE_RADIX_0 = 0, STATE_DETERMINE_RADIX_1 = 1, STATE_CONSUME = 2;

    private static final int RADIX_UNKNOWN = 0,
            RADIX_BINARY = 2,
            RADIX_OCTAL = 8,
            RADIX_HEXADECIMAL = 16,
            RADIX_DECIMAL = 10;

    @NonFinal
    int type = TYPE_INT;

    @NonFinal
    int state = STATE_DETERMINE_RADIX_0;

    @NonFinal
    int radix = RADIX_UNKNOWN;

    @NonFinal
    int prevCharacter;

    @NonFinal
    int character;

    @NonFinal
    boolean illegalOctalNumber = false;

    @NonFinal
    boolean hasExponent = false;

    @NonFinal
    boolean completed = false;

    private boolean isReal() {
        return type == TYPE_FLOAT || type == TYPE_DOUBLE;
    }

    @Override
    public boolean shouldStayActive(final int ch) {
        return !completed && (ch == '.' || ch == '_'
                || (ch >= '0' && ch <= '9')
                || (ch >= 'a' && ch <= 'z')
                || (ch >= 'A' && ch <= 'Z')
                || (ch == '-' || ch == '+') && hasExponent && (character == 'e' || character == 'E'));
    }

    @Override
    public boolean shouldActivate(final int ch) {
        return ch == '.' || (ch >= '0' && ch <= '9');
    }

    @Override
    public void update(final int ch) {
        prevCharacter = character;
        character = ch;

        final boolean bypass;

        if (ch == '_') {
            return;
        }

        // число с плавающей точкой
        if (ch == '.') {
            if (isReal()) {
                throw invalidSyntax("got unexpected '.' but number is real");
            }

            setReal(TYPE_DOUBLE);
            bypass = true;
        } else if ((ch == '-' || ch == '+') && hasExponent && (prevCharacter == 'e' || prevCharacter == 'E')) {
            bypass = true;
        } else if (isReal() && (ch == 'e' || ch == 'E')) {
            if (hasExponent) {
                throw invalidSyntax("got unexpected '<exp>' but number already has exponent");
            }

            bypass = hasExponent = true;
        } else {
            bypass = false;
        }

        if (state == STATE_DETERMINE_RADIX_0) {
            if (ch == '0') {
                state = STATE_DETERMINE_RADIX_1;
                return;
            } else {
                setRadix(RADIX_DECIMAL);
            }
        } else if (state == STATE_DETERMINE_RADIX_1) {
            // 0F, 0D, 0L
            if (tryEnding(ch)) {
                return;
            }

            switch (ch) {
                case 'b':
                case 'B':
                    setRadix(RADIX_BINARY);
                    return;
                case 'x':
                case 'X':
                    setRadix(RADIX_HEXADECIMAL);
                    return;
                case '8':
                case '9':
                    illegalOctalNumber = true;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    buffer.put('0');
                    buffer.put(ch);

                    setRadix(RADIX_OCTAL);
                    return;
                default:
                    throw unexpected(ch);
            }
        }

        if (!bypass) {
            if (tryEnding(ch)) {
                return;
            }

            if (!isValidNumber(ch)) {
                if (radix != 8 || (ch != '8' && ch != '9')) {
                    throw invalidSyntax("Character " + (char) ch + " not in radix " + radix);
                }

                // поскольку может быть число 09.9, мы разрешаем через костыль такие числа
                // в конце мы проверим, если illegalOctalNumber = true, то выкинем исключение
                illegalOctalNumber = true;
            }
        }

        buffer.put(ch);
    }

    private boolean tryEnding(final int ch) {
        if (isRealRadix()) {
            val realEnding = getRealEnding(ch);

            if (realEnding != TYPE_INT) {
                setReal(realEnding);

                return completed = true;
            }
        }

        if (ch != 'l' && ch != 'L') {
            return false;
        }

        if (type != TYPE_INT) {
            // была обработана точка, значит число с плавающей точкой
            // оно не может быть лонгом
            throw invalidSyntax("Cannot read long from floating-point number");
        }

        if (state == STATE_DETERMINE_RADIX_1) {
            buffer.put('0');
            state = STATE_CONSUME;
        }

        if (radix == RADIX_UNKNOWN) {
            radix = RADIX_DECIMAL;
        }

        type = TYPE_LONG;
        return completed = true;
    }

    private boolean isRealRadix() {
        return radix == RADIX_UNKNOWN || radix == RADIX_OCTAL || radix == RADIX_DECIMAL;
    }

    private int getRealEnding(final int ch) {
        switch (ch) {
            case 'f':
            case 'F':
                return TYPE_FLOAT;
            case 'd':
            case 'D':
                return TYPE_DOUBLE;
            default:
                return TYPE_INT;
        }
    }

    private void setReal(final int type) {
        illegalOctalNumber = false;

        if (state == STATE_DETERMINE_RADIX_1) {
            buffer.put('0');
        }

        // число с плавающей точкой не может быть не в десятиричной системе счисления
        setRadix(RADIX_DECIMAL);

        this.type = type;
    }

    private boolean isValidNumber(final int ch) {
        return Character.digit(ch, radix) >= 0;
    }

    private void setRadix(final int radix) {
        this.radix = radix;

        if (state == STATE_DETERMINE_RADIX_0 || state == STATE_DETERMINE_RADIX_1) {
            state = STATE_CONSUME;
        }
    }

    private static Operand createNumber(final String text, final int type, final int radix) {
        switch (type) {
            default:
            case TYPE_INT:
                return OperandInteger.valueOf(Integer.parseInt(text, radix));
            case TYPE_DOUBLE:
                return OperandDouble.valueOf(Double.parseDouble(text));
            case TYPE_FLOAT:
                return OperandFloat.valueOf(Float.parseFloat(text));
            case TYPE_LONG:
                return OperandLong.valueOf(Long.parseLong(text, radix));
        }
    }

    @Override
    public @NotNull Operand doFinal() {
        try {
            if (illegalOctalNumber) {
                throw invalidSyntax("Invalid octal number (leading zero?)");
            }

            final Operand number;

            if (state == STATE_DETERMINE_RADIX_1) {
                number = OperandInteger.valueOf(0);
            } else {
                val text = buffer.getString();
                buffer.reset();

                try {
                    number = createNumber(text, type, radix);
                } catch (final NumberFormatException nfe) {
                    throw invalidSyntax("Cannot parse number", nfe);
                }
            }

            return number;
        } finally {
            type = TYPE_INT;
            radix = RADIX_UNKNOWN;
            state = STATE_DETERMINE_RADIX_0;
            completed = false;
            illegalOctalNumber = false;
            hasExponent = false;
            character = prevCharacter = 0;
        }
    }

    @Override
    protected void writeSyntaxReport(final Map<String, Object> map) {
        map.put("type", type);
        map.put("radix", radix);
        map.put("state", state);
        map.put("completed", completed);
        map.put("illegalOctalNumber", illegalOctalNumber);
        map.put("hasExponent", hasExponent);
        map.put("character", (char) character);
        map.put("prevCharacter", (char) prevCharacter);
    }
}
