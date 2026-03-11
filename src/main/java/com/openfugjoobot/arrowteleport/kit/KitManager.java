package com.openfugjoobot.arrowteleport.kit;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.config.ConfigManager;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages starter kit distribution
 */
public class KitManager {

    private final ArrowTeleport plugin;
    private final Map<String, Enchantment> enchantmentCache;

    public KitManager(ArrowTeleport plugin) {
        this.plugin = plugin;
        this.enchantmentCache = new HashMap<>();
        cacheEnchantments();
    }

    private void cacheEnchantments() {
        enchantmentCache.put("INFINITY", Enchantment.INFINITY);
        enchantmentCache.put("POWER", Enchantment.POWER);
        enchantmentCache.put("PUNCH", Enchantment.PUNCH);
        enchantmentCache.put("FLAME", Enchantment.FLAME);
        enchantmentCache.put("UNBREAKING", Enchantment.UNBREAKING);
        enchantmentCache.put("MENDING", Enchantment.MENDING);
        enchantmentCache.put("SHARPNESS", Enchantment.SHARPNESS);
        enchantmentCache.put("EFFICIENCY", Enchantment.EFFICIENCY);
    }

    /**
     * Give starter kit to player
     */
    public boolean giveKit(Player player) {
        if (!plugin.getConfigManager().isKitEnabled()) {
            player.sendMessage(MessageUtil.withPrefix("&cKit is disabled."));
            return false;
        }

        for (ConfigManager.KitItem kitItem : plugin.getConfigManager().getKitItems()) {
            ItemStack item = new ItemStack(kitItem.getMaterial(), kitItem.getAmount());

            // Apply enchantments
            for (String enchantStr : kitItem.getEnchantments()) {
                try {
                    String[] parts = enchantStr.split(":");
                    String enchantName = parts[0].toUpperCase().trim();
                    int level = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 1;

                    Enchantment enchantment = enchantmentCache.get(enchantName);
                    if (enchantment != null) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.addEnchant(enchantment, level, true);
                            item.setItemMeta(meta);
                        }
                    } else {
                        // Try by key
                        Enchantment byKey = Enchantment.getByKey(NamespacedKey.minecraft(enchantName.toLowerCase()));
                        if (byKey != null) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta != null) {
                                meta.addEnchant(byKey, level, true);
                                item.setItemMeta(meta);
                            }
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Invalid enchantment: " + enchantStr);
                }
            }

            // Add to inventory
            player.getInventory().addItem(item);
        }

        player.sendMessage(MessageUtil.withPrefix("&aKit received!"));
        return true;
    }
}
