## jexpr

Библиотека позволяет вам обрабатывать и исполнять выражения. Синтаксис практически
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
        tokenParser.submit("5 + 5 * 5");

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
        tokenParser.submit("x + y * z");

        // Переменные запрашиваются через DynamicResolver,
        // реализацией которого может быть всё что угодно.
        //
        // В данном примере, для простоты демонстрации,
        // мы получаем значения переменных из Map.

        Map<String, Operand> variables = Map.of(
                "x", OperandInteger.valueOf(1),
                "y", OperandInteger.valueOf(2),
                "z", OperandInteger.valueOf(3)
        );

        Operand dynamicOperand = expressionParser.getResult();
        Operand solvedOperand = dynamicOperand.solve(variables::get);

        assertEquals(7, solvedOperand.getValue());
    }
}
```

## Некоторые замечания

### Сравнение строк

Единственное отличие от Java - возможность сравнивать строки используя
операторы `==` и `!=`.

### Операторы

На данный момент реализованы все операторы, которые существуют в Java,
за исключением тройного (тернарного) оператора.

| Категория      | Операторы                                                      |
|----------------|----------------------------------------------------------------|
| Побитовые      | `~` <code>&#124;</code> `&` `<<` `>>` `>>>` `^`                |
| Логические     | `!` <code>&#124;&#124;</code> `&&` `==` `!=` `>` `<` `>=` `<=` |
| Арифметические | `*` `/` `+` `-` `%`                                            |

### Числа

Синтаксис чисел полностью взят из Java

- Префиксы `0x`, `0b` и `0[0-7]`
- Суффиксы `Ll`, `Ff`, `Dd`
- Начало числа с `.`
- Нижнее подчёркивание (`_`) в числе
- Экспоненциальная запись (`1e-2345`)

### Ключевые слова

Доступные ключевые слова по-умолчанию

- true
- false
- (TODO!!) null

### Функции

TODO

## Лицензия

Код под лицензией [Apache License 2.0](LICENSE)