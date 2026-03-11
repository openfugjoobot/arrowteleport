package com.openfugjoobot.arrowteleport.config;

import com.openfugjoobot.arrowteleport.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Configuration manager for ArrowTeleport
 * Handles config.yml loading and hot-reloading
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    // Cached config values for performance
    private boolean restrictMovement;
    private boolean crossWorldTeleport;
    private double maxTeleportDistance;
    private double fallDamageReduction;
    private int teleportCooldown;
    private String prefix;
    private Map<String, String> messages;
    private Map<String, Boolean> restrictions;
    private List<KitItem> kitItems;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
        this.restrictions = new HashMap<>();
        this.kitItems = new ArrayList<>();
    }

    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }

        // Save default if not exists
        if (!configFile.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        // Load defaults from jar
        InputStream defaultStream = plugin.getResource("config.yml");
        if (defaultStream != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(
                new InputStreamReader(defaultStream, StandardCharsets.UTF_8)));
        }

        cacheConfigValues();
    }

    private void cacheConfigValues() {
        // Prefix
        this.prefix = config.getString("messages.prefix", "&8[&6ArrowTeleport&8] &r");
        MessageUtil.PREFIX = prefix;

        // Game settings
        this.restrictMovement = config.getBoolean("game.restrict-movement", true);
        this.crossWorldTeleport = config.getBoolean("game.cross-world-teleport", true);
        this.maxTeleportDistance = config.getDouble("game.max-teleport-distance", 0);
        this.fallDamageReduction = config.getDouble("game.fall-damage-reduction", 0.5);
        this.teleportCooldown = config.getInt("game.teleport-cooldown", 0);

        // Messages
        messages.clear();
        ConfigurationSection msgSection = config.getConfigurationSection("messages");
        if (msgSection != null) {
            for (String key : msgSection.getKeys(false)) {
                messages.put(key, msgSection.getString(key));
            }
        }

        // Restrictions
        restrictions.clear();
        ConfigurationSection restSection = config.getConfigurationSection("restrictions");
        if (restSection != null) {
            for (String key : restSection.getKeys(false)) {
                restrictions.put(key, restSection.getBoolean(key, true));
            }
        }

        // Kit items
        loadKitItems();

        plugin.getLogger().info("Configuration loaded and cached");
    }

    private void loadKitItems() {
        kitItems.clear();
        ConfigurationSection kitSection = config.getConfigurationSection("kit.items");
        if (kitSection == null) return;

        for (String key : kitSection.getKeys(false)) {
            ConfigurationSection itemSection = kitSection.getConfigurationSection(key);
            if (itemSection == null) continue;

            String materialName = itemSection.getString("material");
            if (materialName == null) continue;

            try {
                Material material = Material.valueOf(materialName.toUpperCase());
                int amount = itemSection.getInt("amount", 1);
                
                KitItem kitItem = new KitItem(material, amount);
                
                // Load enchantments
                List<String> enchants = itemSection.getStringList("enchantments");
                for (String enchantStr : enchants) {
                    kitItem.addEnchantment(enchantStr);
                }
                
                kitItems.add(kitItem);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid material in kit: " + materialName);
            }
        }
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config.yml", e);
        }
    }

    // Getters
    public FileConfiguration getConfig() {
        return config;
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, "Missing message: " + key);
    }

    public String getMessage(String key, String... replacements) {
        String msg = getMessage(key);
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                msg = msg.replace(replacements[i], replacements[1]);
            }
        }
        return msg;
    }

    // Game settings
    public boolean isRestrictMovement() { return restrictMovement; }
    public boolean isCrossWorldTeleport() { return crossWorldTeleport; }
    public double getMaxTeleportDistance() { return maxTeleportDistance; }
    public double getFallDamageReduction() { return fallDamageReduction; }
    public int getTeleportCooldown() { return teleportCooldown; }

    // Restrictions
    public boolean isRestrictionEnabled(String key) {
        return restrictions.getOrDefault(key, true);
    }

    // Kit
    public List<KitItem> getKitItems() { return kitItems; }
    public boolean isKitEnabled() { return config.getBoolean("kit.enabled", true); }

    // Timer
    public boolean isTimerEnabled() { return config.getBoolean("timer.enabled", true); }
    public int getCountdownSeconds() { return config.getInt("timer.countdown", 3); }
    public String getTimerFormat() { return config.getString("timer.format", "&aTime: &f%minutes%:%seconds% &7| &bArrows: &f%arrows%"); }

    public static class KitItem {
        private final Material material;
        private final int amount;
        private final List<String> enchantments;

        public KitItem(Material material, int amount) {
            this.material = material;
            this.amount = amount;
            this.enchantments = new ArrayList<>();
        }

        public void addEnchantment(String enchantment) {
            enchantments.add(enchantment);
        }

        public Material getMaterial() { return material; }
        public int getAmount() { return amount; }
        public List<String> getEnchantments() { return enchantments; }
    }
}
