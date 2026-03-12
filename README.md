# ArrowTeleport Plugin

A Minecraft Paper 1.21.x challenge plugin that restricts players to vertical movement only, requiring arrow teleportation for horizontal traversal.

## 🎮 Features

- **Vertical-Only Movement**: Players can only move up/down naturally, horizontal movement is blocked
- **Arrow Teleportation**: Shoot an arrow → Teleport to arrow location
- **🛡️ Smart Collision Safety**: Teleports to wall position at arrow hit Y level (no upward search), accepts 1-block clearance (crouching/lying in 1x1 holes)
- **🎯 Look Direction Preservation**: Maintains player's look direction from when arrow was shot
- **🎵 Sound + Particles**: Enderman teleport sound + portal particles on teleport
- **Persistent Sessions**: Sessions survive logout/relogin, auto-resume on join
- **Full Command System**: `/at`, `/atStart`, `/atStop`, `/atReset`, `/atKit`, `/atStats`, `/atReload`
- **Starter Kit**: Bow (Infinity) + 1 Arrow (manual via `/atKit`)
- **Timer Display**: Real-time action bar showing time, arrows shot, distance
- **Persistent Stats**: Track total distance, arrows shot, best times

## 📋 Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/at` | `arrowteleport.use` | Main command - shows help and status |
| `/atStart` | `arrowteleport.start` | Start challenge with 3s countdown |
| `/atStop [player]` | `arrowteleport.admin` | Stop challenge (all or specific player) |
| `/atReset` | `arrowteleport.reset` | Reset to spawn, end session |
| `/atReset full` | `arrowteleport.admin` | **Delete all worlds** (overworld/nether/end) + shutdown server |
| `/atKit` | `arrowteleport.kit` | Give starter kit (1 bow + 1 arrow) |
| `/atStats` | `arrowteleport.stats` | Show personal statistics |
| `/atReload` | `arrowteleport.reload` | Reload configuration (admin) |

**Note:** All commands require OP permissions (`default: op`).

## 🔧 Installation

1. **Requirements:**
   - Paper 1.21.x server
   - Java 21 or higher

2. **Download:**
   - Download `arrowteleport-1.0.0.jar` from [Releases](../../releases)
   - Or build from source

3. **Install:**
   - Copy JAR to `plugins/` folder
   - Restart server
   - Edit `plugins/ArrowTeleport/config.yml`

## ⚙️ Configuration

Default config (`src/main/resources/config.yml`):

```yaml
game:
  restrict-movement: true
  cross-world-teleport: true
  max-teleport-distance: 0  # 0 = unlimited
  fall-damage-reduction: 0.5
  teleport-cooldown: 0

kit:
  enabled: true
  items:
    bow:
      material: BOW
      amount: 1
      enchantments:
        - "INFINITY:1"
    arrows:
      material: ARROW
      amount: 1  # Single arrow only

timer:
  enabled: true
  countdown: 3
  format: "&aTime: &f%minutes%:%seconds% &7| &bArrows: &f%arrows%"

restrictions:
  block-walking: true
  block-swimming: true
  block-elytra: true
  block-riding: true
  block-vehicles: true
  block-ender-pearls: true
  block-chorus-fruit: true
  block-wind-charges: true
  block-riptide: true
```

## 🛡️ Collision Safety

**Smart wall-teleport system:**

1. Detects which block face the arrow hit (NORTH/SOUTH/EAST/WEST/UP/DOWN)
2. Positions player **in front of the wall** at arrow hit Y level
3. **No upward search** - prevents roof teleportation
4. Accepts 2-block clearance (standing) or 1-block clearance (crouching/lying in 1x1 holes)
5. Preserves look direction from arrow shot time

## 🎯 Teleport Mechanics

- **Look direction**: Uses yaw/pitch from when arrow was shot (not when it lands)
- **Position**: 0.3 blocks in front of hit block face
- **Y-level**: Exact arrow hit Y (no vertical adjustment unless 1 block down fallback)
- **Effects**: `ENTITY_ENDERMAN_TELEPORT` sound + `PORTAL` particles (50)

## 🔄 Session Persistence

- Sessions **persist across logout/relogin**
- Auto-resume on player join (3-tick delay to avoid login timeout)
- Sessions only end via:
  - `/atReset` (player reset + session end)
  - `/atStop` (admin stop)
  - `/atReset full` (world wipe + server shutdown)

## 🔨 Building from Source

```bash
# Requirements
sudo apt-get install maven openjdk-21-jdk

# Build
cd arrowteleport-plugin
mvn clean package

# Output: target/arrowteleport-1.0.0.jar
```

**CI/CD:** GitHub Actions auto-builds on every push. Download from Actions artifacts.

## 📂 Project Structure

```
arrowteleport-plugin/
├── src/main/java/com/openfugjoobot/arrowteleport/
│   ├── ArrowTeleport.java              # Main class
│   ├── config/
│   │   └── ConfigManager.java          # Config loading
│   ├── commands/
│   │   ├── BaseCommand.java            # Command base
│   │   ├── MainCommand.java            # /at
│   │   ├── StartCommand.java           # /atStart
│   │   ├── StopCommand.java            # /atStop ✨ NEW
│   │   ├── ResetCommand.java           # /atReset, /atReset full
│   │   ├── KitCommand.java             # /atKit
│   │   ├── StatsCommand.java           # /atStats
│   │   ├── ReloadCommand.java          # /atReload
│   │   └── CommandManager.java         # Registration
│   ├── game/
│   │   ├── GameManager.java            # Session mgmt, persistence
│   │   ├── PlayerData.java             # Stats storage
│   │   └── TimerManager.java           # Action bar timer
│   ├── kit/
│   │   └── KitManager.java             # Kit distribution
│   ├── listeners/
│   │   ├── MovementListener.java       # Walk/swim/elytra block
│   │   ├── ArrowListener.java          # TP logic, sounds, particles
│   │   ├── VehicleListener.java        # Ride/vehicle block
│   │   ├── ItemListener.java           # Pearl/chorus/riptide block
│   │   └── JoinListener.java           # Auto-resume on join ✨ NEW
│   └── util/
│       ├── MessageUtil.java            # Color codes
│       ├── SafeLocationFinder.java     # 🛡️ Wall-teleport logic
│       └── PermissionUtil.java         # Permission strings
├── src/main/resources/
│   ├── plugin.yml                      # Plugin metadata + commands
│   └── config.yml                      # Default config
├── .github/workflows/
│   └── build.yml                       # CI/CD pipeline
├── pom.xml                             # Maven build
└── README.md                           # This file
```

## 🐛 Troubleshooting

### Plugin won't load
- Ensure **Paper 1.21.x** (not Spigot/Bukkit)
- Check logs for errors
- Verify Java 21

### Teleport fails ("No safe landing spot")
- Arrow may have hit invalid location
- Check console for errors
- Report with screenshot

### Login timeout
- If persists after updating, delete plugin and re-download
- Ensure latest build from GitHub Actions

## 📜 License

MIT License

## 🤝 Credits

Developed by OpenFugjooBot for the OpenFugjoo community.

---

**Latest Changes:**
- Wall-teleport at arrow hit Y (no roof TP)
- 1x1 hole support (lying position)
- Look direction preservation
- Persistent sessions with auto-resume
- `/atStop` command
- `/atReset full` world wipe + shutdown
