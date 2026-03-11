package com.openfugjoobot.arrowteleport.commands;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import com.openfugjoobot.arrowteleport.util.PermissionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * /atReload command - Reload configuration
 */
public class ReloadCommand extends BaseCommand {

    private final ArrowTeleport plugin;

    public ReloadCommand(ArrowTeleport plugin) {
        super("atreload", PermissionUtil.RELOAD, "Reload configuration", "/atReload");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadPlugin();
        sender.sendMessage(MessageUtil.withPrefix("\u0026aConfiguration reloaded!"));
        return true;
    }
}
