# TOOLS.md - Local Notes

Skills define _how_ tools work. This file is for _your_ specifics — the stuff that's unique to your setup.

## What Goes Here

Things like:

- Camera names and locations
- SSH hosts and aliases
- Preferred voices for TTS
- Speaker/room names
- Device nicknames
- Anything environment-specific

## Examples

```markdown
### Cameras

- living-room → Main area, 180° wide angle
- front-door → Entrance, motion-triggered

### SSH

- home-server → 192.168.1.100, user: admin

### TTS

- Preferred voice: "Nova" (warm, slightly British)
- Default speaker: Kitchen HomePod
```

## Why Separate?

Skills are shared. Your setup is yours. Keeping them apart means you can update skills without losing your notes, and share skills without leaking your infrastructure.

---

Add whatever helps you do your job. This is your cheat sheet.

---

## Südtirol Fahrplan Skill

**Location:** `~/.openclaw/skills/suedtirol-fahrplan/`
**Repo:** `github.com/openfugjoobot/suedtirol-fahrplan`

### Scripts
| Script | Funktion |
|--------|----------|
| `bin/transit-report.js` | Abfahrten Neumarkt (Bahnhof + Busbahnhof + Trudner Bach) |
| `bin/bozen-bahnhof.js` | Abfahrten Bozen Bahnhof (nur Züge) |

### Cron Jobs (aktiv)
| Job | Zeit | Befehl |
|-----|------|--------|
| `daily-transit-neumarkt` | Mo/Mi/Fr 07:45 | `node ~/.openclaw/skills/suedtirol-fahrplan/bin/transit-report.js` |
| `daily-transit-bozen-bahnhof` | Mo/Mi/Fr 16:15 | `node ~/.openclaw/skills/suedtirol-fahrplan/bin/bozen-bahnhof.js` |

### Features
- Verzögerungen: `(+0)`, `(+5)`, `(-2)`
- Plattform nur wenn vorhanden: `(Gl. 3)` / (ausgelassen bei Bussen)
- Hinweise: 📍 = Haltestelle, ⚠️ = Linie, 📝 = Fahrt
- Countdown: `12min` (bis 15 Min vor Abfahrt)

---

## gog (Google Workspace)

Account: `openfugjoobot@gmail.com`
Services: gmail, calendar, drive, contacts, docs, sheets

**Keyring**
- Backend: file
- Password stored in: `~/.openclaw/.env` (GOG_KEYRING_PASSWORD)
- Auto-loaded via `~/.bashrc`

**Setup (wenn nötig)**
```bash
# Token exportieren (Backup)
gog auth tokens export openfugjoobot@gmail.com /tmp/token-backup.json

# Token importieren
export GOG_KEYRING_PASSWORD=openfugjoobot
gog auth tokens import /tmp/token-backup.json
```

**Quick Commands**
```bash
# Gmail
gog gmail search 'newer_than:7d' --max 10
gog gmail messages search "from:sender@example.com" --max 20

# Calendar
gog calendar events primary --from 2026-02-23 --to 2026-02-24
gog calendar create primary --summary "Meeting" --from 2026-02-24T10:00:00 --to 2026-02-24T11:00:00

# Drive
gog drive search "query" --max 10
gog drive list --max 20

# Sheets
gog sheets get SHEET_ID "Tab!A1:D10" --json
gog sheets update SHEET_ID "Tab!A1:B2" --values-json '[["A","B"]]'

# Docs
gog docs export DOC_ID --format txt --out /tmp/doc.txt
gog docs cat DOC_ID

# Contacts
gog contacts list --max 20
```

**Calendars**
- primary → Google Calendar (openfugjoobot@gmail.com)
- Ivan's Kalender → `64epruffnln347sqk5a2fjkeapr646dm@import.calendar.google.com` (M365 import, auto-sync)

**Timezone Note**
- gog returns UTC times (Z suffix)
- User timezone: Europe/Rome (UTC+1 / UTC+2 DST)
- Add +1 hour (or +2 in summer) to convert to local time

**Notes**
- Requires `GOG_KEYRING_PASSWORD` env var (auto-set via .bashrc)
- OAuth credentials: `~/.config/gogcli/credentials.json`
- Tokens: keyring file backend

---

## CoinGecko API

**API Key:** Stored in `~/.openclaw/.env` (COINGECKO_API_KEY)
- Free tier: 10-30 calls/minute, no strict limit
- Paid tier: Higher rate limits

