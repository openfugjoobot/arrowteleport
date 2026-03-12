package com.openfugjoobot.arrowteleport.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * 🛡️ Simple Collision Safety - Teleport to wall, don't suffocate
 */
public class SafeLocationFinder {

    /**
     * Find teleport spot: IN FRONT of hit block, same Y level
     * Returns null only if completely boxed in
     */
    public static Location findSafeLocation(Location hitLocation) {
        if (hitLocation == null || hitLocation.getWorld() == null) {
            return null;
        }

        World world = hitLocation.getWorld();
        Block hitBlock = hitLocation.getBlock();
        
        // Determine which direction the arrow came from
        BlockFace hitFace = getHitFace(hitLocation, hitBlock);
        
        // Position: 1 block in front of the wall (XZ centered)
        int targetX = hitBlock.getX() + hitFace.getModX();
        int targetZ = hitBlock.getZ() + hitFace.getModZ();
        int targetY = hitLocation.getBlockY();
        
        // Check feet position - if passable, we're good (standing or crouching)
        Block feetBlock = world.getBlockAt(targetX, targetY, targetZ);
        if (isPassable(feetBlock)) {
            return new Location(world, targetX + 0.5, targetY, targetZ + 0.5, 
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        // Try 1 block down (common case: arrow hit slightly above ground)
        feetBlock = world.getBlockAt(targetX, targetY - 1, targetZ);
        if (isPassable(feetBlock)) {
            return new Location(world, targetX + 0.5, targetY - 1, targetZ + 0.5, 
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        // Try 1 block up (rare: arrow hit low on wall)
        feetBlock = world.getBlockAt(targetX, targetY + 1, targetZ);
        if (isPassable(feetBlock)) {
            return new Location(world, targetX + 0.5, targetY + 1, targetZ + 0.5, 
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        return null; // Truly no space
    }

    /**
     * Simple hit face detection - arrow trajectory based
     */
    private static BlockFace getHitFace(Location hit, Block block) {
        double x = hit.getX() - block.getX();
        double z = hit.getZ() - block.getZ();
        
        // Wall hits (most common)
        if (z < 0.3) return BlockFace.NORTH;  // Hit south side of block
        if (z > 0.7) return BlockFace.SOUTH;  // Hit north side
        if (x < 0.3) return BlockFace.WEST;   // Hit east side
        if (x > 0.7) return BlockFace.EAST;   // Hit west side
        
        // Center hit - arrow flying through hole into wall
        // Default to NORTH (player shooting forward into wall)
        return BlockFace.NORTH;
    }

    /**
     * Is this block walkable/standable?
     * Air, water, plants, glass, signs, rails = YES
     * Stone, dirt, wood, ore = NO
     */
    private static boolean isPassable(Block block) {
        Material m = block.getType();
        
        // Gases/liquids always passable
        if (m == Material.AIR || m == Material.WATER || m == Material.LAVA) {
            return true;
        }
        
        // Non-solid blocks (plants, signs, rails, torches, etc.)
        if (!m.isSolid()) {
            return true;
        }
        
        // Transparent/special blocks (glass, fences, walls, ladders)
        String name = m.name();
        return name.contains("GLASS") 
            || name.contains("FENCE") 
            || name.contains("WALL")
            || name.contains("PANE")
            || name.contains("SIGN")
            || name.contains("LADDER")
            || name.contains("RAIL")
            || name.contains("TORCH");
    }
}
