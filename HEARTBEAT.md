# HEARTBEAT.md - Periodic Health Check

## Flow

1. **Subagents Check** → `subagents list` → Timeouts >10min?
2. **GitHub Issues** → Repos von `https://github.com/openfugjoobot/*` → P0/P1 Issues?
3. **Git Status** → Uncommitted changes im Workspace?
4. **Memory Maintenance** → `memory/heartbeat-state.json` aktuell?
5. **Workflow Orchestrator** → `node bin/orchestrator.js check` → Advance phases?

---

**Tracking:** Update `memory/heartbeat-spawn-tracker.json`:
```json
{
  "activeSpawns": [{"agentId": "research", "phase": "ANALYSIS", "status": "running"}],
  "autoContinuations": 0
}
```

---

## Output Format

| Level | Format |
|-------|--------|
| 🚨 Critical | `[description]` |
| ⚠️ Warning | `[list]` |
| ✅ OK | `HEARTBEAT_CHECK` |

---

## State Tracking

Update `memory/heartbeat-state.json`:
```json
{
  "lastChecks": {
    "subagents": 1703275200,
    "github": 1703260800,
    "memory": 1703102400,
    "git": 1703275200
  }
}
```

---

## Check Thresholds

| Check | Interval | Alert Condition |
|-------|----------|-----------------|
| Subagents | Every poll | Timeout >10min |
| GitHub | >1h | Any P0/P1 issues |
| Memory | >48h | Maintenance due |
| Git | >30min | Uncommitted changes |

---

## When to Stay Quiet (HEARTBEAT_OK)

- Late night (3:00-08:00) unless urgent
- Nothing new since last check
- No active spawns pending
- No uncommitted changes

---

## When to Reach Out

- Important GitHub issues found
- Sub-agent timeout/failure
- Calendar event <2h
- Something interesting discovered

---

## Workflow Orchestrator Check

**Purpose:** Auto-advance development workflow through 8 phases.

**Command:**
```bash
cd ~/.openclaw/skills/workflow-orchestrator && node bin/orchestrator.js check
```

**Workflow:**
- Loads `memory/workflow-state.json`
- Checks if active subagent completed
- If done → advances to next phase automatically
- Spawns next agent in the chain

**State Tracking:**
- `memory/workflow-state.json` - Current phase, active agents, artifacts
- `memory/backups/` - Automatic backups on each change

**Alert Conditions:**
- Workflow stuck (>30min on same phase) → Alert user
- Multiple subagent failures → Alert user
- Workflow completed → Congratulate user + summary
