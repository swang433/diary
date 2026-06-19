# Diary — Competitive Journaling (Java)

Overview
- Backend service for a diary / streak-tracking app implemented in Java.
- Focused on REST API, streak calculation, leaderboards, and privacy on entries.
- Intended as a minimal backend MVP; frontend is out of scope or provided separately.

Repository layout
- src/main/java — application source (Spring Boot / Jakarta EE style)
- src/main/resources — configuration and application properties
- README.md — this file

Implemented features
- User registration and login with JWT-based authentication
- CRUD for daily journal entries, with ownership checks enforced at the service layer
- Relational database support (PostgreSQL) via Spring Data JPA
- Streak calculation with current and best streak tracked per user

Not implemented / TODO
- Flyway migrations (schema is currently managed via `ddl-auto`, not version-controlled migrations)
- Entry visibility support: public, friends, and private
- Global leaderboard support, ranked by streak
- UI (mobile/web) — planned separately
- Social features (follow, comments, likes) — future
- Push/email reminders and notification webhooks — future
- Dockerfile — container image for the service
- Environment-variable injection for DB credentials and JWT secret (currently hardcoded in `application.properties`; flagged for production setup)

Quick start (local)
Prereqs: Java 17+, Maven, Postgres (or other configured RDBMS)

1. Configure `src/main/resources/application.properties` with your local Postgres connection details and a JWT secret.

2. Build
    mvn clean package

3. Run
    java -jar target/Diary-*.jar

4. Run tests
    mvn test

API (summary)
- [x] POST /auth/signup — create account
- [x] POST /auth/login — returns JWT 
- [x] GET /me/home — homepage with streaks and entries 
- [x] POST /entries/newjournal — create entry 
- [x] GET /entries/{id} — retrieve entry (ownership-checked) 
- [x] PUT /entries/{id} — update entry (ownership-checked) 
- [x] DELETE /entries/{id} — delete entry (ownership-checked) 
- [ ] GET /streaks/me — current / best streak for authenticated user (TODO)
- [ ] GET /leaderboards — global leaderboard (TODO)

Notes & considerations
- Timezone handling: entries are tied to a user's local date; ensure client supplies timezone or user profile has timezone.
- Multiple entries per day: streaks count based on at least one entry per local date.
- Security: ownership checks are enforced in the service layer for entry read/update/delete; entries cannot be accessed across users.

MVP checklist (current)
- [x] User signup/login
- [x] Create/view daily entry
- [x] Streak calculation per user
- [x] Ownership authorization on entry endpoints
- [ ] Leaderboard (global)
- [ ] Entry visibility (public/friends/private)
- [ ] Basic UI for journal and leaderboard