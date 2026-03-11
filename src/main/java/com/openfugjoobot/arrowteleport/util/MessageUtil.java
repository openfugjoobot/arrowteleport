package com.openfugjoobot.arrowteleport.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;

/**
 * Utility class for message formatting
 */
public class MessageUtil {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    
    public static String PREFIX = "&8[&6ArrowTeleport&8] &r";

    /**
     * Colorize a legacy string using '&' color codes
     */
    public static String colorize(String message) {
        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Apply prefix to a message
     */
    public static String withPrefix(String message) {
        return colorize(PREFIX + message);
    }

    /**
     * Create a minimessage component (for action bars)
     */
    public static Component miniMessage(String message) {
        return miniMessage.deserialize(message);
    }

    /**
     * Format a time in seconds to MM:SS
     */
    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    /**
     * Format a distance with 1 decimal place
     */
    public static String formatDistance(double distance) {
        return String.format("%.1f", distance);
    }
}
