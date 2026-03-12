package com.openfugjoobot.arrowteleport.listeners;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        
        // Check if player has an existing (but inactive) session
        if (plugin.getGameManager().getPlayerData(player).isInGame()) {
            // Resume the session
            plugin.getGameManager().resumeSession(player);
            player.sendMessage(MessageUtil.withPrefix("&aChallenge resumed!"));
        }
    }
}
