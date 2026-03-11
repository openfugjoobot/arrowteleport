package com.openfugjoobot.arrowteleport.game;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages timer display for active game sessions
 */
public class TimerManager {

    private final ArrowTeleport plugin;
    private final Map<UUID, BukkitRunnable> activeTimers;
    private final MiniMessage miniMessage;

    public TimerManager(ArrowTeleport plugin) {
        this.plugin = plugin;
        this.activeTimers = new ConcurrentHashMap<>();
        this.miniMessage = MiniMessage.miniMessage();
    }

    /**
     * Start action bar timer for player
     */
    public void startTimer(Player player) {
        stopTimer(player); // Stop existing if any

        PlayerData data = plugin.getGameManager().getPlayerData(player);
        String format = plugin.getConfigManager().getTimerFormat();

        BukkitRunnable timer = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !data.isInGame()) {
                    cancel();
                    activeTimers.remove(player.getUniqueId());
                    return;
                }

                // Build display message
                int totalSeconds = data.getSessionSeconds();
                int minutes = totalSeconds / 60;
                int seconds = totalSeconds % 60;
                int arrows = data.getSessionArrowsShot();

                String message = format
                    .replace("%minutes%", String.format("%02d", minutes))
                    .replace("%seconds%", String.format("%02d", seconds))
                    .replace("%arrows%", String.valueOf(arrows))
                    .replace("%distance%", MessageUtil.formatDistance(data.getSessionDistance()));

                message = MessageUtil.colorize(message);

                // Send action bar
                player.sendActionBar(miniMessage.deserialize(
                    message.replace('&', '§')));
            }
        };

        timer.runTaskTimer(plugin, 0L, 20L); // Update every second
        activeTimers.put(player.getUniqueId(), timer);
    }

    /**
     * Stop timer for player
     */
    public void stopTimer(Player player) {
        BukkitRunnable timer = activeTimers.remove(player.getUniqueId());
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * Run countdown before starting
     */
    public void runCountdown(Player player, Runnable onComplete) {
        int countdownSeconds = plugin.getConfigManager().getCountdownSeconds();

        new BukkitRunnable() {
            int remaining = countdownSeconds;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                if (remaining > 0) {
                    String color = remaining == 1 ? "&c" : (remaining == 2 ? "&e" : "&a");
                    String title = color + remaining;
                    String subtitle = "&7Get ready...";

                    player.sendTitle(
                        MessageUtil.colorize(title),
                        MessageUtil.colorize(subtitle),
                        0, 25, 0
                    );
                    remaining--;
                } else {
                    player.sendTitle(
                        MessageUtil.colorize("&6GO!"),
                        "",
                        0, 20, 10
                    );
                    cancel();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Update every second
    }

    /**
     * Stop all timers on shutdown
     */
    public void shutdown() {
        for (BukkitRunnable timer : activeTimers.values()) {
            timer.cancel();
        }
        activeTimers.clear();
    }

    public boolean hasTimer(Player player) {
        return activeTimers.containsKey(player.getUniqueId());
    }
}
