# HEARTBEAT.md - Periodic Health Check

### Check for all git repositories in https://github.com/openfugjoobot/*

## Flow
   - `subagents list` → Check for timeouts 
   - `gh issue list` → Critical issues/PRs of all repositories **(Exclude archived repos)**
   - **Check spawned agents status** → See below
   - Check `memory/heartbeat-state.json` → Maintenance needed?
   - `git status` → Uncommitted changes 

---

## Spawned Agents Auto-Check

**Purpose:** Automatically check if spawned sub-agents completed their tasks and continue workflow without user prompting.

**How to check:**
```bash
# List recent sub-agents
subagents list

# For each sub-agent spawned in last session:
# 1. Check status (completed / failed / timeout)
# 2. If status shows completion but no workflow continuation:
#    → Spawn next phase automatically (per WORKFLOW.md Phase Transitions)
```

**Auto-Action Rules:**
| Sub-Agent Result | Action |
|-----------------|--------|
| ✅ Completed successfully | Check WORKFLOW.md for next phase → Auto-spawn if defined |
| ❌ Failed/Error | Alert user with error summary |
| ⏱️ Timeout >10min | Attempt graceful kill → Respawn with extended timeout |
| 🔄 Still running | Monitor only, do not interrupt |

**Important:** Only auto-continue if:
1. Sub-agent explicitly reported success
2. Phase transition is marked as "automatic" in WORKFLOW.md
3. spawnContext contains phase information
4. User has not interrupted (no manual commands since spawn)

**Tracking:** Update `memory/heartbeat-spawn-tracker.json` with:
```json
{
  "activeSpawns": [
    {"agentId": "research", "spawnedAt": 1234567890, "phase": "ANALYSIS", "status": "running"}
  ],
  "autoContinuations": 0
}
```

---

## Output Format

| Level | Format |
|-------|--------|
| Critical | 🚨 [description] |
| Warning | ⚠️ [list] |
| Nothing | HEARTBEAT_OK |

---

## State Tracking

Update `memory/heartbeat-state.json` with timestamps:

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

## Memory Maintenance

Daily maintenance of memory files:

### 1. Daily Logs
- Check: Does `memory/YYYY-MM-DD.md` exist for today?
- **Action:** Write important decisions, conversations, context to `memory/YYYY-MM-DD.md`
- **Interval:** Every heartbeat session (when relevant)

### 2. Lessons Learned
File: `memory/LESSONS.md`

**When to add:**
- Mistakes made → How to avoid?
- New insight about tools/workflow
- User preference learned
- Technical problem solved

**Format:**
```markdown
### [YYYY-MM-DD] Short title
- **Context:** What happened?
- **Solution:** What was done?
- **For future:** Concrete rule/statement
```

**Action:** Review last 2-3 days → Transfer important learnings to `LESSONS.md`

---

## Check Thresholds

| Check | Interval | Alert Condition |
|-------|----------|-----------------|
| Subagents | Every poll | Timeout >10min |
| GitHub | >1h since last | Any p0/p1 issues |
| Memory | >48h since last | Maintenance due |
| Git | >30min since last | Uncommitted changes |
| Lessons Update | >48h since last | Entries outdated |
