# Todo List API

PET-проект для изучения основ создания бэкенда на Java. REST API для работы с задачами (TODO List): создание, чтение, изменение и удаление записей.

## Стек технологий

- **Java 25**
- **Spring Boot 4.2.0**
- **Maven**
- **PostgreSQL 16** (в Docker)
- **Hibernate / Spring Data JPA**
- **Flyway** — версионирование схемы БД
- **MapStruct** — маппинг Entity ↔ DTO
- **Springdoc OpenAPI (Swagger UI)** — документация API
- **JUnit 5 + Mockito** — тесты
- **Docker / Docker Compose**

## Возможности API

| Метод  | Эндпоинт             | Описание                              |
|--------|----------------------|---------------------------------------|
| POST   | `/api/tasks`         | Создать задачу                        |
| GET    | `/api/tasks`         | Получить список задач (с пагинацией)  |
| GET    | `/api/tasks/{id}`    | Получить задачу по id                 |
| PUT    | `/api/tasks/{id}`    | Обновить задачу                       |
| DELETE | `/api/tasks/{id}`    | Удалить задачу                        |

Поддерживается пагинация и сортировка через query-параметры:

```
GET /api/tasks?page=0&size=20&sort=createdAt,desc
```

## Этапы работы над проектом

### Этап 1: Инициализация проекта и настройка окружения
1. Создание проекта Spring Boot (Spring Initializr).
2. Настройка Docker для PostgreSQL (`docker-compose.yml`).
3. Конфигурация приложения (`application.properties`).

### Этап 2: Проектирование слоев и модель данных
1. Создание сущности `Task` (Domain/Entity).
2. Создание DTO: `TaskRequestDTO`, `TaskResponseDTO`.
3. Создание репозитория `TaskRepository`.

### Этап 3: Реализация бизнес-логики (Service Layer)
1. Создание `TaskService` с CRUD-операциями и транзакциями.

### Этап 4: Реализация REST API (Controller Layer)
1. Создание `TaskController` с REST-эндпоинтами.
2. Централизованная обработка исключений (`GlobalExceptionHandler`, `ErrorResponse`).

### Этап 5: Тестирование и проверка
1. Написание тестов: unit-тесты сервиса (JUnit 5 + Mockito), slice-тесты контроллера (`@WebMvcTest` + MockMvc).
2. Ручная проверка эндпоинтов (curl / Postman / Swagger UI).

### Этап 6: Документирование API (Swagger / OpenAPI)
Подключён `springdoc-openapi-starter-webmvc-ui`. Интерактивная документация доступна по адресу `/swagger-ui.html`.

### Этап 7: Миграции базы данных (Flyway)
Схема БД управляется миграциями в `src/main/resources/db/migration/`. Hibernate переведён в режим `validate`.

### Этап 8: MapStruct для маппинга Entity ↔ DTO
Маппинг вынесен в `TaskMapper` (генерируется на этапе компиляции). Ручной маппинг в сервисе заменён на вызовы маппера.

### Этап 9: Пагинация и сортировка для GET /api/tasks
`GET /api/tasks` возвращает `Page<TaskResponseDTO>` с поддержкой параметров `page`, `size`, `sort`.

### Этап 10: Dockerfile для приложения
Написан multi-stage `Dockerfile`. Сервис `app` добавлен в `docker-compose.yml`, приложение и БД поднимаются одной командой.

## Запуск проекта

### Требования
- Docker Desktop (или Docker Engine + Docker Compose)

### Запуск через Docker Compose

```bash
docker compose up --build
```

Команда собирает образ приложения (multi-stage build) и поднимает два контейнера:
- `todo-postgres` — PostgreSQL 16;
- `todo-app` — Spring Boot приложение.

После запуска:
- API доступен по адресу `http://localhost:8080/api/tasks`
- Swagger UI — `http://localhost:8080/swagger-ui.html`

Чтобы остановить:

```bash
docker compose down
```

Если нужно начать с чистой базы (удалить volume с данными):

```bash
docker compose down -v
docker compose up --build
```

### Локальный запуск (без Docker для приложения)

1. Поднять только PostgreSQL:
   ```bash
   docker compose up -d postgres
   ```
2. Запустить приложение:
   ```bash
   ./mvnw spring-boot:run
   ```
   (Windows: `.\mvnw.cmd spring-boot:run`)

## Тесты

```bash
./mvnw test
```

(Windows: `.\mvnw.cmd test`)

## Структура проекта

```
src/main/java/com/example/todolist/
├── domain/        # JPA-сущности
├── dto/           # Request/Response DTO
├── repository/    # Spring Data JPA репозитории
├── service/       # Бизнес-логика
├── mapper/        # MapStruct-мапперы
├── web/           # REST-контроллеры и обработка ошибок
└── exception/     # Доменные исключения и формат ошибок
```

## Лицензия

Учебный проект. Свободно используется в образовательных целях.