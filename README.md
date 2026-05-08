# Problem Manager Platform

Backend-платформа для управления пользователями и задачами с микросервисной архитектурой.

Проект разрабатывается как pet-project для изучения и практического применения Java Backend-разработки: Spring Boot, REST API, JPA/Hibernate, транзакций, DTO, валидации, обработки ошибок, ACL и тестирования.

---

## О проекте

Платформа предназначена для управления пользователями и задачами в контексте обучения алгоритмическому программированию.

В будущем система должна помогать преподавателям и ученикам:

- создавать и хранить задачи;
- группировать задачи в банки;
- управлять доступом к задачам;
- хранить условия, решения и валидаторы;
- искать задачи по темам, сложности и другим признакам.

Проект построен как микросервисная система, где каждый сервис отвечает за свою область.

---

## Архитектура

Используется многомодульная Maven-структура:

```text
my-project/
├── common/
├── user-service/
├── problem-service/
└── pom.xml
```

Основной архитектурный подход внутри сервисов:

```text
Controller → DTO → Service → Repository → Database
```

Основные принципы:

- разделение ответственности между слоями;
- изоляция Entity от внешнего API через DTO;
- централизованная обработка ошибок;
- транзакционная бизнес-логика;
- базовый ACL для управления доступом к задачам;
- unit-тестирование бизнес-логики.

---

## Технологии

### Backend

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation

### Database

- H2 для локальной разработки
- PostgreSQL планируется для production/dev через Docker

### Build & Tools

- Maven multi-module
- Git
- JUnit 5
- Mockito

---

## Модули проекта

### common

Общий модуль с переиспользуемыми компонентами:

- `BaseException`
- `ErrorResponse`
- `GlobalExceptionHandler`
- обработка бизнес-ошибок;
- обработка validation errors;
- обработка ошибок целостности данных;
- fallback для неожиданных исключений.

---

### user-service

Микросервис пользователей.

Реализовано:

- CRUD API для пользователей;
- DTO для create/update/patch/response;
- валидация имени, фамилии и email;
- проверка уникальности email;
- ограничение уникальности email на уровне БД;
- транзакционная бизнес-логика;
- кастомные исключения;
- unit-тесты базовой бизнес-логики.

Пример API:

```text
POST   /users
GET    /users
GET    /users/{id}
PUT    /users/{id}
PATCH  /users/{id}
DELETE /users/{id}
```

---

### problem-service

Микросервис задач.

Реализовано:

- CRUD API для задач;
- DTO для create/update/patch/response;
- валидация входных данных;
- уровни сложности задач:
  - general difficulty;
  - school difficulty;
  - ICPC difficulty;
- базовая ACL-модель доступа;
- роли доступа:
  - `READER`;
  - `MODERATOR`;
  - `OWNER`;
- автоматическая выдача `OWNER`-доступа создателю задачи;
- получение только доступных пользователю задач;
- запрет изменения задачи без прав;
- удаление задачи только владельцем;
- удаление ACL-записей при удалении задачи;
- unit-тесты для `ProblemService`.

Пример API:

```text
POST   /problems
GET    /problems?userId={userId}
GET    /problems/{id}?userId={userId}
PUT    /problems/{id}?userId={userId}
PATCH  /problems/{id}?userId={userId}
DELETE /problems/{id}?userId={userId}
```

> Сейчас `userId` передаётся через request parameter как временное учебное решение. В дальнейшем планируется заменить это на Spring Security + JWT.

---

## ACL в problem-service

Для управления доступом используется отдельная сущность `ProblemAccess`.

```text
problemId + userId + role
```

Ограничения доступа:

| Роль | Читать | Изменять | Удалять |
|---|---:|---:|---:|
| NULL | no | no | no |
| READER | yes | no | no |
| MODERATOR | yes | yes | no |
| OWNER | yes | yes | yes |

На уровне БД добавлено ограничение уникальности:

```text
problem_id + user_id
```

Это гарантирует, что один пользователь не может иметь несколько ролей для одной и той же задачи одновременно.

---

## Тестирование

