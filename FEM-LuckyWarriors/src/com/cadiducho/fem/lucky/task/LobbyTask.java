package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTask extends BukkitRunnable {

    private final LuckyWarriors plugin;

    public LobbyTask(LuckyWarriors instance) {
        plugin = instance;
    }

    private int count = 35;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach((players) -> {
            players.setLevel(count);
        });
        if (count == 35) {
            plugin.getServer().getOnlinePlayers().forEach(pl -> pl.hidePlayer(pl));
        } else if (count == 34) {
            plugin.getServer().getOnlinePlayers().forEach(pl -> pl.showPlayer(pl));
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            plugin.getGm().getPlayersInGame().forEach(p -> {
                plugin.getAm().teleportLucky(p);
                plugin.getAm().fixPlayer(p.getLocation());
                plugin.getPm().setCleanPlayer(p, GameMode.SURVIVAL);
                HashMap<Integer, Integer> plays = FEMServer.getUser(p).getUserData().getPlays();
                plays.replace(6, plays.get(6) + 1);
                FEMServer.getUser(p).getUserData().setPlays(plays);
                FEMServer.getUser(p).save();
            });
            new BreakLuckyTask(plugin).runTaskTimer(plugin, 1l, 20l);
            cancel();
        }
        --count;
    }

}
