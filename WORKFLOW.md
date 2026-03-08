# WORKFLOW.md - AI Team Development Workflow

_8-Agent-Team with GitHub Issues._

---

## 🎯 Overview

**Principles:**
- **8-Phase Workflow** (strict quality gates, REQUIREMENTS never skipped)
- **One Repo = One Project** (GitHub Issues = Single Source of Truth)
- **8 Specialized Agents** (working in parallel, own workspaces)
- **Tool Reuse** (search for existing skills and tools, don't reinvent)
- **DevOrchestrator coordinates, NEVER codes** (delegates to specialized agents)

**User:** user (language per preference)
**Organization:** https://github.com/openfugjoobot

---

## 🏗️ 8-Agent Team

| Agent | Role | Phase |
|-------|------|-------|
| **DevOrchestrator** | Coordination, Management *(does NOT code)* | 0-8 |
| **ResearchAgent** | Tech Research, PoCs | 1 |
| **ArchitectAgent** | System Design, APIs | 2 |
| **BackendAgent** | Server, APIs, DB | 4 |
| **FrontendAgent** | UI/UX, Components | 4 |
| **QAAgent** | Tests, Code Review | 5 |
| **DocsAgent** | Documentation | 6 |
| **DevOpsAgent** | CI/CD, Deploy | 7 |

---

## 📋 8-Phase Workflow

| Phase | Owner | Output | GitHub Action |
|-------|-------|--------|---------------|
| **0. REQUIREMENTS** 🔴 | DevOrchestrator + user | `REQUIREMENTS.md` | Create issue, Label `requirements` |
| **1. ANALYSIS** | ResearchAgent | `specs/analysis.md` | Comment with results |
| **2. DESIGN** | ArchitectAgent | `specs/architecture.md` | Link specs in issue |
| **3. PLANNING** | DevOrchestrator | Subtasks with dependencies | Issues for subtasks, Labels, Milestone |
| **4. IMPLEMENTATION** | Backend + Frontend (parallel) | Code + Tests | **PR Workflow**, Branch `feature/xyz` |
| **5. REVIEW** | QAAgent | Review Report | PR Review (inline), Label `qa-approved` or `qa-changes-requested` |
| **6. DOCUMENTATION** | DocsAgent | README, API Docs | Docs added in PR |
| **7. DEPLOYMENT** | DevOpsAgent | Live App | Release `v1.0.0`, Comment deployment status |
| **8. CLOSURE** | DevOrchestrator | Memory Update | Issue `closed`, Retro |

**Phase 0 (REQUIREMENTS) is Mandatory:**
- Project goal (1 sentence), User Stories (3-5), Tech Stack, Deliverables, Exclusions, Success Criteria
- **⚠️ User must confirm in writing before Phase 1 starts!**

---

## 🔗 GitHub Integration

### Repository Structure
```
openfugjoobot/
├── project-a/          # One Repo = One Project
├── project-b/
└── project-c/
```

### Labels
- **Type:** `bug`, `feature`, `enhancement`, `documentation`
- **Priority:** `p0-critical`, `p1-high`, `p2-medium`, `p3-low`
- **Size:** `xs`, `s`, `m`, `l`, `xl`

### Branching & PR
```
main (protected)
  ↑
  ├── feature/xyz    # Feature branches
  └── hotfix/xyz     # Emergency fixes
```

**Rules:**
- No direct commits to `main`
- Every PR needs 1 approval (QAAgent)
- CI must be green
- After merge: delete branch

---

## 🛠️ Tooling & Setup

### Tool Reuse Requirement

**BEFORE project start:**
0. 🔍 **Search for existing software and apis** for quick overview using web_search
1. 🔍 **Search for existing skills** in `~/.openclaw/workspace/skills/`
2. 🔍 **Check TOOLS.md** for existing configurations
3. ✅ **Use existing tools and free software** instead of building new
4. ❌ **Don't reinvent** what already exists

**If skill is missing:**
- `clawhub search <keyword>` → Find skill
- `clawhub install <skill>` → Install
- Only if really needed: Create new skill

### Standard Tools (always check)
| Tool | Usage |
|------|------------|
| `gh` | GitHub CLI for all Git operations |
| `gh-issues` | GitHub CLI Git issues |
| `blogwatcher` | RSS/Atom Feed monitoring |
| `weather` | Weather data (wttr.in) |
| `gog` | Google Workspace (Gmail, Calendar, Drive) |
| `clawhub` | Skill management |
| `mcporter` | MCP Server Integration |
| `opencode` | **Primary Development Tool** – AI coding agent for implementation |

### API Keys
Located in `~/.openclaw/.env`:
```bash
BRAVE_SEARCH_API_KEY      # Web search
OPENROUTER_API_KEY        # LLM models
GOG_CLIENT_ID/SECRET      # Google Workspace
GITHUB_TOKEN              # API access (if needed)
...
```

---

## 📚 Best Practices

### Architecture
- **One repo per project** (no mono-repos)
- **PR workflow** with reviews (no direct main commits)
- **Labels** by Type/Priority/Size (not by agent)
- **YAGNI:** Create folders/files only when content exists

### Code Quality
- Never skip REQUIREMENTS phase
- No code without tests
- No deploy without green CI
- No secrets in commits (only env vars)
- `trash` > `rm` (recoverable is better than gone)

### API Integration
- Prefer official APIs
- Implement rate limiting & error handling
- Fallback mechanisms for critical dependencies
- Caching for frequent requests

---

## 🔄 Continuous Monitoring (24/7)

**Continuous Checks:**
1. 🚫 Blocked Tickets (>2h no activity)
2. 📝 Open Reviews (pending >24h → reminder)
3. 🎫 New issues in repositories
4. 🔍 Memory maintenance (REVIEWED → MEMORY.md)
5. 🧹 Cleanup (temp files, orphaned processes)
6. 💾 Git check (commit uncommitted changes)

**Phase Transition Automation:**
| From | To | Action |
|------|------|--------|
| REQUIREMENTS | ANALYSIS | Spawn ResearchAgent |
| ANALYSIS | DESIGN | Spawn ArchitectAgent |
| DESIGN | PLANNING | Create subtasks |
| PLANNING | IMPLEMENTATION | Backend + Frontend in parallel |
| IMPLEMENTATION | REVIEW | Spawn QAAgent |
| REVIEW | DOCUMENTATION | Spawn DocsAgent |
| DOCUMENTATION | DEPLOYMENT | Spawn DevOpsAgent |
| DEPLOYMENT | CLOSURE | Retro + Cleanup |

---

## ✅ Decision Matrix

| Situation | Action |
|-----------|--------|
| Tech Stack <€10/month | ✅ Auto |
| Refactoring <50 LOC | ✅ Auto |
| Bugfixes | ✅ Auto |
| Agent spawn/retry | ✅ Auto |
| Architecture changes | ⚠️ INFO user |
| New dependencies | ⚠️ INFO user |
| Budget impact | ⚠️ INFO user |
| Agent timeouts | ⚠️ INFO user |
| Security relevant | 🛑 ASK user |
| Breaking changes | 🛑 ASK user |
| Costs >€50 | 🛑 ASK user |
| DB migrations | 🛑 ASK user |
| Unclear requirements | 🛑 ASK user |

---

## 📝 Memory Management

**Files:**
```
~/.openclaw/workspace/
├── MEMORY.md              # Long-term Memory (Main Session only)
├── memory/YYYY-MM-DD.md   # Daily notes
├── DECISION_LOG.md        # Architecture decisions
└── LEARNINGS.md           # Retro insights
```

---

## 🚀 Setup Checklist

- [ ] **Check skills, tools and existing software** Quick Online web_search
- [ ] **Create repo:** One repo per project
- [ ] **Labels:** Type, Priority, Size configured
- [ ] **Branch protection:** main protected, CI required
- [ ] **Workspaces:** 8 agent workspaces set up
- [ ] **PR template:** Create
- [ ] **Test run:** First ticket through the flow

---
