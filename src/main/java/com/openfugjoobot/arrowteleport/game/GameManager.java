package com.openfugjoobot.arrowteleport.game;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages game state, player sessions, and statistics
 */
public class GameManager {

    private final ArrowTeleport plugin;
    private final Map<UUID, PlayerData> playerData;
    private final Set<UUID> activeSessions;
    private File dataFile;
    private org.bukkit.configuration.file.FileConfiguration dataConfig;

    public GameManager(ArrowTeleport plugin) {
        this.plugin = plugin;
        this.playerData = new ConcurrentHashMap<>();
        this.activeSessions = ConcurrentHashMap.newKeySet();
        loadPlayerData();
    }

    /**
     * Get or create player data
     */
    public PlayerData getPlayerData(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), 
            uuid -> new PlayerData(uuid, player.getName()));
    }

    /**
     * Start a game session for player
     */
    public void startSession(Player player) {
        PlayerData data = getPlayerData(player);
        data.startSession();
        activeSessions.add(player.getUniqueId());
        
        plugin.getLogger().info("Started session for " + player.getName());
        
        // Start timer display
        if (plugin.getConfigManager().isTimerEnabled()) {
            plugin.getTimerManager().startTimer(player);
        }
    }

    /**
     * Resume session for returning player (after relog)
     */
    public void resumeSession(Player player) {
        PlayerData data = getPlayerData(player);
        if (data.isInGame()) {
            activeSessions.add(player.getUniqueId());
            plugin.getLogger().info("Resumed existing session for " + player.getName());
            
            // Restart timer
            if (plugin.getConfigManager().isTimerEnabled()) {
                plugin.getTimerManager().startTimer(player);
            }
        }
    }

    /**
     * End a game session for player
     */
    public void endSession(Player player) {
        PlayerData data = getPlayerData(player);
        data.endSession();
        activeSessions.remove(player.getUniqueId());
        
        plugin.getTimerManager().stopTimer(player);
        savePlayerData();
        
        plugin.getLogger().info("Ended session for " + player.getName());
    }

    /**
     * Check if player has active game session
     */
    public boolean hasActiveSession(Player player) {
        return activeSessions.contains(player.getUniqueId()) && 
               getPlayerData(player).isInGame();
    }

    /**
     * Record an arrow shot by player
     */
    public void recordArrowShot(Player player) {
        PlayerData data = getPlayerData(player);
        data.recordArrowShot();
        
        // Tag arrow with owner metadata
        // This is handled in ArrowListener, but we track stats here
    }

    /**
     * Record a successful teleport
     */
    public void recordTeleport(Player player, Location from, Location to) {
        PlayerData data = getPlayerData(player);
        data.recordTeleport(from, to);
    }

    /**
     * Reset player to spawn
     */
    public void resetPlayer(Player player) {
        Location spawn = player.getWorld().getSpawnLocation();
        player.teleport(spawn);
        
        // End current session
        if (hasActiveSession(player)) {
            endSession(player);
        }
    }

    /**
     * Player quit - keep session active (persist until reset/stop)
     */
    public void playerQuit(Player player) {
        // Remove from active sessions but DON'T end - session persists
        activeSessions.remove(player.getUniqueId());
        plugin.getLogger().info("Player " + player.getName() + " logged out - session kept active");
        
        // Timer stopped in ArrowTeleport.onDisable or PlayerJoin handling
    }

    // Persistence
    private void loadPlayerData() {
        dataFile = new File(plugin.getDataFolder(), "player-data.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create player-data.yml");
                return;
            }
        }

        dataConfig = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(dataFile);
        
        if (dataConfig.contains("players")) {
            for (String uuidStr : dataConfig.getConfigurationSection("players").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    String path = "players." + uuidStr;
                    String name = dataConfig.getString(path + ".name", "Unknown");
                    
                    PlayerData data = new PlayerData(uuid, name);
                    data.setTotalDistance(dataConfig.getDouble(path + ".total-distance", 0));
                    data.setArrowsShot(dataConfig.getInt(path + ".arrows-shot", 0));
                    data.setTotalTimeSeconds(dataConfig.getLong(path + ".total-time-seconds", 0));
                    data.setSessionsCompleted(dataConfig.getInt(path + ".sessions-completed", 0));
                    data.setBestTime(dataConfig.getLong(path + ".best-time", Long.MAX_VALUE));
                    
                    playerData.put(uuid, data);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in player-data: " + uuidStr);
                }
            }
        }
    }

    public void savePlayerData() {
        if (dataConfig == null) return;
        
        dataConfig.set("players", null); // Clear existing
        
        for (Map.Entry<UUID, PlayerData> entry : playerData.entrySet()) {
            String path = "players." + entry.getKey().toString();
            PlayerData data = entry.getValue();
            
            dataConfig.set(path + ".name", data.getPlayerName());
            dataConfig.set(path + ".total-distance", data.getTotalDistance());
            dataConfig.set(path + ".arrows-shot", data.getArrowsShot());
            dataConfig.set(path + ".total-time-seconds", data.getTotalTimeSeconds());
            dataConfig.set(path + ".sessions-completed", data.getSessionsCompleted());
            dataConfig.set(path + ".best-time", data.getBestTime());
        }

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save player-data.yml");
        }
    }

    public void shutdown() {
        savePlayerData();
        activeSessions.clear();
    }
}
