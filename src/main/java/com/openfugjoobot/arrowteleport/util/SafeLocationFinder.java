package com.openfugjoobot.arrowteleport.util;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * 🛡️ Trust Minecraft - TP to exact arrow hit, let server fix collisions
 */
public class SafeLocationFinder {

    /**
     * Return exact arrow hit location
     * Minecraft auto-corrects invalid positions (suffocation snap)
     */
    public static Location findSafeLocation(Location hitLocation) {
        if (hitLocation == null || hitLocation.getWorld() == null) {
            return null;
        }

        World world = hitLocation.getWorld();
        
        // Return hit location with yaw/pitch from shot
        return new Location(world, 
            hitLocation.getX(), 
            hitLocation.getY(), 
            hitLocation.getZ(),
            hitLocation.getYaw(), 
            hitLocation.getPitch());
    }
}
