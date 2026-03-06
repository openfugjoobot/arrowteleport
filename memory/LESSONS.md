# LESSONS.md - Lessons Learned

Format:
```markdown
### [YYYY-MM-DD] Short title
- **Context:** What happened?
- **Solution:** What was done?
- **For future:** Concrete rule/statement
```

---

### [2026-03-06] Brave Search API Endpoint deprecated
- **Context:** `web_search` Tool lieferte Fehler 422/403 (invalid subscription token). Direkter API-Test zeigte: Key funktioniert nur mit neuem Endpoint `/res/v1/web/search`, OpenClaw nutzt aber hartkodiert `/api/web/search`.
- **Solution:** Tavily Search als Workaround-Skill erstellt. Funktioniert sofort mit `TAVILY_API_KEY`.
- **For future:** Falls Brave weiterhin fehlschlägt → Tavily nutzen oder auf OpenClaw-Update mit baseUrl-Config warten (GitHub Issue #19075).

---

### [2026-03-06] Gateway Config Updates - Safe Mode
- **Context:** Direktes `write`/`edit` an `openclaw.json` riskant (kann Gateway crashen).
- **Solution:** `gateway config.patch` mit `baseHash` nutzen (Optimistic Locking). Automatische Validierung + optionaler Restart.
- **For future:** Nie direkt Config files editieren – immer `config.patch` verwenden.

---

### [2026-03-06] blogwatcher RSS Feed Setup
- **Context:** 9 Feeds hinzugefügt (Stol.it, Suedtirolnews, Crypto-Seiten). Erster Scan: 231 ungelesene Artikel.
- **Solution:** `blogwatcher add` für jeden Feed, dann `blogwatcher scan`.
- **For future:** Für regelmäßige Updates: Cron-Job mit `blogwatcher scan` + Telegram-Notification.

---

### [2026-03-06] Git Workflow bei Memory-Files
- **Context:** Viele Memory-Dateien entstanden durch Tests/Sessions. Uncommitted files verursachen Warnungen in Heartbeat.
- **Solution:** Am Ende des Tages `git add -A && git commit -m "memory logs YYYY-MM-DD"`.
- **For future:** Heartbeat sollte regelmäßig `git status` checken und bei >10 uncommitted files warnen.

---

### [2026-03-06] Geteilte Kalender in gog
- **Context:** `gog calendar list` zeigt nur primären Kalender. Geteilte/importierte Kalender haben separate IDs.
- **Solution:** `gog calendar calendars` zeigt alle Kalender mit IDs → dann `gog calendar events <kalender-id>`.
- **For future:** Importierte Kalender immer über spezifische ID ansprechen, nicht über "primary".

---

### [2026-03-06] API Key Testing via curl
- **Context:** `web_search` Tool Fehler schwer zu debuggen (abstrakte Fehlermeldungen).
- **Solution:** Direkter curl-Test: `curl -H "X-Subscription-Token: $KEY" https://api.search.brave.com/...` mit `-w "\nHTTP_CODE: %{http_code}"`.
- **For future:** Bei API-Problemen immer curl als ersten Debug-Schritt nutzen.