package com.openfugjoobot.arrowteleport.commands;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.game.PlayerData;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import com.openfugjoobot.arrowteleport.util.PermissionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /atStats command - Show player statistics
 */
public class StatsCommand extends BaseCommand {

    private final ArrowTeleport plugin;

    public StatsCommand(ArrowTeleport plugin) {
        super("atstats", PermissionUtil.STATS, "Show statistics", "/atStats");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtil.withPrefix("\u0026cOnly players can use this command!"));
            return true;
        }

        PlayerData data = plugin.getGameManager().getPlayerData(player);
        boolean inGame = plugin.getGameManager().hasActiveSession(player);

        // Header
        sender.sendMessage(MessageUtil.colorize(""));
        sender.sendMessage(MessageUtil.colorize("\u00266========== \u0026eArrowTeleport Stats \u00266=========="));
        sender.sendMessage(MessageUtil.colorize(""));

        // Current Session
        if (inGame) {
            sender.sendMessage(MessageUtil.colorize("\u0026b\u0026lCurrent Session:"));
            sender.sendMessage(MessageUtil.colorize("  \u00267Time: \u0026f" + MessageUtil.formatTime(data.getSessionSeconds())));
            sender.sendMessage(MessageUtil.colorize("  \u00267Arrows Shot: \u0026f" + data.getSessionArrowsShot()));
            sender.sendMessage(MessageUtil.colorize("  \u00267Distance: \u0026f" + MessageUtil.formatDistance(data.getSessionDistance()) + " blocks"));
            sender.sendMessage(MessageUtil.colorize(""));
        }

        // Lifetime Stats
        sender.sendMessage(MessageUtil.colorize("\u0026b\u0026lLifetime Stats:"));
        sender.sendMessage(MessageUtil.colorize("  \u00267Total Distance: \u0026f" + MessageUtil.formatDistance(data.getTotalDistance()) + " blocks"));
        sender.sendMessage(MessageUtil.colorize("  \u00267Total Arrows: \u0026f" + data.getArrowsShot()));
        sender.sendMessage(MessageUtil.colorize("  \u00267Play Time: \u0026f" + MessageUtil.formatTime((int) data.getTotalTimeSeconds())));
        sender.sendMessage(MessageUtil.colorize("  \u00267Sessions: \u0026f" + data.getSessionsCompleted()));
        
        if (data.getBestTime() > 0) {
            sender.sendMessage(MessageUtil.colorize("  \u00267Best Time: \u0026f" + MessageUtil.formatTime((int) data.getBestTime())));
        }

        sender.sendMessage(MessageUtil.colorize(""));
        sender.sendMessage(MessageUtil.colorize("\u00266======================================"));
        sender.sendMessage(MessageUtil.colorize(""));

        return true;
    }
}
