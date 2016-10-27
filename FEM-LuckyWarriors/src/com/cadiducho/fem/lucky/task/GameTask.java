package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import java.util.Random;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final LuckyWarriors plugin;

    public GameTask(LuckyWarriors instance) {
        plugin = instance;
    }
    
    Random r = new Random();

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&l" + plugin.getAm().gameTime);
        });
        if (plugin.getAm().gameTime == plugin.getConfig().getInt("gameTime")) {
            plugin.getGm().getPlayersInGame().forEach(p -> {
                plugin.getAm().teleportArena(p);
                new Title("&b&l¡A las armas!", "", 1, 2, 1).send(p);
            });
            plugin.getMsg().sendBroadcast("&7¡Que de comienzo la batalla!");
        } else if (plugin.getAm().gameTime == 0) {
            plugin.getGm().dm = true;
            new DeathMatchTask(plugin).runTaskTimer(plugin, 20l, 20l);
            GameState.state = GameState.DEATHMATCH;
            cancel();
        }        
        -- plugin.getAm().gameTime;
    }
}
