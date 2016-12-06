package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.lucky.LuckyPlayer;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class LobbyTask extends BukkitRunnable {

    private final LuckyWarriors plugin;

    public LobbyTask(LuckyWarriors instance) {
        plugin = instance;
    }

    private int count = 35;

    @Override
    public void run() {
        //Comprobar si sigue habiendo suficientes jugadores o cancelar
        if (plugin.getGm().getPlayersInGame().size() < plugin.getAm().getMinPlayers()) {
            plugin.getGm().setCheckStart(true);
            plugin.getServer().getOnlinePlayers().forEach(pl ->  pl.setLevel(0));
            GameState.state = GameState.LOBBY;
            cancel();
            return;
        }
        
        plugin.getGm().getPlayersInGame().stream().forEach((players) -> {
            players.setLevel(count);
        });
        if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            plugin.getGm().getPlayersInGame().forEach(p -> {
                plugin.getAm().teleportLucky(p);
                plugin.getAm().fixPlayer(p.getLocation());
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);

                final LuckyPlayer lp = LuckyWarriors.getPlayer(p);
                lp.setCleanPlayer(GameMode.SURVIVAL);
                HashMap<Integer, Integer> plays = lp.getUserData().getPlays();
                plays.replace(6, plays.get(6) + 1);
                lp.getUserData().setPlays(plays);
                lp.save();
            });
            new BreakLuckyTask(plugin).runTaskTimer(plugin, 1l, 20l);
            cancel();
        }
        --count;
    }

}
