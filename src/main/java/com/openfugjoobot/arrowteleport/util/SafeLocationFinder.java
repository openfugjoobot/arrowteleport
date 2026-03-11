package com.openfugjoobot.arrowteleport.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * 🛡️ COLLISION SAFETY SYSTEM
 * Finds safe teleport locations to prevent suffocation damage
 */
public class SafeLocationFinder {

    private static final int MAX_SEARCH_HEIGHT = 10;
    private static final double PLAYER_HEIGHT = 1.8; // Player is ~1.8 blocks tall
    private static final double SAFETY_MARGIN = 0.1;

    /**
     * Find a safe location for player teleportation
     * 
     * Algorithm:
     * 1. Start from arrow hit location
     * 2. Check if block at hit location is solid
     * 3. If solid, raycast upward to find first air block
     * 4. Ensure there's clearance for player (2 blocks minimum)
     * 5. Return adjusted location on top of safe block
     * 
     * @param hitLocation The arrow hit location
     * @return Safe teleport location, or null if none found
     */
    public static Location findSafeLocation(Location hitLocation) {
        if (hitLocation == null || hitLocation.getWorld() == null) {
            return null;
        }

        World world = hitLocation.getWorld();
        double hitX = hitLocation.getX();
        double hitY = hitLocation.getY();
        double hitZ = hitLocation.getZ();
        int blockX = hitLocation.getBlockX();
        int blockZ = hitLocation.getBlockZ();
        
        // Get the block the arrow hit
        Block hitBlock = hitLocation.getBlock();
        Material hitMaterial = hitBlock.getType();
        
        // Determine starting Y for search
        // If arrow hit a solid block, we need to be ON TOP of it
        double startY;
        if (isSolidBlock(hitMaterial)) {
            // Arrow hit solid - start from block above the hit block
            startY = hitBlock.getY() + 1;
        } else {
            // Arrow hit air/non-solid - start from arrow's Y
            startY = hitY;
        }

        // Raycast upward to find safe location
        int startBlockY = (int) Math.floor(startY);
        
        for (int offset = 0; offset < MAX_SEARCH_HEIGHT; offset++) {
            int checkY = startBlockY + offset;
            
            // Check if current Y and Y+1 have space for player
            Block feetBlock = world.getBlockAt(blockX, checkY, blockZ);
            Block headBlock = world.getBlockAt(blockX, checkY + 1, blockZ);
            
            // We need: feetBlock passable AND headBlock passable
            if (isPassableBlock(feetBlock.getType()) && isPassableBlock(headBlock.getType())) {
                
                // Found safe location - return centered on block with slight elevation
                Location safeLocation = new Location(world, 
                    blockX + 0.5,  // Center X
                    checkY,         // Feet level
                    blockZ + 0.5    // Center Z
                );
                
                // Set facing direction to match original
                safeLocation.setYaw(hitLocation.getYaw());
                safeLocation.setPitch(hitLocation.getPitch());
                
                return safeLocation;
            }
        }
        
        // No safe location found within range
        return null;
    }

    /**
     * Check if a block material is solid (would suffocate player)
     */
    private static boolean isSolidBlock(Material material) {
        if (material == null || material == Material.AIR || material == Material.CAVE_AIR) {
            return false;
        }
        
        // Check if material is solid
        // Note: Material.isSolid() returns true for glass, slabs, etc.
        // We need a stricter check
        return material.isSolid() && !material.isTransparent()
            && material != Material.WATER
            && material != Material.LAVA
            && !material.name().contains("GLASS")
            && !material.name().contains("FENCE")
            && !material.name().contains("WALL");
    }

    /**
     * Check if a block material is passable (safe for player)
     */
    private static boolean isPassableBlock(Material material) {
        if (material == null) return false;
        
        // Passable materials
        return material == Material.AIR 
            || material == Material.CAVE_AIR
            || material == Material.VOID_AIR
            || material == Material.WATER
            || material == Material.LAVA
            || material == Material.SHORT_GRASS
            || material == Material.TALL_GRASS
            || material == Material.FERN
            || material == Material.LARGE_FERN
            || material == Material.DEAD_BUSH
            || material == Material.SUGAR_CANE
            || material == Material.VINE
            || material == Material.GLOW_LICHEN
            || material == Material.SNOW
            || material.name().contains("TULIP")
            || material.name().contains("FLOWER")
            || material.name().contains("MUSHROOM")
            || material.isTransparent();
    }

    /**
     * Check if location is safe (for validation)
     */
    public static boolean isSafeLocation(Location location) {
        if (location == null || location.getWorld() == null) return false;
        
        Block feetBlock = location.getBlock();
        Block headBlock = feetBlock.getRelative(BlockFace.UP);
        
        return isPassableBlock(feetBlock.getType()) && isPassableBlock(headBlock.getType());
    }
}
