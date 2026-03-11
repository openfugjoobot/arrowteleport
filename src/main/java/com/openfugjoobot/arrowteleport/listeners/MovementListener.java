package com.openfugjoobot.arrowteleport.listeners;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

/**
 * Handles player movement restrictions
 * Allows vertical movement only, blocks horizontal
 */
public class MovementListener implements Listener {

    private final ArrowTeleport plugin;
    private static final double MOVEMENT_THRESHOLD = 0.001; // Small movement threshold

    public MovementListener(ArrowTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        // Check if restrictions are enabled and player has active session
        if (!plugin.getConfigManager().isRestrictMovement()) {
            return;
        }
        
        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) {
            return;
        }

        // Allow movement if:
        // 1. Y changed (vertical movement)
        // 2. It's very minimal movement (head rotation, position jitter)
        
        double dx = Math.abs(to.getX() - from.getX());
        double dy = Math.abs(to.getY() - from.getY());
        double dz = Math.abs(to.getZ() - from.getZ());

        // Allow vertical movement and very small position changes
        if (dx < MOVEMENT_THRESHOLD && dz < MOVEMENT_THRESHOLD) {
            // Only Y changed or very minimal movement - allow it
            return;
        }

        // Block horizontal movement
        // Cancel the movement but allow the vertical component
        Location newLocation = from.clone();
        newLocation.setY(to.getY()); // Keep vertical change
        newLocation.setYaw(to.getYaw()); // Keep rotation
        newLocation.setPitch(to.getPitch());

        event.setCancelled(true);
        player.teleport(newLocation);

        // Send message (throttled to avoid spam)
        if (player.hasMetadata("at_movement_msg_cooldown")) {
            long lastMsg = player.getMetadata("at_movement_msg_cooldown").get(0).asLong();
            if (System.currentTimeMillis() - lastMsg < 3000) {
                return;
            }
        }
        
        player.setMetadata("at_movement_msg_cooldown", 
            new org.bukkit.metadata.FixedMetadataValue(plugin, System.currentTimeMillis()));
        
        String msg = plugin.getConfigManager().getMessage("movement-blocked");
        player.sendMessage(MessageUtil.withPrefix(msg));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // Allow teleport if it's our arrow teleport or a plugin command
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            return; // Allow plugin teleports
        }
        
        Player player = event.getPlayer();
        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        // Block non-plugin teleports (ender pearls, chorus fruit, etc.)
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL ||
            event.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
            
            if (plugin.getConfigManager().isRestrictionEnabled("block-ender-pearls") &&
                event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                event.setCancelled(true);
                player.sendMessage(MessageUtil.withPrefix("&cEnder pearls are disabled!"));
            }
            
            if (plugin.getConfigManager().isRestrictionEnabled("block-chorus-fruit") &&
                event.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
                event.setCancelled(true);
                player.sendMessage(MessageUtil.withPrefix("&cChorus fruit is disabled!"));
            }
        }
    }

    @EventHandler
    public void onPlayerSprint(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        if (event.isSprinting() && plugin.getConfigManager().isRestrictionEnabled("block-walking")) {
            // No cancel method, but we can message them
            // Sprinting in place is fine
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getGameManager().playerQuit(player);
    }
}
