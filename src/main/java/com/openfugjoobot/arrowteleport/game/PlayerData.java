package com.openfugjoobot.arrowteleport.game;

import org.bukkit.Location;

import java.util.UUID;

/**
 * Stores per-player game statistics and session data
 */
public class PlayerData {

    private final UUID playerId;
    private String playerName;
    
    // Persistent stats
    private double totalDistance;
    private int arrowsShot;
    private long totalTimeSeconds;
    private int sessionsCompleted;
    private long bestTime;
    
    // Session data (non-persistent)
    private transient boolean inGame;
    private transient long sessionStartTime;
    private transient long lastTeleportTime;
    private transient Location lastTeleportLocation;
    private transient int sessionArrowsShot;
    private transient double sessionDistance;

    public PlayerData(UUID playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.totalDistance = 0;
        this.arrowsShot = 0;
        this.totalTimeSeconds = 0;
        this.sessionsCompleted = 0;
        this.bestTime = Long.MAX_VALUE;
        
        resetSession();
    }

    public void resetSession() {
        this.inGame = false;
        this.sessionStartTime = 0;
        this.lastTeleportTime = 0;
        this.lastTeleportLocation = null;
        this.sessionArrowsShot = 0;
        this.sessionDistance = 0;
    }

    public void startSession() {
        this.inGame = true;
        this.sessionStartTime = System.currentTimeMillis();
        this.sessionArrowsShot = 0;
        this.sessionDistance = 0;
    }

    public void endSession() {
        if (!inGame) return;
        
        long sessionTime = getSessionSeconds();
        this.totalTimeSeconds += sessionTime;
        this.arrowsShot += sessionArrowsShot;
        this.totalDistance += sessionDistance;
        this.sessionsCompleted++;
        
        if (sessionTime < bestTime) {
            bestTime = sessionTime;
        }
        
        resetSession();
    }

    public void recordArrowShot() {
        arrowsShot++;
        sessionArrowsShot++;
    }

    public void recordTeleport(Location from, Location to) {
        if (from != null && to != null && from.getWorld().equals(to.getWorld())) {
            double dist = from.distance(to);
            sessionDistance += dist;
        }
        lastTeleportLocation = to;
        lastTeleportTime = System.currentTimeMillis();
    }

    public boolean isOnCooldown(int cooldownSeconds) {
        if (cooldownSeconds <= 0) return false;
        long elapsed = (System.currentTimeMillis() - lastTeleportTime) / 1000;
        return elapsed < cooldownSeconds;
    }

    public int getRemainingCooldown(int cooldownSeconds) {
        if (cooldownSeconds <= 0) return 0;
        long elapsed = (System.currentTimeMillis() - lastTeleportTime) / 1000;
        return Math.max(0, cooldownSeconds - (int) elapsed);
    }

    // Getters
    public UUID getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String name) { this.playerName = name; }
    
    public double getTotalDistance() { return totalDistance; }
    public int getArrowsShot() { return arrowsShot; }
    public long getTotalTimeSeconds() { return totalTimeSeconds; }
    public int getSessionsCompleted() { return sessionsCompleted; }
    public long getBestTime() { return bestTime == Long.MAX_VALUE ? 0 : bestTime; }

    // Setters for persistence
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
    public void setArrowsShot(int arrowsShot) { this.arrowsShot = arrowsShot; }
    public void setTotalTimeSeconds(long totalTimeSeconds) { this.totalTimeSeconds = totalTimeSeconds; }
    public void setSessionsCompleted(int sessionsCompleted) { this.sessionsCompleted = sessionsCompleted; }
    public void setBestTime(long bestTime) { this.bestTime = bestTime; }

    // Session getters
    public boolean isInGame() { return inGame; }
    public int getSessionSeconds() {
        if (!inGame) return 0;
        return (int) ((System.currentTimeMillis() - sessionStartTime) / 1000);
    }
    public int getSessionArrowsShot() { return sessionArrowsShot; }
    public double getSessionDistance() { return sessionDistance; }
}
