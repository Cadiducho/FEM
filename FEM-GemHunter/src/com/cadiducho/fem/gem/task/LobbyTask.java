package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTask extends BukkitRunnable {

    private final GemHunters plugin;

    public LobbyTask(GemHunters instance) {
        plugin = instance;
    }

    private int count = 45;

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
        plugin.getGm().getPlayersInGame().stream().forEach(pl ->  pl.setLevel(count));
        if (count == 10) {
            plugin.getMsg().sendBroadcast("10 segundos para crear equipos");
            plugin.getGm().getPlayersInGame().stream().forEach((players) -> {
                players.playSound(players.getLocation(), Sound.CLICK, 1f, 1f);
            });
        } else if (count > 0 && count <= 5) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                Title.sendTitle(p, 1, 3, 1, "&c&l" + count, "");
                p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
            });
        } else if(count == 0) {
            new CountdownTask(plugin).runTaskTimer(plugin, 1l, 20l);
            GameState.state = GameState.COUNTDOWN;
            cancel();
        }
        --count;
    }

}
