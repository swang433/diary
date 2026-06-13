# Diary — Competitive Journaling (Java)

Overview
- Backend service for a diary / streak-tracking app implemented in Java.
- Focused on REST API, streak calculation, leaderboards, and privacy on entries.
- Intended as a minimal backend MVP; frontend is out of scope or provided separately.

Repository layout
- src/main/java — application source (Spring Boot / Jakarta EE style)
- src/main/resources — configuration, Flyway migrations, application properties
- Dockerfile — container image for the service
- .github/workflows — CI (build & test)
- README.md — this file

Implemented features
- User accounts with JWT-based auth (register / login)
- CRUD for daily journal entries (visibility: public / friends / private)
- Streak calculation and endpoint to query current / best streak
- Global leaderboard endpoint (rank by current streak / longest streak)
- Database migrations (Flyway) and relational DB support (Postgres)

Not implemented / TODO
- UI (mobile/web) — planned separately
- Social features (follow, comments, likes) — future
- Push/email reminders and notification webhooks — future

Quick start (local)
Prereqs: Java 17+, Maven, Postgres (or other configured RDBMS)

1. Configure environment variables (example)
    - DATABASE_URL=jdbc:postgresql://localhost:5432/diary
    - DATABASE_USER=diary
    - DATABASE_PASSWORD=secret
    - JWT_SECRET=a-very-secret-key
    - SERVER_PORT=8080

2. Build
    mvn clean package

3. Run
    java -jar target/diary-*.jar

4. Run tests
    mvn test

Docker
- Build:
  docker build -t diary-service .
- Run:
  docker run -e DATABASE_URL=... -e DATABASE_USER=... -e DATABASE_PASSWORD=... -e JWT_SECRET=... -p 8080:8080 diary-service

API (summary)
- POST /api/auth/signup — create account ✅
- POST /api/auth/login — returns JWT ✅
- GET /api/users/me — get current user
- GET /api/entries — list entries (query by date, visibility)
- POST /api/entries — create entry ✅
- PUT /api/entries/{id} — update entry ✅
- DELETE /api/entries/{id} — delete entry ✅
- GET /api/streaks/me — current / best streak for authenticated user
- GET /api/leaderboards — global leaderboard (by streak)

Notes & considerations
- Timezone handling: entries are tied to a user's local date; ensure client supplies timezone or user profile has timezone.
- Multiple entries per day: streaks count based on at least one entry per local date.
- Security: validate input and protect endpoints; rate-limit as needed.

Development & CI
- GitHub Actions builds and runs tests on push/PR.
- Dockerfile supports building a runtime image for deployments.

MVP checklist (current)
- [x] User signup/login
- [ ] Create/view daily entry
- [x] Streak calculation per user
- [ ] Leaderboard (global)
- [ ] Privacy for entries
- [ ] Basic UI for journal and leaderboard