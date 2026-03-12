package com.openfugjoobot.arrowteleport.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * 🛡️ Simple Collision Safety - Teleport to wall, slide sideways if blocked
 */
public class SafeLocationFinder {

    /**
     * Find teleport spot: IN FRONT of hit block, same Y level
     * Slides left/right along the wall if center is blocked
     */
    public static Location findSafeLocation(Location hitLocation) {
        if (hitLocation == null || hitLocation.getWorld() == null) {
            return null;
        }

        World world = hitLocation.getWorld();
        Block hitBlock = hitLocation.getBlock();
        
        // Determine which direction the arrow came from
        BlockFace hitFace = getHitFace(hitLocation, hitBlock);
        
        // Primary: 1 block in front of the wall (centered)
        int baseX = hitBlock.getX() + hitFace.getModX();
        int baseZ = hitBlock.getZ() + hitFace.getModZ();
        int y = hitLocation.getBlockY();
        
        // Try center position
        if (isPassable(world.getBlockAt(baseX, y, baseZ))) {
            return new Location(world, baseX + 0.5, y, baseZ + 0.5, 
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        // Slide along the wall: try left and right offsets
        // Get perpendicular face for sliding direction
        BlockFace slideFace = getSlideFace(hitFace);
        
        // Try 1 block left
        int leftX = baseX + slideFace.getModX();
        int leftZ = baseZ + slideFace.getModZ();
        if (isPassable(world.getBlockAt(leftX, y, leftZ))) {
            return new Location(world, leftX + 0.5, y, leftZ + 0.5, 
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        // Try 1 block right (opposite direction)
        int rightX = baseX - slideFace.getModX();
        int rightZ = baseZ - slideFace.getModZ();
        if (isPassable(world.getBlockAt(rightX, y, rightZ))) {
            return new Location(world, rightX + 0.5, y, rightZ + 0.5, 
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        // Try 2 blocks left (wider slide)
        leftX = baseX + (slideFace.getModX() * 2);
        leftZ = baseZ + (slideFace.getModZ() * 2);
        if (isPassable(world.getBlockAt(leftX, y, leftZ))) {
            return new Location(world, leftX + 0.5, y, leftZ + 0.5, 
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        return null; // Truly boxed in
    }

    /**
     * Simple hit face detection
     */
    private static BlockFace getHitFace(Location hit, Block block) {
        double x = hit.getX() - block.getX();
        double z = hit.getZ() - block.getZ();
        
        if (z < 0.3) return BlockFace.NORTH;
        if (z > 0.7) return BlockFace.SOUTH;
        if (x < 0.3) return BlockFace.WEST;
        if (x > 0.7) return BlockFace.EAST;
        
        // Center hit - default NORTH
        return BlockFace.NORTH;
    }

    /**
     * Get perpendicular face for sliding along wall
     */
    private static BlockFace getSlideFace(BlockFace hitFace) {
        switch (hitFace) {
            case NORTH:
            case SOUTH:
                return BlockFace.EAST;  // Slide along X axis
            case EAST:
            case WEST:
                return BlockFace.NORTH; // Slide along Z axis
            default:
                return BlockFace.EAST;
        }
    }

    /**
     * Is this block passable?
     */
    private static boolean isPassable(Block block) {
        Material m = block.getType();
        
        if (m == Material.AIR || m == Material.WATER || m == Material.LAVA) {
            return true;
        }
        
        if (!m.isSolid()) {
            return true;
        }
        
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
