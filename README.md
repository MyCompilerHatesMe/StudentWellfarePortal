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
- Jakarta Validation

---

## Features

- Four roles: `ADMIN`, `FACULTY`, `STUDENT`, `PARENT` — each with their own endpoint access
- JWT-based stateless auth via a custom filter chain
- Parent accounts are scoped — they can only access data for students they're linked to
- Grades and CGPA are dynamically calculated from marks records
- `AuthUser` handles auth. Separate profile entities (`StudentProfile`, `FacultyProfile`, etc.) handle everything else,
  linked via `@MapsId`
- Global exception handler so errors come back in a consistent format

### Todo

- Error responses currently return a plain string. Should be a structured `ErrorResponse` JSON object
  for consistent frontend consumption, e.g. `{ "status": 400, "message": "..." }`

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

## API Overview

All endpoints require a Bearer JWT token unless stated otherwise.
Where `{username}` is not in the path, the authenticated user's identity is used from the security context.

### Auth — `/auth`

| Method |    Path     | Auth |
|:------:|:-----------:|:----:|
|  POST  | `/register` | None |
|  POST  |  `/login`   | None |

### Student — `/student`

| Method |         Path          |
|:------:|:---------------------:|
|  GET   | `/{username}/profile` |
|  GET   |  `/{username}/marks`  |
|  GET   | `/{username}/faculty` |
|  PUT   | `/{username}/profile` |

### Faculty — `/faculty`

| Method |                     Path                     |
|:------:|:--------------------------------------------:|
|  GET   |                  `/profile`                  | 
|  GET   |                 `/students`                  |
|  GET   |      `/student/{studentUsername}/marks`      |
|  PUT   | `/student/{studentUsername}/marks/{subject}` |
|  PUT   |                  `/profile`                  |

### Parent — `/parent`

| Method |            Path            |
|:------:|:--------------------------:|
|  GET   |         `/profile`         |
|  GET   |          `/wards`          |
|  GET   | `/ward/{username}/profile` |
|  GET   |  `/ward/{username}/marks`  |
|  PUT   |         `/profile`         |

### Admin — `/admin`

| Method |    Path    |
|:------:|:----------:|
|  POST  | `/enroll`  |
|  POST  | `/connect` |

---

## Notes

This is a portfolio project focused on backend architecture, authentication, and clean code.

### First Run Flow

Profiles are auto-created on registration. Students and faculty get their name from their log in username and a
generated university email. Parent accounts get placeholder email and number, a PUT to `/parent/profile` is expected to
fill these in.

### Known Limitations

- Roll number generation uses a count query and is not concurrency safe, a db sequence would be better for prod.
