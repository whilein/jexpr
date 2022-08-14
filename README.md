## jexpr
Библиотека позволяет вам обрабатывать и выполнять выражения. Синтаксис практически
идентичен синтаксису Java.

## Документация
Данные пишутся в `TokenParser`, результат своей деятельности он пишет
в `TokenVisitor`, которым является `ExpressionParser`.

Например, чтобы посчитать сумму двух известных чисел, можно сделать следующее:
```java
class Example {
    @Test
    public void sumTwoNumbers() {
        ExpressionParser expressionParser = DefaultExpressionParser.create();
        TokenParser tokenParser = SequenceTokenParser.createDefault(expressionParser);
        
        TokenParserUtils.update(tokenParser, "5 + 5 * 5");
        
        assertEquals(30, expressionParser.getResult().getValue());
    }
}
```


## Лицензия
Код под лицензией [Apache License 2.0](LICENSE)