## jexpr

Библиотека позволяет вам обрабатывать и выполнять выражения. Синтаксис практически
идентичен синтаксису Java.

Данные пишутся в `TokenParser`, результат своей деятельности он пишет
в `TokenVisitor`, реализацией которого является `ExpressionParser`.

## Примеры использования

> Выполнение арифметического выражения с известными числами

```java
class Example {
    @Test
    public void testArithmeticEvaluation() {
        ExpressionParser expressionParser = DefaultExpressionParser.create();
        TokenParser tokenParser = SequenceTokenParser.createDefault(expressionParser);

        TokenParserUtils.update(tokenParser, "5 + 5 * 5");

        assertEquals(30, expressionParser.getResult().getValue());
    }
}
```

> Выполнение арифметического выражения с неизвестными числами

```java
class Example {
    @Test
    public void testArithmeticEvaluation() {
        ExpressionParser expressionParser = DefaultExpressionParser.create();
        TokenParser tokenParser = SequenceTokenParser.createDefault(expressionParser);

        TokenParserUtils.update(tokenParser, "x + y * z");

        Operand dynamicOperand = expressionParser.getResult();

        Operand solvedOperand = dynamicOperand.solve(value -> {
            switch (value) {
                case "x":
                    return 1;
                case "y":
                    return 2;
                case "z":
                    return 3;
                default:
                    throw new IllegalArgumentException(value);
            }
        });

        assertEquals(7, solvedOperand.getValue());
    }
}
```

## Лицензия

Код под лицензией [Apache License 2.0](LICENSE)