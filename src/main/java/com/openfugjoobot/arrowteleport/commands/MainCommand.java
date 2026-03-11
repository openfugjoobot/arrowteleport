package com.openfugjoobot.arrowteleport.commands;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import com.openfugjoobot.arrowteleport.util.PermissionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Main /arrowteleport or /at command
 */
public class MainCommand extends BaseCommand {

    private final ArrowTeleport plugin;

    public MainCommand(ArrowTeleport plugin) {
        super("arrowteleport", PermissionUtil.USE, "Main ArrowTeleport command", "/at [help|status]");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // Show help
            showHelp(sender);
            return true;
        }

        String subcommand = args[0].toLowerCase();
        
        switch (subcommand) {
            case "help":
            case "?":
                showHelp(sender);
                break;
            case "status":
                showStatus(sender);
                break;
            default:
                sender.sendMessage(MessageUtil.withPrefix("\u0026cUnknown subcommand. Use /at help"));
                break;
        }
        
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(MessageUtil.colorize("\u00266========== ArrowTeleport Help =========="));
        sender.sendMessage(MessageUtil.colorize("\u0026e/at help\u00267 - Show this help"));
        sender.sendMessage(MessageUtil.colorize("\u0026e/at status\u00267 - Show your game status"));
        sender.sendMessage(MessageUtil.colorize("\u0026e/atstart\u00267 - Start a new challenge"));
        sender.sendMessage(MessageUtil.colorize("\u0026e/atreset\u00267 - Reset to spawn"));
        sender.sendMessage(MessageUtil.colorize("\u0026e/atkit\u00267 - Get starter kit"));
        sender.sendMessage(MessageUtil.colorize("\u0026e/atstats\u00267 - View your statistics"));
        
        if (sender.hasPermission(PermissionUtil.ADMIN)) {
            sender.sendMessage(MessageUtil.colorize("\u0026e/atreload\u00267 - Reload config (admin)"));
        }
        
        sender.sendMessage(MessageUtil.colorize("\u00266========================================"));
    }

    private void showStatus(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtil.withPrefix("\u0026cOnly players can check status!"));
            return;
        }

        var data = plugin.getGameManager().getPlayerData(player);
        boolean inGame = plugin.getGameManager().hasActiveSession(player);

        sender.sendMessage(MessageUtil.colorize("\u00266========== Your Status =========="));
        sender.sendMessage(MessageUtil.colorize("\u0026eIn Game: \u0026f" + (inGame ? "Yes" : "No")));
        
        if (inGame) {
            sender.sendMessage(MessageUtil.colorize("\u0026eTime: \u0026f" + MessageUtil.formatTime(data.getSessionSeconds())));
            sender.sendMessage(MessageUtil.colorize("\u0026eArrows: \u0026f" + data.getSessionArrowsShot()));
            sender.sendMessage(MessageUtil.colorize("\u0026eDistance: \u0026f" + MessageUtil.formatDistance(data.getSessionDistance())));
        } else {
            sender.sendMessage(MessageUtil.colorize("\u0026eTotal Distance: \u0026f" + MessageUtil.formatDistance(data.getTotalDistance())));
            sender.sendMessage(MessageUtil.colorize("\u0026eTotal Arrows: \u0026f" + data.getArrowsShot()));
            sender.sendMessage(MessageUtil.colorize("\u0026eSessions: \u0026f" + data.getSessionsCompleted()));
        }
        
        sender.sendMessage(MessageUtil.colorize("\u00266================================="));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("help");
            completions.add("status");
            return completions;
        }
        return null;
    }
}