**Endpoint:** `https://api.coingecko.com/api/v3/coins/markets`

**Common Parameters:**
- `vs_currency`: eur | usd
- `order`: market_cap_desc, volume_desc
- `per_page`: 10-250 (default 100)
- `page`: 1, 2, 3...
- `price_change_percentage`: 1h, 24h, 7d, 14d, 30d, 200d, 1y

**Quick Commands:**
```bash
# Top 10 coins with 1h and 24h change
curl -s "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&order=market_cap_desc&per_page=10&page=1&price_change_percentage=1h,24h"

# Specific coins by ID
curl -s "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&ids=bitcoin,ethereum,solana&price_change_percentage=24h"
```

**Response Fields:**
- `current_price`: Current price in vs_currency
- `price_change_percentage_1h_in_currency`: 1h change
- `price_change_percentage_24h_in_currency`: 24h change
- `market_cap`: Market capitalization

**Coins Tracked:**
- BTC: bitcoin
- ETH: ethereum
- SOL: solana
- XRP: ripple
- BNB: binancecoin
- DOGE: dogecoin
- TRX: tron

**Rate Limits:**
- Free: ~10-30 calls/minute
- If limited: Wait 1-2 minutes between calls

**Notes:**
- No API key required for basic calls (but recommended)
- Returns real-time data
- EUR prices available directly

---

## blogwatcher (RSS Feed Monitor)

**Install:** `go install github.com/Hyaxia/blogwatcher/cmd/blogwatcher@latest`

**Feeds Configured:**
- Stol.it (Südtirol News) - https://www.stol.it/rss
- Suedtirolnews (Südtirol News) - https://www.suedtirolnews.it/feed/
- Krypto-Magazin (Crypto DE) - https://www.krypto-magazin.de/feed/
- BTC-Echo (Crypto DE) - https://www.btc-echo.de/feed/
- Coin-Update (Crypto DE) - https://coin-update.de/feed/
- CoinDesk (Crypto EN) - https://www.coindesk.com/arc/outboundfeeds/rss/
- Cointelegraph (Crypto EN) - https://cointelegraph.com/rss
- TheBlock (Crypto EN) - https://www.theblock.co/feeds/rss
- Decrypt (Crypto EN) - https://decrypt.co/feed

**Quick Commands:**
```bash
# List all tracked blogs
blogwatcher blogs

# Scan for updates
blogwatcher scan

# List unread articles
blogwatcher articles

# Mark article as read
blogwatcher read <article-id>

# Mark all as read
blogwatcher read-all
```

**Notes:**
- Articles are stored locally
- No API key required
- Supports RSS and Atom feeds

---

## gh (GitHub CLI)

**Install:** Available via `gh` command (brew or apt)

**Auth:**
```bash
# Login with token
echo "ghp_..." | gh auth login --with-token --hostname github.com
```

**Quick Commands:**
```bash
# Check status
gh auth status

# Repo operations
gh repo create <name> --private
git push -u origin master

# Issues
gh issue create --repo owner/repo --title "..." --body "..."
gh issue close <number> --repo owner/repo
gh issue list --repo owner/repo

# PRs  
gh pr list --repo owner/repo
gh pr create --title "..." --body "..."
```

**GH_TOKEN:**
- Stored in: `~/.openclaw/.env` (GH_TOKEN)
- Also in: `~/.openclaw/openclaw.json` skills.entries.gh-issues.apiKey
- Used for: API calls, git push, issue/PR creation

---

## wttr.in (Weather)

**Endpoint:** `https://wttr.in/<location>`

**Quick Commands:**
```bash
# Current conditions
curl -s "wttr.in/Neumarkt+Südtirol?format=3"

# Detailed JSON
curl -s "wttr.in/Neumarkt+Südtirol?format=j1"

# 3-day forecast
curl -s "wttr.in/Neumarkt+Südtirol"
```

**Location:** Neumarkt-Egna (Südtirol)
- Timezone: Europe/Rome (UTC+1/+2 DST)

