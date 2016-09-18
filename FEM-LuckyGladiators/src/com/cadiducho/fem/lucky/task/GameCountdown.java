package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.lucky.LuckyGladiators;
import com.cadiducho.fem.lucky.manager.GameState;
import java.util.Random;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountdown extends BukkitRunnable {

    private final LuckyGladiators plugin;

    public GameCountdown(LuckyGladiators instance) {
        plugin = instance;
    }
    
    Random r = new Random();

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&l" + plugin.getAm().gameTime);
        });
        if (plugin.getAm().gameTime == plugin.getConfig().getInt("gameTime")) {
            plugin.getGm().getPlayersInGame().forEach(p -> plugin.getAm().teleportArena(p));
            plugin.getMsg().sendBroadcast("&7¡Que de comienzo la batalla de gladiadores!");
        } else if (plugin.getAm().gameTime == 0) {
            plugin.getGm().dm = true;
            new DeathMatchCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            GameState.state = GameState.DEATHMATCH;
            cancel();
        }        
        -- plugin.getAm().gameTime;
    }
}
