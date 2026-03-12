package com.openfugjoobot.arrowteleport.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * 🛡️ Wall Teleport - Position in front of hit block
 * Accepts 1-block clearance for 1x1 holes (crawling)
 */
public class SafeLocationFinder {

    /**
     * Find teleport spot: IN FRONT of hit block (between player and wall)
     * Accepts 1-block height (crawling position for 1x1 holes)
     */
    public static Location findSafeLocation(Location hitLocation) {
        if (hitLocation == null || hitLocation.getWorld() == null) {
            return null;
        }

        World world = hitLocation.getWorld();
        Block hitBlock = hitLocation.getBlock();
        
        // Determine which face the arrow hit
        BlockFace hitFace = getHitFace(hitLocation, hitBlock);
        
        // Target: 1 block in front of the wall, same Y level
        int targetX = hitBlock.getX() + hitFace.getModX();
        int targetZ = hitBlock.getZ() + hitFace.getModZ();
        int targetY = hitLocation.getBlockY();
        
        // Check if feet position is passable (air, water, non-solid)
        Block feetBlock = world.getBlockAt(targetX, targetY, targetZ);
        if (isPassable(feetBlock)) {
            // ✅ Valid spot - standing OR crawling (1x1 hole accepted)
            return new Location(world, targetX + 0.5, targetY, targetZ + 0.5, 
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        return null; // Block is solid, no space
    }

    /**
     * Simple hit face: which side of block did arrow hit?
     */
    private static BlockFace getHitFace(Location hit, Block block) {
        double x = hit.getX() - block.getX();
        double z = hit.getZ() - block.getZ();
        
        if (z < 0.3) return BlockFace.NORTH;   // Arrow hit south face
        if (z > 0.7) return BlockFace.SOUTH;   // Arrow hit north face
        if (x < 0.3) return BlockFace.WEST;    // Arrow hit east face
        if (x > 0.7) return BlockFace.EAST;    // Arrow hit west face
        
        // Center hit - arrow flew through 1x1 hole into wall
        return BlockFace.NORTH;  // Default forward
    }

    /**
     * Check if player can stand here (feet only - head checked by server collision)
     */
    private static boolean isPassable(Block block) {
        Material m = block.getType();
        
        // Air, water, lava always OK
        if (m == Material.AIR || m == Material.WATER || m == Material.LAVA) {
            return true;
        }
        
        // Non-solid blocks: signs, rails, torches, plants, etc.
        if (!m.isSolid()) {
            return true;
        }
        
        // Transparent blocks: glass, panes, fences
        String name = m.name();
        return name.contains("GLASS") 
            || name.contains("PANE")
            || name.contains("FENCE")
            || name.contains("RAIL");
    }
}
