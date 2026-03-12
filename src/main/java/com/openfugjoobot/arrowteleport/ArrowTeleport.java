package com.openfugjoobot.arrowteleport;

import com.openfugjoobot.arrowteleport.commands.CommandManager;
import com.openfugjoobot.arrowteleport.config.ConfigManager;
import com.openfugjoobot.arrowteleport.game.GameManager;
import com.openfugjoobot.arrowteleport.game.TimerManager;
import com.openfugjoobot.arrowteleport.kit.KitManager;
import com.openfugjoobot.arrowteleport.listeners.ArrowListener;
import com.openfugjoobot.arrowteleport.listeners.ItemListener;
import com.openfugjoobot.arrowteleport.listeners.MovementListener;
import com.openfugjoobot.arrowteleport.listeners.VehicleListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ArrowTeleport Plugin Main Class
 * Paper 1.21.x - Arrow teleportation challenge game mode
 */
public class ArrowTeleport extends JavaPlugin {

    private static ArrowTeleport instance;
    
    private ConfigManager configManager;
    private GameManager gameManager;
    private TimerManager timerManager;
    private KitManager kitManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        
        getLogger().info("═══════════════════════════════════");
        getLogger().info("  ArrowTeleport v" + getDescription().getVersion());
        getLogger().info("  by OpenFugjooBot");
        getLogger().info("═══════════════════════════════════");
        
        // Initialize managers
        initializeManagers();
        
        // Register events
        registerListeners();
        
        // Register commands
        commandManager.registerCommands();
        
        getLogger().info("Plugin enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down ArrowTeleport...");
        
        // Cleanup
        if (gameManager != null) {
            gameManager.shutdown();
        }
        if (timerManager != null) {
            timerManager.shutdown();
        }
        
        getLogger().info("ArrowTeleport disabled.");
    }

    private void initializeManagers() {
        // Config first (others depend on it)
        this.configManager = new ConfigManager(this);
        this.configManager.loadConfig();
        
        // Game managers
        this.gameManager = new GameManager(this);
        this.timerManager = new TimerManager(this);
        this.kitManager = new KitManager(this);
        
        // Commands (registered at end)
        this.commandManager = new CommandManager(this);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
        getServer().getPluginManager().registerEvents(new ArrowListener(this), this);
        getServer().getPluginManager().registerEvents(new VehicleListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        
        getLogger().info("Registered 5 event listeners");
    }

    // Getters
    public static ArrowTeleport getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public TimerManager getTimerManager() {
        return timerManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }
    
    public void reloadPlugin() {
        getLogger().info("Reloading configuration...");
        configManager.loadConfig();
        getLogger().info("Configuration reloaded!");
    }
}
