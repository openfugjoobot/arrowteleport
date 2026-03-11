# ArrowTeleport Plugin

A Minecraft Paper 1.21.x challenge plugin that restricts players to vertical movement only, requiring arrow teleportation for horizontal traversal.

## 🎮 Features

- **Vertical-Only Movement**: Players can only move up/down naturally, horizontal movement is blocked
- **Arrow Teleportation**: Shoot an arrow → Land → Teleport to arrow location
- **🛡️ Collision Safety**: Advanced teleport location detection - finds safe landing spots, prevents suffocation
- **Full Command System**: `/at`, `/atReset`, `/atStart`, `/atKit`, `/atStats`, `/atReload`
- **Starter Kit**: Automatic distribution of bow with Infinity, arrows, food, and golden apples
- **Timer Display**: Real-time action bar showing time, arrows shot, and distance traveled
- **Multiplayer Support**: Independent sessions per player
- **Persistent Stats**: Track total distance, arrows shot, best times across sessions

## 📋 Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/at` | `arrowteleport.use` | Main command - shows help and status |
| `/atReset` | `arrowteleport.reset` | Reset to world spawn, end session |
| `/atStart` | `arrowteleport.start` | Start challenge with countdown |
| `/atKit` | `arrowteleport.kit` | Give starter kit |
| `/atStats` | `arrowteleport.stats` | Show personal statistics |
| `/atReload` | `arrowteleport.reload` | Reload configuration (admin) |

## 🔧 Installation

1. **Requirements:**
   - Paper 1.21.x server
   - Java 21 or higher

2. **Download:**
   - Download `arrowteleport-1.0.0.jar` from [Releases](../../releases)
   - Or build from source (see below)

3. **Install:**
   - Copy `arrowteleport-1.0.0.jar` to your server's `plugins/` folder
   - Restart the server
   - Edit `plugins/ArrowTeleport/config.yml` as needed

## ⚙️ Configuration

See `src/main/resources/config.yml` for default configuration:

```yaml
game:
  restrict-movement: true
  cross-world-teleport: true
  max-teleport-distance: 0  # 0 = unlimited
  fall-damage-reduction: 0.5  # Reduce fall damage by 50%
  teleport-cooldown: 0  # seconds

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
      amount: 64

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

The plugin implements **smart teleport location finding**:

1. When an arrow hits, the plugin checks if the hit location would cause suffocation
2. It raycasts upward to find the first safe air block
3. Player is teleported to the safe location, not inside the block
4. Prevents suffocation damage and getting stuck

The safety search looks up to 10 blocks above the hit location and requires 2 blocks clearance (feet + head).

## 🔨 Building from Source

### Option 1: Using Maven (Recommended)

```bash
# Install Maven and Java 21
sudo apt-get install maven openjdk-21-jdk

# Clone and build
mvn clean package

# Plugin JAR will be in target/arrowteleport-1.0.0.jar
```

### Option 2: Using GitHub Actions (CI/CD)

The repository includes a GitHub Actions workflow that automatically builds the plugin on every push.

1. Fork this repository
2. Push changes to `main` branch
3. Download the built JAR from GitHub Actions artifacts

### Option 3: Manual Compilation

If you have the Paper API available:

```bash
javac -cp "paper-api-1.21.4.jar" -d target/classes src/main/java/**/*.java
jar cf arrowteleport-1.0.0.jar -C target/classes . -C src/main/resources .
```

## 📝 API & Events

The plugin uses standard Paper events:

- `PlayerMoveEvent` - Movement restriction
- `ProjectileHitEvent` - Arrow landing detection
- `EntityMountEvent` - Riding prevention
- `PlayerRiptideEvent` - Trident blocking

## 🐛 Troubleshooting

### Plugin won't load
- Ensure you're running **Paper 1.21.x** (not Spigot/Bukkit)
- Check server logs for error messages
- Verify Java 21 is being used

### Movement not being blocked
- Ensure player is in an active session (`/atStart`)
- Check `game.restrict-movement` in config

### TPs landing inside blocks
- This shouldn't happen with collision safety enabled
- Verify you're using the latest version
- Report with `/atStats` output

## 📂 Project Structure

```
arrowteleport-plugin/
├── src/main/java/com/openfugjoobot/arrowteleport/
│   ├── ArrowTeleport.java              # Main plugin class
│   ├── config/ConfigManager.java       # Config.yml handling
│   ├── commands/                       # All commands
│   ├── listeners/
│   │   ├── MovementListener.java       # Movement restriction
│   │   ├── ArrowListener.java          # Arrow teleport + collision safety
│   │   ├── VehicleListener.java        # Riding/vehicle blocking
│   │   └── ItemListener.java           # Elytra/pearl/riptide blocking
│   ├── game/
│   │   ├── GameManager.java            # Session management
│   │   ├── PlayerData.java             # Statistics
│   │   └── TimerManager.java           # Action bar timer
│   ├── kit/KitManager.java             # Starter kit
│   └── util/
│       ├── MessageUtil.java            # Color codes
│       ├── SafeLocationFinder.java     # 🛡️ Collision safety
│       └── PermissionUtil.java         # Permission constants
├── src/main/resources/
│   ├── plugin.yml                      # Plugin metadata
│   └── config.yml                      # Default config
├── pom.xml                             # Maven build config
└── README.md                           # This file
```

## 📜 License

MIT License - Feel free to use, modify, and distribute.

## 🤝 Credits

Developed by OpenFugjooBot for the OpenFugjoo community.
