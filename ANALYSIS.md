# ArrowTeleport Plugin - Analysis Document

## Overview
A Minecraft Paper 1.21.x challenge/speedrun game mode plugin that restricts players to vertical movement only, with arrow-teleportation as the sole horizontal traversal method.

---

## Requirements Breakdown

### Core Gameplay Mechanics

#### 1. Movement Restriction System
| Movement Type | Status | Implementation Challenge |
|--------------|--------|-------------------------|
| Walking (WASD) | BLOCK | Cancel `PlayerMoveEvent` when X/Z delta > 0 |
| Sprinting | BLOCK | Same as walking + disable sprint flag |
| Swimming | BLOCK | Cancel in water, handle dive physics |
| Knockback | BLOCK | Cancel `EntityDamageByEntityEvent` velocity changes |
| Riding (Horse/Pig/Strider) | BLOCK | Prevent mounting or cancel movement while riding |
| Minecarts/Boats | BLOCK | Cancel vehicle enter + movement events |
| Elytra Flight | BLOCK | Cancel glide start, disable elytra mid-flight |
| Wind Charges | BLOCK | Cancel `PlayerWindChargeEvent` |
| Trident Riptide | BLOCK | Cancel `PlayerRiptideEvent` |
| Ender Pearls | BLOCK | Cancel `ProjectileLaunchEvent` for pearls |
| Chorus Fruit | BLOCK | Cancel teleport event, unless it's our arrow teleport |
| **Vertical Movement (Jump/Fall)** | **ALLOW** | Keep natural Y-axis physics |
| **Arrow Teleport** | **ALLOW** | Custom teleport logic on arrow land |

#### 2. Arrow Teleport Mechanic
- Player shoots arrow → Arrow flies → Lands/hits → Player teleports to arrow location
- Must handle:
  - Normal arrow shots
  - Spectral arrows (allow these too)
  - Crossbow arrows
  - Arrow removal after teleport
  - Damage on landing (configurable fall damage reduction)
  - Cross-world teleportation support
  - Chunk loading for distant shots

---

### Command System

| Command | Description | Permission | Usage |
|---------|-------------|------------|-------|
| `/arrowTeleport` | Main command, shows help/status | `arrowteleport.use` | `/at` alias |
| `/atReset` | Reset player to spawn | `arrowteleport.reset` | Teleport to world spawn |
| `/atStart` | Start challenge with countdown | `arrowteleport.start` | Initiates game timer |
| `/atKit` | Give starter kit items | `arrowteleport.kit` | Bow + arrows + essentials |
| `/atStats` | Show player statistics | `arrowteleport.stats` | Time, distance, arrows shot |
| `/atReload` | Reload configuration | `arrowteleport.admin` | Reload config.yml |

**Command Aliases:** `/at` for all commands (e.g., `/at reset`, `/at start`)

---

### Configuration (config.yml)

```yaml
# ArrowTeleport Configuration

# Game Settings
game:
  # Enable movement restrictions globally
  restrict-movement: true
  
  # Allow cross-world arrow teleportation
  cross-world-teleport: true
  
  # Maximum teleport distance (0 = unlimited, blocks)
  max-teleport-distance: 0
  
  # Fall damage reduction after teleport (0-1, 1 = no damage)
  fall-damage-reduction: 0.5
  
  # Teleport cooldown in seconds
  teleport-cooldown: 0

# Starter Kit
kit:
  enabled: true
  items:
    - material: BOW
      amount: 1
      enchantments:
        - type: INFINITY
          level: 1
    - material: ARROW
      amount: 64
    - material: COOKED_BEEF
      amount: 32
    - material: GOLDEN_APPLE
      amount: 5

# Timer Settings
timer:
  enabled: true
  # Countdown before timer starts (seconds)
  countdown: 3
  # Action bar display format
  format: "&aTime: &f%minutes%:%seconds% &7| &bArrows: &f%arrows%"

# Restrictions
restrictions:
  # Block walking/sprinting
  block-walking: true
  # Block swimming  
  block-swimming: true
  # Block elytra flight
  block-elytra: true
  # Block riding entities
  block-riding: true
  # Block vehicles
  block-vehicles: true
  # Block ender pearls
  block-ender-pearls: true
  # Block chorus fruit
  block-chorus-fruit: true
  # Block wind charges
  block-wind-charges: true
  # Block trident riptide
  block-riptide: true

# Messages
messages:
  prefix: "&8[&6ArrowTeleport&8] &r"
  movement-blocked: "&cYou can only move vertically! Shoot an arrow to teleport."
  teleport-success: "&aWhoosh! Teleported to arrow."
  cooldown-active: "&cTeleport on cooldown: &f%seconds%s"
```

---

## Technical Architecture

