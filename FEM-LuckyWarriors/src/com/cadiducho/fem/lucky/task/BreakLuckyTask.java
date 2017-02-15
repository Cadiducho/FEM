package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class BreakLuckyTask extends BukkitRunnable {

    private final LuckyWarriors plugin;

    public BreakLuckyTask(LuckyWarriors instance) {
        plugin = instance;
    }
    
    Random r = new Random();

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&l" + plugin.getAm().luckyTime);
        });

        if (plugin.getAm().luckyTime == plugin.getConfig().getInt("luckyTime")) { //No es constante :c
            GameState.state = GameState.LUCKY;
            plugin.getGm().getPlayersInGame().forEach(p -> {
                Title.sendTitle(p, 1, 7, 1, "", "&b&l¡Rompe los LuckyBlocks que puedas!");
            });
            plugin.getMsg().sendBroadcast("&7Ya puedes romper Lucky Blocks!");
        }

        switch (plugin.getAm().luckyTime){
            case 5:
                plugin.getMsg().sendBroadcast("&7Solo te quedan 5 segundos!");
                plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.NOTE_PLING, 1F, 1F));
                break;
            case 4:
            case 3:
            case 2:
            case 1:
                plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.NOTE_PLING, 1F, 1F));
                break;
            case 0:
                plugin.getGm().dm = true;
                new CraftTask(plugin).runTaskTimer(plugin, 1l, 20l);
                GameState.state = GameState.CRAFT;
                cancel();
                break;
        }
        -- plugin.getAm().luckyTime;
    }
}
