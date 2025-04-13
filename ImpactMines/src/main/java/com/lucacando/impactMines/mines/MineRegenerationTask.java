package com.lucacando.impactMines.mines;

import com.lucacando.impactMines.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class MineRegenerationTask extends BukkitRunnable {
    private final Main main;

    public MineRegenerationTask(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Mine mine : main.activeMines) {
            mine.regenerate();
        }
    }
}