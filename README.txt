Проект diff в моей реализации позволяет узнавать по двум текстам какие строки из первого должны быть удалены
и какие строки из второго должны быть вставлены, а также в какие места, чтобы из первого текста получить второй.

Запуск программы осуществляется с помощью IntelliJ IDEA, при компилировании fun main().

Реализация программы основана на алгоритме динамического программирования поиска наибольшей общей подпоследовательности
строк в двух файлах. Сравнения строк внутри алгоритма происходят с применением хеширования для улучшения быстродействия.
Инетрфейс программы написан на javax.swing.

Тексты для тестирования представлены в папке src/test
