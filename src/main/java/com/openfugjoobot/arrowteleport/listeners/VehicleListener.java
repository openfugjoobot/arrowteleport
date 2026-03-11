package com.openfugjoobot.arrowteleport.listeners;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.spigotmc.event.entity.EntityMountEvent;

/**
 * Blocks player from riding entities and vehicles
 */
public class VehicleListener implements Listener {

    private final ArrowTeleport plugin;

    public VehicleListener(ArrowTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityMount(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        if (plugin.getConfigManager().isRestrictionEnabled("block-riding")) {
            event.setCancelled(true);
            player.sendMessage(MessageUtil.withPrefix("\u0026cRiding is disabled!"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player player)) {
            return;
        }

        if (!plugin.getGameManager().hasActiveSession(player)) {
            return;
        }

        if (plugin.getConfigManager().isRestrictionEnabled("block-vehicles")) {
            event.setCancelled(true);
            player.sendMessage(MessageUtil.withPrefix("\u0026cVehicles are disabled!"));
        }
    }
}
