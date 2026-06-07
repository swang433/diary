Todo: Diary app — competitive journaling (streaks)

Core features
- User accounts (email/password, OAuth optional)
- Daily journal entry: create, edit, view
- Streak tracking: calculate consecutive days with at least one entry
- Public leaderboards: global and friends (rank by current streak and longest streak)
- Privacy settings per entry: public / friends / private

API & backend
- REST endpoints for entries, users, auth, leaderboards
- Background job to recalculate streaks (cron or scheduled task)
- Webhook or notification system for streak milestones
- Rate limits and abuse protection

Frontend
- Mobile-first web UI: journal composer, streak badge, leaderboard view
- Daily reminder notifications (email / push)
- Profile page showing current streak, best streak, history calendar

Data & storage
- Schema: users, entries (date, text, visibility), streaks, follows/friends
- Use a relational DB (Postgres) for consistency
- Optional cache (Redis) for leaderboards

Authentication & security
- JWT or session-based auth
- Email verification, password reset
- Protect endpoints and validate input

Metrics & monitoring
- Track DAU, weekly active users, average streak length
- Alert on job failures (streak calc) and API errors

Devops & CI
- GitHub Actions: build, tests, Docker image publish
- Dockerfile for runtime and optional multi-stage build
- Deploy targets: Cloud Run / ECS / Heroku

MVP checklist (short-term)
- [ ] User signup/login
- [ ] Create/view daily entry
- [ ] Streak calculation per user
- [ ] Leaderboard (global)
- [ ] Privacy for entries
- [ ] Basic UI for journal and leaderboard

Future enhancements
- Social features: follow, comment, like
- Competitions: time-limited challenges with rewards
- Gamification: badges, levels, streak insurance
- Data export and backup

Notes
- Define timezone handling early (entries tied to user local date)
- Decide if multiple entries per day count (one per day vs multiple)

If you want, I can implement the MVP endpoints and a basic UI next.