# TOOLS.md - Local Notes

## GitHub
- **Token:** `GH_TOKEN` in `~/.openclaw/.env`
- **Repos:** `github.com/openfugjoobot/*`
- **CLI:** `gh` command available

## Google Workspace (gog)
- **Account:** openfugjoobot@gmail.com
- **Services:** gmail, calendar, drive, contacts, sheets, docs
- **Password:** `GOG_KEYRING_PASSWORD` in `~/.openclaw/.env`
- **Timezone:** Europe/Rome (add +1h to UTC, +2h in summer)

## CoinGecko API
- **Key:** `COINGECKO_API_KEY` in `~/.openclaw/.env`
- **Rate Limit:** ~10-30 calls/min (free tier)
- **Endpoint:** `https://api.coingecko.com/api/v3/coins/markets`

## Blogwatcher (RSS)
- **Install:** `go install github.com/Hyaxia/blogwatcher/cmd/blogwatcher@latest`
- **Feeds:** 12 feeds (Lokal, Krypto, Tech, F1)
- **Cron:** Stündlich um :15 (hourly scan)
- **Workflow:** Scan → Vorschau → Artikel laden → Als gelesen markieren

## Weather (wttr.in)
- **Location:** Neumarkt-Egna (Südtirol)
- **Endpoint:** `https://wttr.in/Neumarkt+Südtirol`
- **No API key required**

## Brave Search
- **Key:** `BRAVE_API_KEY` in `~/.openclaw/.env`
- **Status:** ✅ Working (2026-03-08 fix)
- **Note:** Use `BRAVE_API_KEY` (not `BRAVE_SEARCH_API_KEY`)

## Tavily Search
- **Key:** `TAVILY_API_KEY` in `~/.openclaw/.env`
- **Location:** `~/.openclaw/workspace/skills/openclaw-tavily-search/`
- **Installed:** Via ClawHub (`npx clawhub install openclaw-tavily-search`)
- **Usage:** Backup when Brave is rate-limited

## Transit (Südtirol)
- **Skill:** `~/.openclaw/skills/suedtirol-fahrplan/`
- **Scripts:** `bin/transit-report.js`, `bin/bozen-bahnhof.js`
- **Cron:** Mo/Mi/Fr 07:45 + 16:15

### Cron Jobs
| Job | Schedule | Purpose | Status |
|-----|----------|---------|--------|
| blogwatcher-hourly | */15 * * * * | RSS Feed Scan | ✅ Active |
| system-health-check | 0 */4 * * * | System Check → Topic 44 | ✅ Active |

## OpenClaw Config
- **Path:** `~/.openclaw/openclaw.json`
- **Backup:** Auto-created on `config.patch`
- **Safe Update:** `openclaw gateway call config.patch`

---

## Agents (8-Team)

| Agent | Model | Workspace |
|-------|-------|-----------|
| main | qwen3.5:cloud 👁️ | workspace |
| dev-orchestrator | kimi-k2.5:cloud | workspace-orchestrator |
| research | gemini-3-flash-preview:cloud | workspace-research |
| architect | glm-5:cloud | workspace-architect |
| backend | qwen3-coder:480b-cloud | workspace-backend |
| frontend | qwen3-coder-next:cloud | workspace-frontend |
| qa | qwen3.5:cloud 👁️ | workspace-qa |
| docs | minimax-m2.5:cloud | workspace-docs |
| devops | glm-4.7:cloud | workspace-devops |
