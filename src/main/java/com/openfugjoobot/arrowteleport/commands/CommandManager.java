package com.openfugjoobot.arrowteleport.commands;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import org.bukkit.command.PluginCommand;

/**
 * Handles command registration
 */
public class CommandManager {

    private final ArrowTeleport plugin;

    public CommandManager(ArrowTeleport plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        registerCommand("arrowteleport", new MainCommand(plugin), "at");
        registerCommand("atreset", new ResetCommand(plugin));
        registerCommand("atstart", new StartCommand(plugin));
        registerCommand("atkit", new KitCommand(plugin));
        registerCommand("atstats", new StatsCommand(plugin));
        registerCommand("atreload", new ReloadCommand(plugin));
    }

    private void registerCommand(String commandName, BaseCommand executor, String... aliases) {
        PluginCommand command = plugin.getCommand(commandName);
        if (command != null) {
            command.setExecutor(executor);
            command.setTabCompleter(executor);
            
            // Register aliases
            if (aliases != null && aliases.length > 0) {
                // Aliases are defined in plugin.yml
            }
        } else {
            plugin.getLogger().warning("Failed to register command: " + commandName);
        }
    }
}