**Notes:**
- No API key required
- Rate limited (don't spam)

---

## TTS (Text-to-Speech)

**Usage:** Use `tts` tool with channel and text

**Best Practices:**
- Keep text under 60 seconds
- Use for: Weather reports, Crypto analysis, Stories
- German language preferred

**Example:**
```
Wetter für Neumarkt... [60 Sek Text]
```

---

## Cron Jobs (Active)

**Status:** Check via `openclaw cron status` or `cron action=list`

**Active Jobs:**
1. **daily-weather-neumarkt** - 06:45 daily
2. **daily-news-suedtirol** - 08:00 daily (7 articles, categorized)
3. **daily-crypto-update** - 09:00 daily (prices + button for analysis)

**Debug:**
- Logs: Check Gateway logs if cron fails
- Auth: Ensure GH_TOKEN and other env vars are set
- Timezone: Europe/Rome

---

## X API (Twitter)

**Status:** ⚠️ Eingeschränkt nutzbar – kostenlos read-only, Posting kostet

### API Tiers

| Tier | Preis | Features |
|------|-------|----------|
| **Free** | $0 | 1.500 Lesen/Monat, Posting NEIN, keine Mentions |
| **Basic** | $100/Monat | 3.000 Lesen/Monat, 500 Posting/Monat |
| **Pro** | $5.000/Monat | 1M Lesen/Monat, 300k Posting/Monat |
| **Enterprise** | Custom | Firehose, Support |

### Free Tier Limits

**Was geht:**
- User-Profil abfragen → `GET /2/users/:id`
- User-Tweets lesen → `GET /2/users/:id/tweets`
- Suchen (neu) → `GET /2/tweets/search/recent`
- Trends → `GET /1.1/trends/place.json`

**Was NICHT geht:**
- ❌ Tweet posten (erfordert Basic+: $100/Monat)
- ❌ Reply (erfordert Basic+)
- ❌ Mentions lesen (erfordert Basic+)
- ❌ DMs

### Auth

**OAuth 2.0 Bearer Token:**
1. Developer Portal: https://console.x.com/
2. App erstellen → Keys anzeigen

**Token speichern:**
```bash
# In ~/.openclaw/.env hinzufügen:
X_BEARER_TOKEN="your_token_here"
```

### Quick Commands (Free Tier)

```bash
# User-Info abfragen (benötigt X_BEARER_TOKEN)
curl -s -H "Authorization: Bearer $X_BEARER_TOKEN" \
  "https://api.twitter.com/2/users/by/username/elonmusk"

# Tweets eines Users
curl -s -H "Authorization: Bearer $X_BEARER_TOKEN" \
  "https://api.twitter.com/2/users/44196397/tweets?max_results=5"

# Tweet suchen (kürzlich)
curl -s -H "Authorization: Bearer $X_BEARER_TOKEN" \
  "https://api.twitter.com/2/tweets/search/recent?query=openclaw&max_results=10"
```

### Fazit

- **Free Tier**: Nur Lesen möglich (kein Posting, keine Mentions)
- **Kostenlos**: User-Info, Tweets lesen, Suche
- **Basic ($100/Monat)**: Minimum für Posting/Reply/Mentions
- **Alternative**: Mastodon API (kostenlos, offen)

**Nicht geeignet für:** Automatisches Posten kostenlos

---

## OpenCode

**Version:** 1.2.10
**Modell:** glm-5:cloud (via Ollama Cloud)

**Installation:**
```bash
npm install -g opencode-ai
# oder
curl -fsSL https://opencode.ai/install | bash  # (fehleranfällig)
```

**Konfiguration:**
```json
// ~/.config/opencode/opencode.json
{
  "$schema": "https://opencode.ai/config.json",
  "provider": {
    "ollama": {
      "npm": "@ai-sdk/openai-compatible",
      "name": "Ollama Cloud",
      "options": {
        "baseURL": "https://ollama.com/v1"
      },
      "models": {
        "glm-5:cloud": { "name": "glm-5:cloud" }
      }
    }
  }
}
```

**Nutzung:**
```bash
# Starten
opencode

# Mit Modell
opencode --model glm-5:cloud

# Hilfe
opencode --help
```

**OpenCode Controller Skill:**
- Location: `~/.openclaw/skills/opencode-controller/`
- OpenClaw-Integration über Skills-API
- Erlaubt Steuerung via `/sessions`, `/agents`, `/models`

---

## Sub-Agent Spawning

**Use When:**
- Long-running analysis tasks
- Isolated context needed
- Parallel processing

**Example:**
```javascript
sessions_spawn({
  task: "Analyze crypto news...",
  mode: "run",
  timeoutSeconds: 300
})
```

**Notes:**
- Auto-announces on completion
- Don't poll - wait for result
- Use `label` for tracking

---

## OpenClaw Config Updates (Safe Mode)

**⚠️ Wichtig:** Niemals direkt `write`/`edit` auf `openclaw.json` – immer `config.patch` verwenden.

### Safe Workflow

```bash
# 1. Hash holen (Optimistic Locking)
HASH=$(openclaw gateway call config.get --params '{}' | jq -r '.hash')

# 2. Backup erstellen
BACKUP="$HOME/.openclaw/openclaw.json.backup.$(date +%s)"
cp "$HOME/.openclaw/openclaw.json" "$BACKUP"

# 3. Patch anwenden (validiert + sicher)
openclaw gateway call config.patch --params "{
  \"raw\": \"{ agents: { defaults: { heartbeat: { every: \\\"1h\\\" } } } }\",
  \"baseHash\": \"$HASH\",
  \"note\": \"Heartbeat auf 1h geändert\"
}"
```

### Was passiert automatisch?

| Szenario | Verhalten |
|----------|-----------|
| JSON5 ungültig | Abgelehnt, Config bleibt |
| Schema-Fehler | Abgelehnt, Config bleibt |
| Hash mismatch | Abgelehnt (Jemand anderes hat geändert) |
| Gateway-Fields* | Automatischer Restart |
| Sonst alles | Hot-apply (kein Restart) |

*Gateway-Fields: `gateway.port`, `gateway.auth`, `gateway.bind`, `discovery.*`, `canvasHost.*`, `plugins.*`

### Bash Wrapper (empfohlen)

In `~/.bashrc`:

```bash
safe-config-patch() {
  local raw="$1"
  local note="${2:-Config update}"
  
  local hash=$(openclaw gateway call config.get --params '{}' 2>/dev/null | jq -r '.hash')
  [[ -z "$hash" || "$hash" == "null" ]] && { echo "Gateway nicht erreichbar"; return 1; }
  
  cp ~/.openclaw/openclaw.json ~/.openclaw/openclaw.json.backup.$(date +%s)
  
  openclaw gateway call config.patch --params "{
    \"raw\": \"$raw\",
    \"baseHash\": \"$hash\",
    \"note\": \"$note\"
  }"
}
```

Nutzung:
```bash
safe-config-patch '{ agents: { defaults: { heartbeat: { every: "2h" } } } }' "Heartbeat auf 2h"
```

### Doku
- Full Ref: `/home/ubuntu/.npm-global/lib/node_modules/openclaw/docs/gateway/configuration-reference.md`
- Rate limit: 3 Config-RPCs / 60 Sekunden
- Hot-reload: Most fields apply instantly (no restart)

---

## Tavily Search (Web Search Alternative)

**Status:** ✅ Aktiv – Workaround für broken Brave Search
**Location:** `~/.openclaw/skills/tavily-search/`
**API Key:** `TAVILY_API_KEY` (in `~/.openclaw/.env`)

### Warum Tavily?
- Brave Search API ist broken (endpoint `/api/web/search` deprecated)
- Tavily funktioniert sofort mit gültigem Key
- Kostenlos: ~1000 Requests/Monat
- Gute Ergebnisqualität mit AI-Context

### Quick Commands

```bash
# Direkt nutzen
node ~/.openclaw/skills/tavily-search/scripts/tavily-search.js "openclaw" 5

# Als Alias (empfohlen – in .bashrc hinzufügen)
alias tavily-search='node ~/.openclaw/skills/tavily-search/scripts/tavily-search.js'
```

### Response Format

```json
{
  "query": "openclaw",
  "results": [
    {
      "title": "What Is OpenClaw? Complete Guide...",
      "url": "https://milvus.io/blog/...",
      "snippet": "OpenClaw is an autonomous..."
    }
  ],
  "answer": "OpenClaw is an open-source AI agent..."
}
```

### Vergleich: Brave vs Tavily

| Feature | Brave | Tavily |
|---------|-------|--------|
| Status | ❌ Broken | ✅ Funktioniert |
| Free Tier | 2000/mo | ~1000/mo |
| AI Answer | Nein | Ja |
| API Key | BSA* tokens defekt | tvly-dev-* funktioniert |

### Weitere Infos
- Quickstart: `~/.openclaw/skills/tavily-search/QUICKSTART.md`
- Skill Docs: `~/.openclaw/skills/tavily-search/SKILL.md`

