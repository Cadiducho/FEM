package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.lucky.LuckyGladiators;
import com.cadiducho.fem.lucky.manager.GameState;
import java.util.Random;
import org.bukkit.scheduler.BukkitRunnable;

public class CraftCountdown extends BukkitRunnable {

    private final LuckyGladiators plugin;

    public CraftCountdown(LuckyGladiators instance) {
        plugin = instance;
    }
    
    Random r = new Random();

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&l" + plugin.getAm().craftTime);
        });
        if (plugin.getAm().craftTime == plugin.getConfig().getInt("craftTime")) {
            plugin.getGm().getPlayersInGame().forEach(p -> plugin.getAm().teleportDungeon(p));
            plugin.getMsg().sendBroadcast("&7Tienes " + plugin.getAm().craftTime + " segundos para mejorar tu equipo!");
        } else if (plugin.getAm().craftTime == 0) {
            plugin.getGm().dm = true;
            new GameCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            GameState.state = GameState.GAME;
            cancel();
        }        
        -- plugin.getAm().craftTime;
    }
}
