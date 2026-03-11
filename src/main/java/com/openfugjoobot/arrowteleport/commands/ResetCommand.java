package com.openfugjoobot.arrowteleport.commands;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import com.openfugjoobot.arrowteleport.util.PermissionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /atReset command - Reset player to spawn
 */
public class ResetCommand extends BaseCommand {

    private final ArrowTeleport plugin;

    public ResetCommand(ArrowTeleport plugin) {
        super("atreset", PermissionUtil.RESET, "Reset to spawn", "/atReset");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtil.withPrefix("\u0026cOnly players can use this command!"));
            return true;
        }

        // End current session if active
        if (plugin.getGameManager().hasActiveSession(player)) {
            plugin.getGameManager().endSession(player);
            player.sendMessage(MessageUtil.withPrefix("\u00267Session ended."));
        }

        // Teleport to spawn
        plugin.getGameManager().resetPlayer(player);
        player.sendMessage(MessageUtil.withPrefix("\u0026aYou have been reset to spawn!"));

        return true;
    }
}
