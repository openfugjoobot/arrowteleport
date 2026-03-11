package com.openfugjoobot.arrowteleport.listeners;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Handles item usage restrictions
 * Blocks elytra, trident riptide, wind charges, etc.
 */
public class ItemListener implements Listener {

    private final ArrowTeleport plugin;

    public ItemListener(ArrowTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        // Check for elytra activation
        if (plugin.getConfigManager().isRestrictionEnabled("block-elytra")) {
            if (player.getInventory().getChestplate() != null &&
                player.getInventory().getChestplate().getType() == Material.ELYTRA) {
                // Trying to glide
                if (event.getPlayer().isSneaking() || 
                    (event.getItem() != null && event.getItem().getType().isEdible())) {
                    // Let the glide event handle it
                }
            }
        }

        // Block wind charge use (right click)
        if (event.getItem() != null && event.getItem().getType() == Material.WIND_CHARGE) {
            if (plugin.getConfigManager().isRestrictionEnabled("block-wind-charges")) {
                event.setCancelled(true);
                player.sendMessage(MessageUtil.withPrefix("\u0026cWind charges are disabled!"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onToggleGlide(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        if (event.isGliding() && plugin.getConfigManager().isRestrictionEnabled("block-elytra")) {
            event.setCancelled(true);
            player.sendMessage(MessageUtil.withPrefix("\u0026cElytra flight is disabled!"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onRiptide(PlayerRiptideEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        if (plugin.getConfigManager().isRestrictionEnabled("block-riptide")) {
            event.setCancelled(true);
            player.sendMessage(MessageUtil.withPrefix("\u0026cTrident riptide is disabled!"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWindCharge(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        if (event.getItem() != null && event.getItem().getType() == Material.WIND_CHARGE) {
            if (plugin.getConfigManager().isRestrictionEnabled("block-wind-charges")) {
                event.setCancelled(true);
                player.sendMessage(MessageUtil.withPrefix("&cWind charges are disabled!"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPearlTeleport(PlayerTeleportEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        // Already handled in MovementListener, but double-check here
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL &&
            plugin.getConfigManager().isRestrictionEnabled("block-ender-pearls")) {
            event.setCancelled(true);
            player.sendMessage(MessageUtil.withPrefix("\u0026cEnder pearls are disabled!"));
        }
    }
}
