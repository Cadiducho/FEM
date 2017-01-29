package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final LuckyWarriors plugin;

    public GameTask(LuckyWarriors instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&l" + plugin.getAm().gameTime);
        });
        if (plugin.getAm().gameTime == plugin.getConfig().getInt("gameTime")) {
            plugin.getGm().getPlayersInGame().forEach(p -> {
                plugin.getAm().teleportArena(p);
                Title.sendTitle(p, 1, 7, 1, "&b&l¡A las armas!", "");
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
            });
            plugin.getMsg().sendBroadcast("&7¡Que de comienzo la batalla!");
        } else if (plugin.getAm().gameTime == 0) {
            plugin.getGm().dm = true;
            new DeathMatchTask(plugin).runTaskTimer(plugin, 20l, 20l);
            GameState.state = GameState.DEATHMATCH;
            cancel();
        }        
        -- plugin.getAm().gameTime;
        noPlayers();
    }


    private void noPlayers(){
        if (plugin.getGm().getPlayersInGame().isEmpty()){
            plugin.getServer().shutdown();
        }
    }
}