### Plugin Structure
```
com.openfugjoobot.arrowteleport/
├── ArrowTeleport.java          # Main plugin class
├── config/
│   └── ConfigManager.java      # Config.yml handler
├── commands/
│   ├── CommandManager.java     # Command registration
│   ├── ArrowTeleportCommand.java
│   ├── ResetCommand.java
│   ├── StartCommand.java
│   ├── KitCommand.java
│   ├── StatsCommand.java
│   └── ReloadCommand.java
├── listeners/
│   ├── MovementListener.java   # Movement restriction handler
│   ├── ArrowListener.java      # Arrow teleport logic
│   ├── VehicleListener.java    # Vehicle blocking
│   └── ItemListener.java       # Item restriction (pearl/chorus)
├── game/
│   ├── GameManager.java        # Game state management
│   ├── PlayerStats.java        # Per-player statistics
│   └── TimerManager.java       # Countdown timer display
├── kit/
│   └── KitManager.java         # Starter kit distribution
└── util/
    ├── MessageUtil.java        # Message formatting
    └── PermissionUtil.java     # Permission checks
```

### Required Paper API Events

1. **PlayerMoveEvent** - Cancel horizontal movement
2. **ProjectileLaunchEvent** - Track arrow shots
3. **ProjectileHitEvent** - Arrow landing detection
4. **PlayerTeleportEvent** - Handle teleport completion
5. **EntityMountEvent** - Block riding
6. **VehicleEnterEvent** - Block vehicles
7. **PlayerInteractEvent** - Block elytra equip/pearl use
8. **EntityToggleGlideEvent** - Block elytra flight
9. **PlayerRiptideEvent** - Block trident riptide
10. **PlayerWindChargeEvent** - Block wind charges

### Data Storage

**Player Statistics (YAML-based):**
```yaml
players:
  uuid-here:
    name: "PlayerName"
    total-time: 3600  # seconds
    arrows-shot: 150
    total-distance: 5000  # blocks teleported
    best-time: 120  # for speedruns
```

**Session Data (Memory-only):**
- Active game sessions
- Countdown timers
- Cooldown tracking
- Arrow-to-player mapping (for tracking ownership)

---

## Multiplayer Considerations

### Global Commands
- Commands affect the executing player only
- No team/region restrictions (cross-world support)

### Performance
- Arrow tracking: Store weak references or use custom metadata
- Movement checks: Lightweight delta calculation
- Timer updates: Action bar display every tick, optimized

### Edge Cases
1. **Arrow hits entity** - Teleport to entity location or cancel?
2. **Arrow fired into void** - Teleport to void death or block?
3. **Player disconnects while arrow in flight** - Clean up tracking
4. **Arrow enters unloaded chunk** - Load chunk or destroy arrow
5. **Multiple players shoot simultaneously** - Proper tracking per-player
6. **Player dies during teleport** - Handle gracefully
7. **Arrow hits portal/end-gateway** - Define behavior

---

## Risk Assessment

### Technical Risks
| Risk | Likelihood | Mitigation |
|------|------------|------------|
| Movement bypass via glitches | Medium | Comprehensive event coverage + periodic position checks |
| Arrow tracking memory leak | Low | Remove entries on arrow hit/timeout/remove |
| Cross-world teleport issues | Low | Validate world loading, handle exceptions |
| Conflict with other plugins | Medium | Priority settings, configurable event cancellation |

### Gameplay Risks
| Risk | Mitigation |
|------|------------|
| Player stuck in bad location | `/atReset` command always available |
| No arrows left | Kit command, or configurable auto-refill |
| Arrow too far (unload chunk) | Max distance check in config |

---

## Success Criteria

- [ ] All horizontal movement blocked (walking, swimming, riding, elytra, etc.)
- [ ] Arrow teleport works reliably (bow + crossbow, normal + spectral)
- [ ] All 6 commands functional
- [ ] Config.yml loads and hot-reloads correctly
- [ ] Timer displays with countdown
- [ ] Starter kit distributes correctly
- [ ] Cross-world teleportation works
- [ ] Multiplayer compatible (concurrent players)
- [ ] Clean compile with Paper 1.21.x API

---

## Dependencies

- **Paper API 1.21.x** - Core server API
- **Java 21** - Required for MC 1.21
- **Maven/Gradle** - Build system

---

## Estimate

| Phase | Estimated Time |
|-------|---------------|
| ANALYSIS | Complete |
| ARCHITECTURE | 30 min |
| BACKEND | 2-3 hours |
| FRONTEND | 30 min |
| INTEGRATION | 30 min |
| QA | 1 hour |
| DOCS | 30 min |
| DEPLOY | 15 min |
| **Total** | **~5-6 hours** |
