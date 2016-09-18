package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.lucky.LuckyGladiators;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportCountdown extends BukkitRunnable {

    private final LuckyGladiators plugin;

    public TeleportCountdown(LuckyGladiators instance) {
        plugin = instance;
    }

    private int count = 5;

    @Override
    public void run() {
        switch (count) {
            case 5:
                plugin.getGm().getPlayersInGame().forEach(p -> plugin.getAm().teleportLucky(p));
                break;
            case 0:
                plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                    plugin.getAm().teleportLucky(p);
                    plugin.getAm().fixPlayer(p.getLocation());
                    plugin.getPm().setCleanPlayer(p, GameMode.SURVIVAL);
                });
                new BreakLuckyCountdown(plugin).runTaskTimer(plugin, 1l, 20l);
                cancel();
                break;
        }
        --count;
    }
}