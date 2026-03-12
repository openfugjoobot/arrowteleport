package com.openfugjoobot.arrowteleport.listeners;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.game.PlayerData;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import com.openfugjoobot.arrowteleport.util.SafeLocationFinder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Handles arrow teleportation mechanics
 * 🛡️ COLLISION SAFETY: Uses SafeLocationFinder
 */
public class ArrowListener implements Listener {

    private final ArrowTeleport plugin;
    private static final String OWNER_METADATA = "at_owner";
    private static final String YAW_METADATA = "at_yaw";
    private static final String PITCH_METADATA = "at_pitch";

    public ArrowListener(ArrowTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow arrow)) {
            return;
        }

        // Owner metadata is set by EntityShootBowEvent, this is backup
        if (arrow.getShooter() instanceof Player player) {
            arrow.setMetadata(OWNER_METADATA, new FixedMetadataValue(plugin, player.getUniqueId().toString()));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!(event.getProjectile() instanceof AbstractArrow arrow)) {
            return;
        }

        // Store owner metadata
        arrow.setMetadata(OWNER_METADATA, new FixedMetadataValue(plugin, player.getUniqueId().toString()));
        
        // Store player's look direction at time of shot
        arrow.setMetadata(YAW_METADATA, new FixedMetadataValue(plugin, (float) player.getLocation().getYaw()));
        arrow.setMetadata(PITCH_METADATA, new FixedMetadataValue(plugin, (float) player.getLocation().getPitch()));

        // Track arrow shot stats
        plugin.getGameManager().recordArrowShot(player);

        // Schedule arrow cleanup after timeout
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow.isValid() && !arrow.isDead()) {
                    arrow.remove();
                }
            }
        }.runTaskLater(plugin, 600L); // 30 seconds
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow arrow)) {
            return;
        }

        // Get owner from metadata
        if (!arrow.hasMetadata(OWNER_METADATA)) {
            return;
        }

        String ownerIdStr = arrow.getMetadata(OWNER_METADATA).get(0).asString();
        UUID ownerId;
        try {
            ownerId = UUID.fromString(ownerIdStr);
        } catch (IllegalArgumentException e) {
            return;
        }

        Player player = plugin.getServer().getPlayer(ownerId);
        if (player == null || !player.isOnline()) {
            arrow.remove();
            return;
        }

        // Check if player is in a game session
        if (!plugin.getGameManager().hasActiveSession(player)) {
            arrow.remove();
            return;
        }

        PlayerData data = plugin.getGameManager().getPlayerData(player);

        // Check cooldown
        int cooldown = plugin.getConfigManager().getTeleportCooldown();
        if (data.isOnCooldown(cooldown)) {
            int remaining = data.getRemainingCooldown(cooldown);
            String msg = plugin.getConfigManager().getMessage("cooldown-active")
                .replace("%seconds%", String.valueOf(remaining));
            player.sendMessage(MessageUtil.withPrefix(msg));
            arrow.remove();
            return;
        }

        // Get hit location
        Location hitLocation;
        if (event.getHitBlock() != null) {
            hitLocation = event.getHitBlock().getLocation();
        } else if (event.getHitEntity() != null) {
            hitLocation = event.getHitEntity().getLocation();
        } else {
            // Fall back to arrow location
            hitLocation = arrow.getLocation();
        }

        // 🛡️ COLLISION SAFETY: Find safe location
        Location safeLocation = SafeLocationFinder.findSafeLocation(hitLocation);

        if (safeLocation == null) {
            player.sendMessage(MessageUtil.withPrefix("\u0026cNo safe landing spot found!"));
            arrow.remove();
            return;
        }

        // Check cross-world teleport
        if (!player.getWorld().equals(safeLocation.getWorld())) {
            if (!plugin.getConfigManager().isCrossWorldTeleport()) {
                player.sendMessage(MessageUtil.withPrefix("\u0026cCross-world teleport is disabled!"));
                arrow.remove();
                return;
            }
        }

        // Check max distance
        double maxDist = plugin.getConfigManager().getMaxTeleportDistance();
        if (maxDist > 0) {
            if (player.getWorld().equals(safeLocation.getWorld()) && player.getLocation().distance(safeLocation) > maxDist) {
                player.sendMessage(MessageUtil.withPrefix("\u0026cArrow too far! Max distance: " + (int) maxDist));
                arrow.remove();
                return;
            }
        }

        // Skip strict isSafeLocation check - findSafeLocation already validates
        // isSafeLocation requires 2-block clearance, but 1-block (crouch) is now valid

        // Teleport! - preserve look direction from when arrow was shot
        Location from = player.getLocation();
        
        // Get stored yaw/pitch from arrow metadata
        float yaw = 0, pitch = 0;
        if (arrow.hasMetadata(YAW_METADATA)) {
            yaw = arrow.getMetadata(YAW_METADATA).get(0).asFloat();
        }
        if (arrow.hasMetadata(PITCH_METADATA)) {
            pitch = arrow.getMetadata(PITCH_METADATA).get(0).asFloat();
        }
        
        safeLocation.setYaw(yaw);
        safeLocation.setPitch(pitch);
        
        player.teleport(safeLocation);

        // 🎵 Sound + ✨ Particles
        player.playSound(safeLocation, org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        player.getWorld().spawnParticle(org.bukkit.Particle.PORTAL, safeLocation, 50, 0.5, 0.5, 0.5, 0.5);

        // Record stats
        plugin.getGameManager().recordTeleport(player, from, safeLocation);

        // Apply fall damage reduction
        double fallReduction = plugin.getConfigManager().getFallDamageReduction();
        if (fallReduction < 1.0) {
            player.setNoDamageTicks((int) (20 * (1.0 - fallReduction))); // Temporary invulnerability
        }

        // Message
        String msg = plugin.getConfigManager().getMessage("teleport-success");
        player.sendMessage(MessageUtil.withPrefix(msg));

        // Remove arrow
        arrow.remove();
    }
}
