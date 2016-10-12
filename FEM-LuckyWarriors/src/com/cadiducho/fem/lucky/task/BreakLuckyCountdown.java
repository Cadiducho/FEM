package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import java.util.Random;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class BreakLuckyCountdown extends BukkitRunnable {

    private final LuckyWarriors plugin;

    public BreakLuckyCountdown(LuckyWarriors instance) {
        plugin = instance;
    }
    
    Random r = new Random();

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&l" + plugin.getAm().luckyTime);
        });
        
        if (plugin.getAm().luckyTime == plugin.getConfig().getInt("luckyTime")) {
            plugin.getMsg().sendBroadcast("&7Ya puedes romper Lucky Blocks!");
        } else if (plugin.getAm().luckyTime == 5) {
            plugin.getMsg().sendBroadcast("&7Solo te quedan 5 segundos!");
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (plugin.getAm().luckyTime > 0 && plugin.getAm().luckyTime <= 4) {
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (plugin.getAm().luckyTime == 0) {
            plugin.getGm().dm = true;
            new CraftCountdown(plugin).runTaskTimer(plugin, 1l, 20l);
            GameState.state = GameState.CRAFT;
            cancel();
        }        
        -- plugin.getAm().luckyTime;
    }
}
