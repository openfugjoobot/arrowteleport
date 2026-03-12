package com.openfugjoobot.arrowteleport.listeners;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Handles player join events - resume active sessions after relog
 */
public class JoinListener implements Listener {

    private final ArrowTeleport plugin;

    public JoinListener(ArrowTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Resume existing session after short delay (wait for login to complete)
        if (plugin.getGameManager().getPlayerData(player).isInGame()) {
            // Delay 3 ticks (~60ms) to ensure player is fully connected
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getGameManager().resumeSession(player);
                    player.sendMessage(MessageUtil.withPrefix("&aChallenge resumed!"));
                }
            }.runTaskLater(plugin, 3L);
        }
    }
}
