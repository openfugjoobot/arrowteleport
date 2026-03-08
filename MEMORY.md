# MEMORY.md - Long-Term Memory

_This file contains curated, lasting memories. Update when something important is learned or decided._

---

## Identity

**Name:** OpenFugjooBot
**Emoji:** 🦞
**Role:** Junior Developer / Digital Assistant
**Specialty:** OpenClaw features expert (docs = Bible)

## User

**Name:** Ivan
**Role:** CEO, software company
**Language:** German (casual)
**Timezone:** Europe/Rome (CET/CEST)
**Hierarchy:** Flat - call him "Ivan", not "Chef"

## Setup

- **OpenClaw Version:** 2026.3.7 (stable)
- **Model:** main = qwen3.5:cloud (262k context), current session = glm-5:cloud
- **9 Agents:** main, dev-orchestrator, research, architect, backend, frontend, qa, docs, devops
- **Heartbeat:** 5 minutes
- **GitHub Org:** openfugjoobot/*
- **Workflow Orchestrator:** ✅ 9 Phasen, Parallel-Execution (Phase 4), Retry-Logic (max 3), Auto-Backups

## Security Notes

- SSH: Key-only, no root login
- UFW: Active (deny incoming, allow outgoing)
- No disk encryption (Oracle Cloud)
- Gateway: loopback only (127.0.0.1:18789)

## API Keys & Integrations

| Service | Status |
|---------|--------|
| Brave Search | ✅ Working |
| Tavily Search | ✅ Working |
| Telegram Bot | ✅ Connected |
| GitHub | ✅ Authenticated (openfugjoobot) |
| Google Workspace | ✅ gog configured |
| ElevenLabs TTS | ✅ Configured |
| CoinGecko | ✅ Configured |

## Key Files

- `~/.openclaw/openclaw.json` - Main config
- `~/.openclaw/.env` - API keys
- `~/.openclaw/workspace/` - Working directory
- `~/.openclaw/skills/` - Installed skills

---

_Last updated: 2026-03-08_
