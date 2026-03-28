# Student Welfare Portal

Backend REST API for managing student academic data, faculty, course enrollments, and parent access at a university.
Built with Spring Boot.

---

## Tech Stack

- Java 25, Spring Boot, Spring Security, Spring Data JPA
- PostgreSQL
- JWT authentication
- Lombok, MapStruct
- Gradle

---

## Features

- Four roles: `ADMIN`, `FACULTY`, `STUDENT`, `PARENT` — each with their own endpoint access
- JWT-based stateless auth via a custom filter chain
- Parent accounts are scoped — they can only access data for students they're linked to
- Grades and CGPA are dynamically calculated from marks records
- `AuthUser` handles auth. Separate profile entities (`StudentProfile`, `FacultyProfile`, etc.) handle everything else,
  linked via `@MapsId`
- Global exception handler so errors come back in a consistent format

---

## Running it

### Requirements

- JDK >= 25
- A running PostgreSQL instance

### Environment variables

```
JWT_SECRET_KEY
DB_URL
DB_USERNAME
DB_PASSWORD
```

### Run

```bash
git clone https://github.com/MyCompilerHatesMe/StudentWellfarePortal.git
cd StudentWellfarePortal
./gradlew bootRun
```

---

## Notes

This is a portfolio project focused on backend architecture, authentication, and clean code.