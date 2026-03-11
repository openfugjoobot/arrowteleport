package com.openfugjoobot.arrowteleport.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 * Base class for ArrowTeleport commands
 */
public abstract class BaseCommand implements CommandExecutor, TabCompleter {

    protected final String name;
    protected final String permission;
    protected final String description;
    protected final String usage;

    public BaseCommand(String name, String permission, String description, String usage) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.usage = usage;
    }

    public String getName() { return name; }
    public String getPermission() { return permission; }
    public String getDescription() { return description; }
    public String getUsage() { return usage; }

    @Override
    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null; // Override in subclasses if needed
    }
}
