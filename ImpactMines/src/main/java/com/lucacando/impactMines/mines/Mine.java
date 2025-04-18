package com.lucacando.impactMines.mines;

import com.lucacando.impactMines.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.Random;

public class Mine {

    private final World world;
    private final Location min;
    private final Location max;
    private final Material block1;
    private final Material block2;
    private final int totalBlocks;
    private final Random random = new Random();

    public Mine(World world, Location pos1, Location pos2, Material block1, Material block2) {
        this.world = world;
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        this.min = new Location(world, minX, minY, minZ);
        this.max = new Location(world, maxX, maxY, maxZ);
        this.totalBlocks = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
        this.block1 = block1;
        this.block2 = block2;
    }

    public void fillInitial() {
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Location loc = new Location(world, x, y, z);
                    Material chosen = (random.nextDouble() <= 0.65) ? block1 : block2;
                    world.getBlockAt(loc).setType(chosen);
                }
            }
        }
    }

    public void regenerate() {
        int airCount = 0;
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block b = world.getBlockAt(x, y, z);
                    if (b.getType() == Material.AIR) {
                        Material fill = (random.nextDouble() <= 0.65) ? block1 : block2;
                        b.setType(fill);
                        airCount++;
                    }
                }
            }
        }
        // If more than 50% of the blocks are air, fully reset the mine.
        if (((double) airCount / totalBlocks) > 0.5) {
            resetMine();
        }
    }

    public void resetMine() {
        Main main = new Main();
        fillInitial();
        for (Player player : world.getPlayers()) {
            Location loc = player.getLocation();
            if (isInside(loc)) {
                player.sendMessage(main.prefix + "Mine reset! Teleporting you to safety.");
                player.teleport(getTopLocation());
            }
        }
    }

    public void removeFromWorld() {
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    world.getBlockAt(x, y, z).setType(org.bukkit.Material.AIR);
                }
            }
        }
    }

    // Checks whether a location is inside the mine region.
    public boolean isInside(Location loc) {
        if (!loc.getWorld().equals(world))
            return false;
        return loc.getBlockX() >= min.getBlockX() && loc.getBlockX() <= max.getBlockX() &&
                loc.getBlockY() >= min.getBlockY() && loc.getBlockY() <= max.getBlockY() &&
                loc.getBlockZ() >= min.getBlockZ() && loc.getBlockZ() <= max.getBlockZ();
    }

    // Returns a location at the top-center of the mine.
    public Location getTopLocation() {
        int centerX = (min.getBlockX() + max.getBlockX()) / 2;
        int centerZ = (min.getBlockZ() + max.getBlockZ()) / 2;
        return new Location(world, centerX, max.getBlockY() + 1, centerZ);
    }

    // Getters for persistence.
    public World getWorld() {
        return world;
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }

    public Material getBlock1() {
        return block1;
    }

    public Material getBlock2() {
        return block2;
    }
}