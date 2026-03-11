# ArrowTeleport - Architecture Document

## Phase 2: ARCHITECTURE - Complete Design

---

## Package Structure

```
com.openfugjoobot.arrowteleport/
├── ArrowTeleport.java               # Main plugin class
├── config/
│   └── ConfigManager.java           # YAML configuration handler
├── commands/
│   ├── CommandManager.java          # Command registration
│   ├── BaseCommand.java             # Abstract command base
│   ├── MainCommand.java             # /at command implementation
│   ├── ResetCommand.java            # /atReset implementation
│   ├── StartCommand.java            # /atStart implementation
│   ├── KitCommand.java              # /atKit implementation
│   ├── StatsCommand.java            # /atStats implementation
│   └── ReloadCommand.java           # /atReload implementation
├── listeners/
│   ├── MovementListener.java        # Movement restriction handler
│   ├── ArrowListener.java           # Arrow teleport + collision safety
│   ├── VehicleListener.java         # Vehicle/riding blocking
│   └── ItemListener.java            # Item usage blocking
├── game/
│   ├── GameManager.java             # Game state/session management
│   ├── PlayerData.java              # Per-player statistics
│   └── TimerManager.java            # Timer display (action bar)
├── kit/
│   └── KitManager.java              # Starter kit distribution
└── util/
    ├── MessageUtil.java             # Color codes, prefixes
    ├── SafeLocationFinder.java      # 🛡️ COLLISION SAFETY: Raycast upward
    └── PermissionUtil.java          # Permission constants
```

---

## Core Architecture

### 1. Main Plugin Class (ArrowTeleport.java)
- Implements `JavaPlugin`
- Singleton pattern for global access
- Initializes all managers in `onEnable()`
- Cleanup in `onDisable()`

### 2. Collision Safety System (SafeLocationFinder.java)
**Critical Requirement:** Teleport player to SAFE LOCATION

**Algorithm:**
```
1. Arrow hits at location (x, y, z)
2. Check if hit block is solid:
   - If solid: Find air block above
   - Raycast upward from hit location
   - Check y+1, y+2, y+3... until air found
3. Teleport player to (x, foundY+1, z)
4. If no safe location found in range: Cancel teleport + notify player
```

**Safety Checks:**
- Block must be `Material.isSolid()` = false (AIR, WATER, etc.)
- Player height clearance (2 blocks minimum: feet + head)
- Max search height: hitY + 10 blocks

### 3. Arrow Tracking System
**Problem:** Need to know which player shot an arrow

**Solution:** Use arrow metadata
```java
// When arrow is shot:
arrow.setMetadata("owner", new FixedMetadataValue(plugin, playerUUID));

// When arrow hits:
if (arrow.hasMetadata("owner")) {
    UUID ownerId = (UUID) arrow.getMetadata("owner").get(0).value();
}
```

### 4. Movement Restriction Engine

**Event Chain:**
1. `PlayerMoveEvent` - Primary handler
   - Calculate delta: `to.distanceSquared(from)`
   - Allow if `to.getY() != from.getY()` (vertical movement only)
   - Block if X or Z changed significantly

2. `PlayerToggleSprintEvent` - Prevent sprint (horizontal boost)

3. `PlayerToggleFlightEvent` - Block creative flight

### 5. Data Flow

```
Player shoots arrow:
  ↓ ArrowListener.onProjectileLaunch()
  ↓ Store owner metadata
  ↓ Track in GameManager.arrowsInFlight

Arrow lands:
  ↓ ArrowListener.onProjectileHit()
  ↓ Retrieve owner from metadata
  ↓ SafeLocationFinder.findSafeLocation()
  ↓ Validate teleport (distance, cooldown, etc.)
  ↓ Teleport player + update stats
  ↓ Remove arrow entity

Player moves:
  ↓ MovementListener.onPlayerMove()
  ↓ Check if active game
  ↓ Allow if only Y change
  ↓ Cancel if X or Z change
  ↓ Optional: Send action bar reminder
```

---

## Key Design Patterns

### Singleton Managers
- `ArrowTeleport.getInstance()`
- `GameManager.getInstance()`
- Thread-safe lazy initialization

### Event-Driven Architecture
- All game logic triggered by Paper events
- No polling loops (performance)
- Cleanup handlers for edge cases

### Configuration System
- Hierarchical YAML: `config.yml`
- Hot-reload via `/atReload`
- Type-safe getters with defaults

---

## Event Registration Map

| Event | Listener Class | Handler Method | Priority |
|-------|---------------|----------------|----------|
| PlayerMoveEvent | MovementListener | onPlayerMove() | HIGH |
| PlayerTeleportEvent | MovementListener | onPlayerTeleport() | NORMAL |
| ProjectileLaunchEvent | ArrowListener | onProjectileLaunch() | NORMAL |
| ProjectileHitEvent | ArrowListener | onProjectileHit() | NORMAL |
| EntityShootBowEvent | ArrowListener | onEntityShootBow() | NORMAL |
| EntityMountEvent | VehicleListener | onEntityMount() | HIGH |
| VehicleEnterEvent | VehicleListener | onVehicleEnter() | HIGH |
| PlayerInteractEvent | ItemListener | onPlayerInteract() | HIGH |
| PlayerTeleportEvent | ItemListener | onPearlTeleport() | HIGH |
| PlayerRiptideEvent | ItemListener | onRiptide() | HIGH |
| PlayerWindChargeEvent | ItemListener | onWindCharge() | HIGH |
| EntityToggleGlideEvent | ItemListener | onToggleGlide() | HIGH |

---

## Memory Management

### Weak References
- Arrow-to-player mapping uses metadata (GC-safe)
- No static collections that leak

### Cleanup Tasks
- Arrow timeout: Remove tracking after 30s
- Player quit: Remove session data
- Plugin disable: Clear all collections

---

## Multiplayer Safety

### Concurrent Modifications
- Use `ConcurrentHashMap` for active sessions
- Arrow tracking per-world to avoid conflicts

### Threading
- All events are synchronous (main thread)
- Async timer for action bar updates only

---

## File I/O

### config.yml (static, read-only after load)
Plugin configuration, copied on first run

### player-data.yml (read/write)
Persistent statistics storage

### Structure:
```yaml
players:
  550e8400-e29b-41d4-a716-446655440000:
    name: "PlayerName"
    total-distance: 1500.5
    arrows-shot: 45
    total-time-seconds: 1234
    sessions-completed: 5
```

---

## Error Handling Strategy

| Error Type | Response |
|------------|----------|
| Config parse error | Use defaults + log warning |
| Arrow owner not found | Cancel teleport silently |
| Safe location not found | Cancel + message to player |
| Cross-world disabled | Cancel + message |
| Chunk unloaded | Force load or cancel |
| Permission denied | Silent fail (Bukkit handles) |

---

## Performance Optimizations

1. **Movement checks:** Use squared distance (avoid sqrt)
2. **Arrow tracking:** Metadata-based (no HashMap lookups on hit)
3. **Timestamps:** System.currentTimeMillis() (fastest)
4. **Timer display:** Update every 1s, not every tick
5. **Safe location search:** Max 10 blocks up (configurable)

---

## Next Phase
Phase 3 (BACKEND): Implement all Java classes per this architecture.
