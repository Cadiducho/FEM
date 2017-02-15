package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class CraftTask extends BukkitRunnable {

    private final LuckyWarriors plugin;

    public CraftTask(LuckyWarriors instance) {
        plugin = instance;
    }
    
    Random r = new Random();

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&l" + plugin.getAm().craftTime);
        });
        if (plugin.getAm().craftTime == plugin.getConfig().getInt("craftTime")) { //No es constante :c
            plugin.getGm().getPlayersInGame().forEach(p -> {
                plugin.getAm().teleportDungeon(p);
                Title.sendTitle(p, 1,2, 1, "&b&l¡Mejora tu equipo!", "");
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
            });
            plugin.getMsg().sendBroadcast("&7Tienes " + plugin.getAm().craftTime + " segundos para mejorar tu equipo!");
        }

        switch (plugin.getAm().luckyTime){
            case 4:
            case 3:
            case 2:
            case 1:
                plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.NOTE_PLING, 1F, 1F));
                break;
            case 0:
                plugin.getGm().dm = true;
                new GameTask(plugin).runTaskTimer(plugin, 20l, 20l);
                GameState.state = GameState.GAME;
                cancel();
        }
        -- plugin.getAm().craftTime;
    }
}
