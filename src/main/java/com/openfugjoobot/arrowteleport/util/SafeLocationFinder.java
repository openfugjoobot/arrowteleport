package com.openfugjoobot.arrowteleport.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * 🛡️ Teleport to arrow hit location - trust the shot!
 */
public class SafeLocationFinder {

    /**
     * Teleport player to arrow hit position:
     * - UP: on top of block
     * - Wall: in front of wall
     * - DOWN: below block
     */
    public static Location findSafeLocation(Location hitLocation) {
        if (hitLocation == null || hitLocation.getWorld() == null) {
            return null;
        }

        World world = hitLocation.getWorld();
        Block hitBlock = hitLocation.getBlock();
        
        // Get hit face
        BlockFace hitFace = getHitFace(hitLocation, hitBlock);
        
        // UP: on top of block
        if (hitFace == BlockFace.UP) {
            return new Location(world, 
                hitBlock.getX() + 0.5, 
                hitBlock.getY() + 1, 
                hitBlock.getZ() + 0.5,
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        // DOWN: below block
        if (hitFace == BlockFace.DOWN) {
            return new Location(world, 
                hitBlock.getX() + 0.5, 
                hitBlock.getY() - 1, 
                hitBlock.getZ() + 0.5,
                hitLocation.getYaw(), hitLocation.getPitch());
        }
        
        // Wall: 1 block in front
        int targetX = hitBlock.getX() + hitFace.getModX();
        int targetZ = hitBlock.getZ() + hitFace.getModZ();
        
        return new Location(world, 
            targetX + 0.5, 
            hitLocation.getBlockY(), 
            targetZ + 0.5, 
            hitLocation.getYaw(), 
            hitLocation.getPitch());
    }

    /**
     * Get hit face from relative coordinates
     */
    private static BlockFace getHitFace(Location hit, Block block) {
        double x = hit.getX() - block.getX();
        double y = hit.getY() - block.getY();
        double z = hit.getZ() - block.getZ();
        
        if (y > 0.85) return BlockFace.UP;
        if (y < 0.15) return BlockFace.DOWN;
        if (z < 0.3) return BlockFace.NORTH;
        if (z > 0.7) return BlockFace.SOUTH;
        if (x < 0.3) return BlockFace.WEST;
        if (x > 0.7) return BlockFace.EAST;
        
        return BlockFace.NORTH; // Center hit
    }
}
