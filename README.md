# Problem Manager Platform

Backend-платформа для управления пользователями и задачами с микросервисной архитектурой.

Проект разрабатывается с фокусом на изучение и применение best practices backend-разработки на Java (Spring Boot).

---

## О проекте

Платформа позволяет:

- управлять пользователями
- создавать и хранить задачи
- работать с контентом задач (условия, решения, валидаторы)
- управлять доступами (в разработке)

Проект построен как **микросервисная система**, где каждый сервис отвечает за свою область.

---

## Архитектура

Используется классическая многослойная архитектура:
Controller → Service → Repository → Database


### Основные принципы:
- разделение ответственности (SRP)
- изоляция доменной модели через DTO
- централизованная обработка ошибок
- транзакционная бизнес-логика

---

## Технологии

**Backend:**
- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA

**База данных:**
- PostgreSQL (production)
- H2 (dev)

**Сборка и инструменты:**
- Maven (multi-module)
- Git

---

## Микросервисы

### user-service
- CRUD операции с пользователями
- проверка уникальности email
- глобальная обработка ошибок
- DTO + валидация
- транзакции (@Transactional)

---

### problem-service (в разработке)
- управление задачами
- хранение контента (statement, solutions, validators)
- поддержка разных форматов (HTML/PDF/CPP)
- подготовка к масштабируемому хранению (файлы/облако)

---

## Реализованный функционал

- REST API (CRUD)
- DTO слой (разделение API и сущностей)
- GlobalExceptionHandler
- Валидация входных данных
- Транзакционная логика
- Маппинг DTO ↔ Entity
- Мультимодульная структура Maven

---

## В разработке (планы)

- ACL (система ролей: reader / moderator / admin)
- Problem Bank (группировка задач)
- File storage
- Авторизация и аутентификация (JWT)
- Dockerизация

---

## Как запустить микросервис пользователей

```bash
git clone https://github.com/MichsSS-G/my-project.git
cd my-project

mvn clean install

cd user-service
mvn spring-boot:run
```

Примеры запросов:

- Создание пользователя
```bash
curl -X POST http://localhost:8080/users \
-H "Content-Type: application/json" \
-d '{
  "name": "user_name",
  "surname": "user_surname",
  "email": "user_email@mail.com"
}'
```
- получение пользователя по id
```bash
curl http://localhost:8080/users/{$id}
```