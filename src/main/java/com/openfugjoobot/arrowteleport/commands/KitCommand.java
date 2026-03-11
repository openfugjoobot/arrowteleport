package com.openfugjoobot.arrowteleport.commands;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import com.openfugjoobot.arrowteleport.util.PermissionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /atKit command - Give starter kit
 */
public class KitCommand extends BaseCommand {

    private final ArrowTeleport plugin;

    public KitCommand(ArrowTeleport plugin) {
        super("atkit", PermissionUtil.KIT, "Get starter kit", "/atKit");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtil.withPrefix("\u0026cOnly players can use this command!"));
            return true;
        }

        plugin.getKitManager().giveKit(player);
        return true;
    }
}
