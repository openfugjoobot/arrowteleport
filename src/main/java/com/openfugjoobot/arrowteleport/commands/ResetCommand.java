package com.openfugjoobot.arrowteleport.commands;

import com.openfugjoobot.arrowteleport.ArrowTeleport;
import com.openfugjoobot.arrowteleport.util.MessageUtil;
import com.openfugjoobot.arrowteleport.util.PermissionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

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
        // Check for admin full-reset mode
        if (args.length > 0 && args[0].equalsIgnoreCase("full")) {
            if (!sender.hasPermission("arrowteleport.admin")) {
                sender.sendMessage(MessageUtil.withPrefix("\u0026cAdmin permission required!"));
                return true;
            }
            
            sender.sendMessage(MessageUtil.withPrefix("&6Starting full server reset..."));
            sender.sendMessage(MessageUtil.withPrefix("&eThis will delete all worlds and restart the server!"));
            
            // Schedule full reset (delete worlds + restart)
            plugin.getLogger().warning("FULL RESET INITIATED by " + sender.getName());
            
            // Stop all active sessions
            for (org.bukkit.entity.Player p : sender.getServer().getOnlinePlayers()) {
                if (plugin.getGameManager().hasActiveSession(p)) {
                    plugin.getGameManager().endSession(p);
                    p.sendMessage(MessageUtil.withPrefix("&cServer resetting - challenge ended!"));
                }
            }
            
            // Schedule world delete + restart (1 second delay)
            new org.bukkit.scheduler.BukkitRunnable() {
                @Override
                public void run() {
                    // Get server directory
                    File serverDir = new File(".");
                    String[] worlds = {"world", "world_the_end", "world_nether"};
                    
                    for (String worldName : worlds) {
                        File worldDir = new File(serverDir, worldName);
                        if (worldDir.exists()) {
                            deleteDirectory(worldDir);
                            plugin.getLogger().info("Deleted world: " + worldName);
                        }
                    }
                    
                    // Trigger server restart via kick with message
                    sender.getServer().shutdown();
                    
                    plugin.getLogger().warning("Server shutdown - worlds deleted. Restart required.");
                }
            }.runTaskLater(plugin, 20L);
            
            return true;
        }
        
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

    private void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }
}