В проекте используются:

- JUnit 5
- Mockito
- Maven test lifecycle

Покрыта unit-тестами бизнес-логика `ProblemService`:

- создание задачи;
- автоматическое создание `OWNER`-доступа;
- получение задачи при наличии доступа;
- запрет доступа без ACL-записи;
- обновление задачи;
- частичное обновление задачи;
- удаление задачи только владельцем;
- запрет удаления для `READER`, `MODERATOR` и пользователя без доступа;
- получение списка только доступных пользователю задач.

Запуск всех тестов:

```bash
mvn clean install
```

Запуск тестов только для `problem-service`:

```bash
mvn -pl problem-service -am test
```

Запуск конкретного тестового класса:

```bash
mvn -pl problem-service -am -Dtest=ProblemServiceTest test
```

---

## Как запустить проект локально

### 1. Склонировать репозиторий

```bash
git clone https://github.com/MichsSS-G/my-project.git
cd my-project
```

### 2. Собрать проект

```bash
mvn clean install
```

### 3. Запустить user-service

В первом терминале:

```bash
cd user-service
mvn spring-boot:run
```

Сервис будет доступен на:

```text
http://localhost:8080
```

### 4. Запустить problem-service

Во втором терминале:

```bash
cd problem-service
mvn spring-boot:run
```

Сервис будет доступен на:

```text
http://localhost:8081
```

---

## Примеры запросов

### Создать пользователя

```bash
curl -i -X POST "http://localhost:8080/users" \
-H "Content-Type: application/json" \
-d "{\"name\":\"Ivan\",\"surname\":\"Grib\",\"email\":\"ivan@test.com\"}"
```

Ожидаемо:

```text
HTTP/1.1 201
```

---

### Получить пользователей

```bash
curl -i "http://localhost:8080/users"
```

Ожидаемо:

```text
HTTP/1.1 200
```

---

### Создать задачу

```bash
curl -i -X POST "http://localhost:8081/problems" \
-H "Content-Type: application/json" \
-d "{\"title\":\"Two Sum\",\"ownerId\":1,\"generalDifficulty\":\"EASY\",\"schoolDifficulty\":\"SCHOOL\",\"icpcDifficulty\":\"QUALIFICATION\"}"
```

Ожидаемо:

```text
HTTP/1.1 201
```

---

### Получить задачи пользователя

```bash
curl -i "http://localhost:8081/problems?userId=1"
```

Ожидаемо:

```text
HTTP/1.1 200
```

---

### Получить задачу по id

```bash
curl -i "http://localhost:8081/problems/1?userId=1"
```

Ожидаемо:

```text
HTTP/1.1 200
```

---

### Проверить запрет доступа

```bash
curl -i "http://localhost:8081/problems/1?userId=999"
```

Ожидаемо:

```text
HTTP/1.1 403
```

---

### Частично обновить задачу

```bash
curl -i -X PATCH "http://localhost:8081/problems/1?userId=1" \
-H "Content-Type: application/json" \
-d "{\"title\":\"Updated Two Sum\",\"generalDifficulty\":\"MEDIUM\"}"
```

Ожидаемо:

```text
HTTP/1.1 200
```

---

### Удалить задачу

```bash
curl -i -X DELETE "http://localhost:8081/problems/1?userId=1"
```

Ожидаемо:

```text
HTTP/1.1 204
```

---

## Что уже реализовано

- Multi-module Maven project
- `common` module
- `user-service`
- `problem-service`
- REST API
- DTO layer
- Bean Validation
- JPA/Hibernate
- H2 database for local development
- Transactional service layer
- Global exception handling
- Custom business exceptions
- ACL model for problems
- Unit tests for problem-service business logic

---

## Планы развития

Следующие этапы:

- integration-тесты для REST API;
- Mapper для DTO ↔ Entity;
- Spring Security + JWT;
- замена `userId` query parameter на authenticated user;
- file storage для statements, solutions и validators;
- Strategy pattern для разных реализаций файлового хранилища;
- problem-bank-service;
- Docker Compose для сервисов и PostgreSQL;

---
