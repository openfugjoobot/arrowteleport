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
     * Find teleport spot based on hit face:
     * - NORTH/SOUTH/EAST/WEST: 1 block in front of wall
     * - UP: on top of block (1-block clearance OK)
     */
    public static Location findSafeLocation(Location hitLocation) {
        if (hitLocation == null || hitLocation.getWorld() == null) {
            return null;
        }

        World world = hitLocation.getWorld();
        Block hitBlock = hitLocation.getBlock();
        
        // Get hit face
        BlockFace hitFace = getHitFace(hitLocation, hitBlock);
        
        // UP hit: player shoots onto top of block
        if (hitFace == BlockFace.UP) {
            // Teleport ON TOP of the block
            int y = hitBlock.getY() + 1; // One block above hit block
            Block feetBlock = world.getBlockAt(hitBlock.getX(), y, hitBlock.getZ());
            
            if (isPassable(feetBlock)) {
                return new Location(world, 
                    hitBlock.getX() + 0.5, y, hitBlock.getZ() + 0.5,
                    hitLocation.getYaw(), hitLocation.getPitch());
            }
            
            return null;
        }
        
        // DOWN hit (rare: shooting from above)
        if (hitFace == BlockFace.DOWN) {
            // Teleport 1 block below
            int y = hitBlock.getY() - 1;
            Block feetBlock = world.getBlockAt(hitBlock.getX(), y, hitBlock.getZ());
            
            if (isPassable(feetBlock)) {
                return new Location(world, 
                    hitBlock.getX() + 0.5, y, hitBlock.getZ() + 0.5,
                    hitLocation.getYaw(), hitLocation.getPitch());
            }
            
            return null;
        }
        
        // Wall hits (NORTH/SOUTH/EAST/WEST): in front of wall
        int targetX = hitBlock.getX() + hitFace.getModX();
        int targetZ = hitBlock.getZ() + hitFace.getModZ();
        int targetY = hitLocation.getBlockY();
        
        Block feetBlock = world.getBlockAt(targetX, targetY, targetZ);
        if (isPassable(feetBlock)) {
            return new Location(world, targetX + 0.5, targetY, targetZ + 0.5, 
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        return null;
    }

    /**
     * Get hit face from relative coordinates
     */
    private static BlockFace getHitFace(Location hit, Block block) {
        double x = hit.getX() - block.getX();
        double y = hit.getY() - block.getY();
        double z = hit.getZ() - block.getZ();
        
        // UP: arrow hit top surface (y > 0.85)
        if (y > 0.85) return BlockFace.UP;
        
        // DOWN: arrow hit bottom surface (y < 0.15)
        if (y < 0.15) return BlockFace.DOWN;
        
        // Wall faces
        if (z < 0.3) return BlockFace.NORTH;
        if (z > 0.7) return BlockFace.SOUTH;
        if (x < 0.3) return BlockFace.WEST;
        if (x > 0.7) return BlockFace.EAST;
        
        // Center hit (through 1x1 hole)
        return BlockFace.NORTH;
    }

    /**
     * Check if feet position is passable
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
            || name.contains("PANE")
            || name.contains("FENCE");
    }
}
