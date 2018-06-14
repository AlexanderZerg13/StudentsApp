# Особенности модульной структуры проекта.
Пакет ru.infocom.university.modules содержит в себе модули приложения из которых формируется пункты навигационного меню и утилитарные классы и интерфес:
- Интерфейс Module - интерфейс для реализации модуля (подробнее в ru.infocom.university.modules.Module)
- Класс Requirement - требование необходимое для модуля. Метод getModuleRequirements модуля возвращает массив требований (ru.infocom.university.modules.Requirement)
- Класс ModulesConfig - содержит статичные методы для конфигурации досутпных модулей и итеграции их в навигационное меню (ru.infocom.university.modules.ModulesConfig)

Модели приложения:
- ru.infocom.university.modules.[moduleName].model - пакет для хранения моделей конкретного модуля (Пакеты soap-request/response)
- ru.infocom.university.model - общие модели приложения

# Работа с веб-сервесом.
Работа с данными веб-сервесов происходит посредством класса ru.infocom.university.network.DataRepository (Используется RxJava, Retrofit2, OkHttp).
Модуль напрямую (Обычно при инициализации) вызывает соответсвующие методы DataRepository для получения необходимых данных (Кэширование в БД не используется).

# Обработка ошибок DataRepository
DataRepository может генерировать разные ошибки при выполнение запроса к веб-сервису.
Для преобразования ошибки в текст для вывода используется класс ru.infocom.university.network.ExceptionUtils.
При отсутсвие данных генерируется исключение ru.infocom.university.network.EmptyDataException. За обработку ошибок отвечает модуль.
