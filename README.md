## jexpr

Библиотека позволяет вам обрабатывать и исполнять выражения. Синтаксис практически
идентичен синтаксису Java.

## Примеры использования

> Выполнение арифметического выражения с известными числами

```java
class Example {
    @Test
    public void testArithmeticEvaluation() {
        ExpressionParser expressionParser = SimpleExpressionParser.createDefault();

        assertEquals(30, expressionParser.parse("5 + 5 * 5").getValue());
    }
}
```

> Выполнение арифметического выражения с неизвестными числами

```java
class Example {
    @Test
    public void testArithmeticEvaluation() {
        ExpressionParser expressionParser = SimpleExpressionParser.createDefault();

        // Переменные запрашиваются через UndefinedResolver,
        // реализацией которого может быть всё что угодно.
        //
        // В данном примере, для простоты демонстрации,
        // мы получаем значения переменных из Map.

        Map<String, Operand> variables = Map.of(
                "x", OperandInteger.valueOf(1),
                "y", OperandInteger.valueOf(2),
                "z", OperandInteger.valueOf(3)
        );

        Operand undefinedOperand = expressionParser.parse("x + y * z");
        Operand solvedOperand = undefinedOperand.solve(variables::get);

        assertEquals(7, solvedOperand.getValue());
    }
}
```

## Некоторые замечания

### Строки

Строки можно сравнивать используя операторы `==` и `!=`.

В полной мере реализована
поддержка [Escape-последовательностей](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10.6),
например: `\0`, `\u89ab`, `\n`, `\\`, `\"`

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

- Префиксы `0x`, `0b` и `0[0-7]` у целых чисел
- Суффиксы `Ll`, `Ff`, `Dd`
- Начало числа с `.`
- Нижнее подчёркивание (`_`) в числе
- Экспоненциальная запись (`1e-2345`)

Отсутствует обработка чисел с плавающей точкой в 16ричной системе счисления.

### Ключевые слова

Псевдодинамичная сущность, которая используется на стадии обработки текста. Список ключевых слов и их значение
зафиксированы для парсера и не могут быть изменены.

Доступные ключевые слова по-умолчанию

- true
- false
- (TODO!!) null

### Функции

TODO

## Лицензия

Код под лицензией [Apache License 2.0](LICENSE)
