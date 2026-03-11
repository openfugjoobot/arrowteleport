package com.openfugjoobot.arrowteleport.util;

/**
 * Permission constants for ArrowTeleport
 */
public final class PermissionUtil {

    private PermissionUtil() {} // Utility class

    // User permissions
    public static final String USE = "arrowteleport.use";
    public static final String RESET = "arrowteleport.reset";
    public static final String START = "arrowteleport.start";
    public static final String KIT = "arrowteleport.kit";
    public static final String STATS = "arrowteleport.stats";
    
    // Admin permissions
    public static final String ADMIN = "arrowteleport.admin";
    public static final String RELOAD = "arrowteleport.reload";

    // All permissions
    public static final String[] ALL_PERMISSIONS = {
        USE, RESET, START, KIT, STATS, ADMIN, RELOAD
    };
}
