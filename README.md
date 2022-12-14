## jexpr
![](https://img.shields.io/github/issues/whilein/jexpr)
![](https://img.shields.io/github/issues-pr/whilein/jexpr)

Библиотека позволяет вам обрабатывать и исполнять выражения. Синтаксис практически
идентичен синтаксису Java.

## Примеры использования

> Выполнение арифметического выражения с известными числами

```java
class Example {
    @Test
    public void testArithmeticEvaluation() {
        Jexpr jexpr = DefaultJexpr.create();

        assertEquals(30, jexpr.parse("5 + 5 * 5").getValue());
    }
}
```

> Выполнение арифметического выражения с неизвестными числами

```java
class Example {
    @Test
    public void testArithmeticEvaluation() {
        Jexpr jexpr = DefaultJexpr.create();

        // Переменные запрашиваются через UndefinedResolver,
        // реализацией которого может быть всё что угодно.
        //
        // В данном примере, для простоты демонстрации,
        // мы получаем значения переменных из Map.

        Map<String, Operand> variables = Map.of(
                "x", Operands.constantInt(1),
                "y", Operands.constantInt(2),
                "z", Operands.constantInt(3)
        );

        Operand operand = jexpr.parse("x + y * z");
        Operand solvedOperand = operand.solve(variables::get);

        assertEquals(7, solvedOperand.getValue());
    }
}
```

## Компиляция в байткод

С помощью модуля `jexpr-compiler` вы можете компилировать неопределённые выражения (содержащие переменные)
напрямую в байткод. Реализованы все операторы из `jexpr-core`.

```java
class Example {
    public void compile(MethodVisitor mv) {
        Jexpr jexpr = DefaultJexpr.create();
        JexprCompiler jexprCompiler = DefaultJexprCompiler.create();
        
        // парсим выражение
        Operand operand = jexpr.parse("variable + 1");

        // Указываем, что у нас находится в локальных переменных
        // они будут использованы компилятором
        LocalMap localMap = SimpleLocalMap.create()
                // в параметре 1 у нас целое число с именем variable.
                // при компиляции оно увидит неизвестное "variable" и 
                // запросит его на стек из локальных параметров
                .add("variable", 1, Type.INT_TYPE);

        jexprCompiler.compile(mv, localMap, operand);

        // Результат:
        //
        // ILOAD_1
        // ICONST_1
        // IADD
    }
}
```

Также есть упрощенный функционал, который может создать нужную реализацию функционального интерфейса.

```java
import java.util.function.IntBinaryOperator;

class Example {
    public IntBinaryOperator sum() {
        Jexpr jexpr = DefaultJexpr.create();
        JexprCompiler jexprCompiler = DefaultJexprCompiler.create();

        Operand operand = jexpr.parse("a + b");

        return jexprCompiler.implementInterface(IntBinaryOperator.class)
                .name(0, "a")
                .name(1, "b")
                .compile(operand);
    }
}
```

## Некоторые замечания

### Объекты

Объекты и строки можно сравнивать используя операторы `==` и `!=`, эти операторы будут заменены
на `equals` (даже при генерации байткода через `jexpr-compiler`).

Объекты можно вставить в `Operand` используя метод `solve`, но при генерации байткода
произвольные объекты недопустимы, поскольку их нельзя записать в формате байткода.

### Строки

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

Используется на стадии обработки текста. Список ключевых слов и их значение
зафиксированы для парсера и не могут быть изменены.

Доступные ключевые слова по-умолчанию:

| Название | Значение             |
|----------|----------------------|
| `true`   | OperandBoolean.TRUE  |
| `false`  | OperandBoolean.FALSE |
| `null`   | OperandObject.NULL   |

### Функции

TODO

## Лицензия

Код под лицензией [Apache License 2.0](LICENSE)
