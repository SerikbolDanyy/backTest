# BackTest Project

## Описание проекта
`BackTest` — это сервис для отслеживания и управления транзакциями с использованием **Java 21**, **Spring Boot**, **PostgreSQL** и **Docker**. Сервис включает конвертацию валют и проверку лимитов транзакций с поддержкой работы через Docker.

---

## Требования
Для запуска убедитесь, что у вас установлено:
- **Java** 21+
- **Gradle** (или используйте встроенный `gradlew`)
- **Docker** и **Docker Compose**
- **PostgreSQL** (при локальном запуске без Docker)

---

## Установка и запуск

### 1. Локальная сборка и запуск

1. **Проверьте, что Java установлена**:
   ````bash
   java -version

2. **Соберите проект с помощью Gradle**:
   ````bash
   ./gradlew clean build
   
3. **Запустите приложение**:
   ````bash
   java -jar build/libs/backTest-0.0.1-SNAPSHOT.jar
4. **Проверьте работу приложения**: 
   * Приложение будет доступно по адресу: http://localhost:8080.

---

### 2. Запуск через Docker и Docker Compose

1. **Соберите и запустите контейнеры**:
   ````bash
   docker-compose up --build

2. **Проверьте, что контейнеры запущены**:
   ````bash
   docker ps

3. **Остановите контейнеры при необходимости**:
   ````bash
   docker-compose down
   
4. **Проверка доступа**:
   * Приложение будет доступно по адресу: http://localhost:8080.
   * База данных PostgreSQL доступна на порту 5432.

---

## Структура проекта
1.
   ````bash
   backTest/
   │
   ├── Dockerfile                # Конфигурация для сборки Docker-образа
   ├── docker-compose.yml        # Конфигурация для запуска через Docker Compose
   ├── build.gradle              # Файл сборки проекта Gradle
   ├── gradlew                   # Gradle wrapper для удобного запуска
   ├── src/
   │   ├── main/
   │   │   ├── java/             # Основной код на Java
   │   │   └── resources/
   │   │       └── db.changelog/ # Миграции базы данных (Liquibase)
   │   └── test/                 # Тесты проекта
   └── README.md                 # Документация (этот файл)

---

## API-эндпоинты
1. **Создать новую транзакцию**
   `POST /transactions`
    ````json
   {
    "account_from": "0000000123",
    "account_to": "9999999999",
    "currency_shortname": "KZT",
    "sum": 1500.00,
    "expense_category": "product",
    "datetime": "2024-10-28T10:00:00+06:00"
    }
   
2. Установить новый лимит
   `POST /limits`
    ```json
   {
    "limit_sum": 2000.00,
    "limit_currency_shortname": "USD",
    "limit_datetime": "2024-10-28T00:00:00+06:00"
    }

3. Получить все транзакции, превысившие лимит
   `GET /transactions/exceeded`
    ````json
    {
    "account_from": "0000000123",
    "account_to": "9999999999",
    "currency_shortname": "KZT",
    "sum": 1500.00,
    "expense_category": "product",
    "datetime": "2024-10-28T10:00:00+06:00",
    "limit_exceeded": true
    }


4. Получить текущие лимиты
   `GET /limits`
    ````json
    {
    "limit_sum": 2000.00,
    "limit_currency_shortname": "USD",
    "limit_datetime": "2024-10-28T00:00:00+06:00"
    }


---

## Миграции базы данных
В проекте используется Liquibase для управления миграциями базы данных.
1. **Миграции находятся в папке:**
   ````css
   src/main/resources/db.changelog/

2. **Чтобы выполнить миграции, просто запустите приложение — Liquibase автоматически применит все необходимые изменения при запуске.**
---

## Примечания и полезные команды

### Полезные команды для Docker
* Просмотреть логи контейнера:
   ````bash
   docker logs <container_id>

* Перезапустить контейнер:
   ````bash
  docker restart <container_id>

* Удалить все контейнеры и образы (при необходимости):
   ````bash
  docker system prune -a

### Отладка
Если возникнут проблемы при запуске:
* Убедитесь, что Docker работает и контейнеры запущены.
* Проверьте конфигурацию базы данных в файле `application.properties`.
* Если приложение не запускается, используйте команду `docker-compose logs` для просмотра логов контейнеров.

## Контакты
* Если у вас возникли вопросы или проблемы с проектом, обратитесь к разработчику:
*serikboldany@gmail.com*