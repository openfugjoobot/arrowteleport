package com.openfugjoobot.arrowteleport.commands;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import com.openfugjoobot.arrowteleport.util.PermissionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /atStart command - Start challenge with countdown
 */
public class StartCommand extends BaseCommand {

    private final ArrowTeleport plugin;

    public StartCommand(ArrowTeleport plugin) {
        super("atstart", PermissionUtil.START, "Start challenge", "/atStart");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtil.withPrefix("\u0026cOnly players can use this command!"));
            return true;
        }

        // End existing session if any
        if (plugin.getGameManager().hasActiveSession(player)) {
            plugin.getGameManager().endSession(player);
        }

        player.sendMessage(MessageUtil.withPrefix("\u00267Starting challenge..."));

        // Run countdown then start
        plugin.getTimerManager().runCountdown(player, () -> {
            plugin.getGameManager().startSession(player);
            
            // Give kit automatically
            if (plugin.getConfigManager().isKitEnabled()) {
                plugin.getKitManager().giveKit(player);
            }
        });

        return true;
    }
}
