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
            
            // Check 2-block clearance first (standing)
            if (isPassableBlock(feetBlock.getType()) && isPassableBlock(headBlock.getType())) {
                return new Location(world, 
                    playerX,
                    Math.floor(playerY),
                    playerZ,
                    hitLocation.getYaw(),
                    hitLocation.getPitch()
                );
            }
            
            // Accept 1-block clearance (1x1 hole - lying position)
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
            
            // 2-block clearance at lower Y
            Block headBlockLower = world.getBlockAt(playerBlockX, lowerY + 1, playerBlockZ);
            if (isPassableBlock(feetBlock.getType()) && isPassableBlock(headBlockLower.getType())) {
                return new Location(world, 
                    playerX,
                    lowerY,
                    playerZ,
                    hitLocation.getYaw(),
                    hitLocation.getPitch()
                );
            }
            
            // 1-block clearance at lower Y (1x1 hole)
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
     * For 1x1 holes, arrow hits center - use trajectory to guess correct face
     */
    private static BlockFace determineHitFace(Location relativeHit) {
        double x = relativeHit.getX();
        double y = relativeHit.getY();
        double z = relativeHit.getZ();
        
        // Sharp edge hits (< 0.15 or > 0.85) = clear face
        if (x < 0.15) return BlockFace.WEST;
        if (x > 0.85) return BlockFace.EAST;
        if (z < 0.15) return BlockFace.NORTH;
        if (z > 0.85) return BlockFace.SOUTH;
        
        // Center hits (0.3-0.7) - arrow came through hole, use Z axis as default
        // (most common: shooting INTO wall)
        if (x > 0.3 && x < 0.7 && z > 0.3 && z < 0.7) {
            // Arrow came from somewhere - assume NORTH (positive Z direction)
            // This is the most common case: shooting at wall
            return BlockFace.NORTH;
        }
        
        // Ambiguous hits - fall back to primary axis
        if (Math.abs(x - 0.5) > Math.abs(z - 0.5)) {
            // X axis is more extreme
            return x < 0.5 ? BlockFace.WEST : BlockFace.EAST;
        } else {
            // Z axis is more extreme
            return z < 0.5 ? BlockFace.NORTH : BlockFace.SOUTH;
        }
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
     * Simple check: air, water, lava, plants, glass, fences, etc.
     */
    private static boolean isPassableBlock(Material material) {
        if (material == null) return false;
        
        // Definitely passable
        if (material == Material.AIR 
            || material == Material.CAVE_AIR
            || material == Material.VOID_AIR
            || material == Material.WATER
            || material == Material.LAVA) {
            return true;
        }
        
        // Plants, flowers, signs, rails, etc. - all non-solid
        if (!material.isSolid()) {
            return true;
        }
        
        // Explicitly allow some transparent/solid-ish blocks
        String name = material.name();
        return name.contains("GLASS")
            || name.contains("FENCE")
            || name.contains("WALL")
            || name.contains("PANE")
            || name.contains("SIGN")
            || name.contains("RAIL")
            || name.contains("TORCH")
            || name.contains("BUTTON")
            || name.contains("FLOWER")
            || name.contains("GRASS")
            || name.contains("VINE")
            || name.contains("LADDER");
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
