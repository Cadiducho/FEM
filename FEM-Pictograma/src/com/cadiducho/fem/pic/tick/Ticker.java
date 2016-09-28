package com.cadiducho.fem.pic.tick;

import org.bukkit.plugin.java.JavaPlugin;

public class Ticker implements Runnable {

    private final JavaPlugin plugin;

    public Ticker(JavaPlugin instance) {
        plugin = instance;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 1L);
    }

    @Override
    public void run() {
        TickType[] arrayOfTickType;
        int j = (arrayOfTickType = TickType.values()).length;
        for (int i = 0; i < j; i++) {
            TickType updateType = arrayOfTickType[i];
            //for (Game game : GameManager.getInstance().getGames()) {*/
                if (updateType.Elapsed()) {
                    this.plugin.getServer().getPluginManager().callEvent(new EventTick(updateType));
                }
            //}
        }
    }
}
