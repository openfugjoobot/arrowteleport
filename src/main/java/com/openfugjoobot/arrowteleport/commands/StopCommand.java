package com.openfugjoobot.arrowteleport.commands;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import com.openfugjoobot.arrowteleport.util.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /atStop command - Stop challenge (ends session for all players or specific player)
 */
public class StopCommand extends BaseCommand {

    private final ArrowTeleport plugin;

    public StopCommand(ArrowTeleport plugin) {
        super("atstop", PermissionUtil.ADMIN, "Stop challenge", "/atStop [player]");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            // Stop specific player's session
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(MessageUtil.withPrefix("&cPlayer not found: " + args[0]));
                return true;
            }
            
            if (plugin.getGameManager().hasActiveSession(target)) {
                plugin.getGameManager().endSession(target);
                target.sendMessage(MessageUtil.withPrefix("&eChallenge stopped for you!"));
                sender.sendMessage(MessageUtil.withPrefix("&aStopped challenge for " + target.getName()));
            } else {
                sender.sendMessage(MessageUtil.withPrefix("&c" + target.getName() + " has no active session"));
            }
        } else {
            // Stop all active sessions
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (plugin.getGameManager().hasActiveSession(player)) {
                    plugin.getGameManager().endSession(player);
                    player.sendMessage(MessageUtil.withPrefix("&eChallenge stopped!"));
                }
            }
            sender.sendMessage(MessageUtil.withPrefix("&aAll challenges stopped"));
        }

        return true;
    }
}
