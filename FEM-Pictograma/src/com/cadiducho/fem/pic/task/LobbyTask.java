package com.cadiducho.fem.pic.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTask extends BukkitRunnable {

    private final Pictograma plugin;

    public LobbyTask(Pictograma instance) {
        plugin = instance;
    }

    private int count = 30;

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
        
        plugin.getGm().getPlayersInGame().forEach(pl ->  pl.setLevel(count));
        if (count == 10) {
            plugin.getMsg().sendBroadcast("10 segundos para comenzar");
            plugin.getGm().getPlayersInGame().forEach((players) -> {
                players.playSound(players.getLocation(), Sound.CLICK, 1f, 1f);
            });
        } else if (count > 0 && count <= 5) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                Title.sendTitle(p, 0, 5, 0, "&c&l" + count, "");
                p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
            });
        } else if(count == 0){
            plugin.getGm().getPlayersInGame().stream().forEach(p -> p.getInventory().clear());
            new CountdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
            GameState.state = GameState.COUNTDOWN;
            this.cancel();
        }
        --count;
    }

}
