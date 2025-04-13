package com.lucacando.impactMines.mines;

import org.bukkit.Location;

public class Selection {

    private Location pos1;
    private Location pos2;

    public void setPos1(Location loc) {
        this.pos1 = loc;
    }

    public void setPos2(Location loc) {
        this.pos2 = loc;
    }

    public boolean isComplete() {
        return pos1 != null && pos2 != null;
    }

    public int getVolume() {
        if (!isComplete()) {
            return 0;
        }
        int xDiff = Math.abs(pos1.getBlockX() - pos2.getBlockX()) + 1;
        int yDiff = Math.abs(pos1.getBlockY() - pos2.getBlockY()) + 1;
        int zDiff = Math.abs(pos1.getBlockZ() - pos2.getBlockZ()) + 1;
        return xDiff * yDiff * zDiff;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }
}