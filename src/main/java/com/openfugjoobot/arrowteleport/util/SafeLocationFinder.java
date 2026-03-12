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
        
        // If arrow hit a solid block, spawn player IN FRONT of the wall (not above!)
        if (isSolidBlock(hitMaterial)) {
            // Get the face the arrow hit
            Location relativeHit = new Location(world, hitX - blockX, hitY - hitBlock.getY(), hitZ - blockZ);
            BlockFace hitFace = determineHitFace(relativeHit);
            
            // Calculate block position in front of the hit block
            int playerBlockX = blockX + hitFace.getModX();
            int playerBlockZ = blockZ + hitFace.getModZ();
            
            // Calculate position 0.3 blocks in front of the wall (at arrow hit Y level)
            double playerX = playerBlockX + 0.5;
            double playerZ = playerBlockZ + 0.5;
            double playerY = hitY; // Keep same Y as arrow hit - don't go upward!
            
            // Check if this position has at least 1-block clearance (crouching)
            Block feetBlock = world.getBlockAt(playerBlockX, (int) Math.floor(playerY), playerBlockZ);
            Block headBlock = world.getBlockAt(playerBlockX, (int) Math.floor(playerY) + 1, playerBlockZ);
            
            // If 2-block clearance available, use standing position
            if (isPassableBlock(feetBlock.getType()) && isPassableBlock(headBlock.getType())) {
                return new Location(world, 
                    playerX,
                    Math.floor(playerY),
                    playerZ,
                    hitLocation.getYaw(),
                    hitLocation.getPitch()
                );
            }
            
            // If only 1-block clearance, still use it (crouch position)
            if (isPassableBlock(feetBlock.getType())) {
                return new Location(world, 
                    playerX,
                    Math.floor(playerY),
                    playerZ,
                    hitLocation.getYaw(),
                    hitLocation.getPitch()
                );
            }
            
            // Try 1 block lower
            int lowerY = (int) Math.floor(playerY) - 1;
            feetBlock = world.getBlockAt(playerBlockX, lowerY, playerBlockZ);
            headBlock = world.getBlockAt(playerBlockX, lowerY + 1, playerBlockZ);
            
            if (isPassableBlock(feetBlock.getType()) && isPassableBlock(headBlock.getType())) {
                return new Location(world, 
                    playerX,
                    lowerY,
                    playerZ,
                    hitLocation.getYaw(),
                    hitLocation.getPitch()
                );
            }
            
            if (isPassableBlock(feetBlock.getType())) {
                return new Location(world, 
                    playerX,
                    lowerY,
                    playerZ,
                    hitLocation.getYaw(),
                    hitLocation.getPitch()
                );
            }
            
            // No safe location found
            return null;
        }
        
        // Arrow hit air/non-solid - raycast upward from hit Y
        int startBlockY = (int) Math.floor(hitY);
        for (int offset = 0; offset < MAX_SEARCH_HEIGHT; offset++) {
            int checkY = startBlockY + offset;
            Block feetBlock = world.getBlockAt(blockX, checkY, blockZ);
            Block headBlock = world.getBlockAt(blockX, checkY + 1, blockZ);
            
            if (isPassableBlock(feetBlock.getType()) && isPassableBlock(headBlock.getType())) {
                Location safeLocation = new Location(world, 
                    blockX + 0.5,
                    checkY,
                    blockZ + 0.5
                );
                safeLocation.setYaw(hitLocation.getYaw());
                safeLocation.setPitch(hitLocation.getPitch());
                return safeLocation;
            }
        }
        
        return null;
    }

    /**
     * Determine which face of the block was hit based on relative coordinates
     */
    private static BlockFace determineHitFace(Location relativeHit) {
        double x = relativeHit.getX();
        double y = relativeHit.getY();
        double z = relativeHit.getZ();
        
        // Check which face is closest
        if (x <= 0.2) return BlockFace.WEST;
        if (x >= 0.8) return BlockFace.EAST;
        if (z <= 0.2) return BlockFace.NORTH;
        if (z >= 0.8) return BlockFace.SOUTH;
        if (y <= 0.2) return BlockFace.DOWN;
        return BlockFace.UP;
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
