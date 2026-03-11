# ArrowTeleport - Implementation Guide

## Phase 3: Backend Implementation - Complete

### Java Classes Implemented

#### Core
- ✅ `ArrowTeleport.java` - Main plugin class, lifecycle management
- ✅ `config/ConfigManager.java` - Configuration loading and caching
- ✅ `game/GameManager.java` - Session management and statistics persistence
- ✅ `game/PlayerData.java` - Player statistics data model
- ✅ `game/TimerManager.java` - Action bar countdown and timer display

#### Commands (7 commands)
- ✅ `commands/BaseCommand.java` - Abstract base for all commands
- ✅ `commands/CommandManager.java` - Command registration
- ✅ `commands/MainCommand.java` - `/at` implementation
- ✅ `commands/ResetCommand.java` - `/atReset` implementation
- ✅ `commands/StartCommand.java` - `/atStart` with countdown
- ✅ `commands/KitCommand.java` - `/atKit` implementation
- ✅ `commands/StatsCommand.java` - `/atStats` implementation
- ✅ `commands/ReloadCommand.java` - `/atReload` implementation

#### Listeners (4 listeners, 10+ events)
- ✅ `listeners/MovementListener.java`
  - `PlayerMoveEvent` - Horizontal movement blocking
  - `PlayerTeleportEvent` - Pearl/chorus blocking
  - `PlayerQuitEvent` - Session cleanup
  - `PlayerToggleSprintEvent` - Sprint tracking
  
- ✅ `listeners/ArrowListener.java`
  - `ProjectileLaunchEvent` - Arrow launch tracking
  - `EntityShootBowEvent` - Owner metadata tagging
  - `ProjectileHitEvent` - Teleport logic + 🛡️ Collision safety
  
- ✅ `listeners/VehicleListener.java`
  - `EntityMountEvent` - Riding blocking
  - `VehicleEnterEvent` - Vehicle blocking
  
- ✅ `listeners/ItemListener.java`
  - `PlayerInteractEvent` - Item usage tracking
  - `EntityToggleGlideEvent` - Elytra blocking
  - `PlayerRiptideEvent` - Trident riptide blocking
  - `PlayerWindChargeEvent` - Wind charge blocking
  - `PlayerTeleportEvent` - Additional teleport type checking

#### Utilities
- ✅ `util/MessageUtil.java` - Color codes, prefixes, formatting
- ✅ `util/SafeLocationFinder.java` - 🛡️ COLLISION SAFETY core logic
- ✅ `util/PermissionUtil.java` - Permission constants

#### Kit System
- ✅ `kit/KitManager.java` - Starter kit distribution with enchantments

### Resources
- ✅ `plugin.yml` - Plugin descriptor with all commands and permissions
- ✅ `config.yml` - Default configuration

---

## 🛡️ Collision Safety Implementation

### SafeLocationFinder Algorithm

```java
public static Location findSafeLocation(Location hitLocation) {
    // 1. Check if hit block is solid
    if (isSolidBlock(hitMaterial)) {
        // Start from block above the solid
        startY = hitBlock.getY() + 1;
    }
    
    // 2. Raycast upward (max 10 blocks)
    for (int offset = 0; offset < MAX_SEARCH_HEIGHT; offset++) {
        Block feetBlock = world.getBlockAt(x, y + offset, z);
        Block headBlock = world.getBlockAt(x, y + offset + 1, z);
        
        // 3. Check if both blocks are passable
        if (isPassableBlock(feetBlock) && isPassableBlock(headBlock)) {
            // 4. Return centered safe location
            return new Location(world, x + 0.5, y + offset, z + 0.5);
        }
    }
    
    // 5. No safe location found
    return null;
}
```

### Safety Features
- ✓ Detects solid vs passable blocks
- ✓ Ensures 2-block clearance for player
- ✓ Centers player on block (0.5 offset)
- ✓ Maintains facing direction
- ✓ Checks up to 10 blocks above hit

---

## Build Configuration

### Maven POM
- Paper API 1.21.4
- Java 21 source/target
- Resource filtering enabled
- Paper manifest entries

### GitHub Actions
- Automated build on push
- JDK 21 (Temurin)
- Artifact upload
- Release creation on main branch

---

## Testing Checklist

### Movement Blocking
- [ ] WASD movement blocked during active session
- [ ] Vertical jumping/falling allowed
- [ ] Movement allowed when no active session
- [ ] Movement allowed when `restrict-movement: false`

### Arrow Teleport
- [ ] Arrow shot tracks owner correctly
- [ ] Teleport happens on arrow land
- [ ] Safe location found when hitting solid blocks
- [ ] Safe location found when hitting air/water
- [ ] Suffocation prevented by collision safety
- [ ] Fall damage reduced per config
- [ ] Cooldown enforced

### Vehicle Blocking
- [ ] Horse riding blocked
- [ ] Boat entry blocked
- [ ] Minecart entry blocked

### Item Blocking
- [ ] Elytra flight blocked
- [ ] Ender pearls blocked
- [ ] Chorus fruit blocked
- [ ] Wind charges blocked
- [ ] Trident riptide blocked

### Commands
- [ ] `/at help` shows help
- [ ] `/at status` shows session status
- [ ] `/atStart` runs countdown and starts session
- [ ] `/atReset` teleports to spawn
- [ ] `/atKit` gives items
- [ ] `/atStats` shows statistics
- [ ] `/atReload` reloads config

### Multiplayer
- [ ] Concurrent players work independently
- [ ] Stats tracked per-player
- [ ] Sessions don't interfere

---

## Known Considerations

1. **Movement Detection**: Uses small threshold (0.001) to allow position jitter
2. **Cooldown**: Uses last teleport timestamp
3. **Cross-World**: Configurable, can be disabled
4. **Max Distance**: Configurable, 0 = unlimited
5. **Timer**: Updates every second (20 ticks)
6. **Stats Persistence**: YAML file, auto-saved on session end

---

## Next Steps for Production

1. Build JAR with `mvn clean package`
2. Test on local Paper server
3. Review debug console output
4. Configure permissions
5. Deploy to production server

---

**Implementation Status: ✅ COMPLETE**
All classes implemented, configuration files ready, build system configured.
