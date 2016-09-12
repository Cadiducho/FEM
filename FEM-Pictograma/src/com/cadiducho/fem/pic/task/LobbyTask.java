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

    private int count = 15;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().forEach(pl ->  pl.setLevel(count));
        if (count == 10) {
            plugin.getMsg().sendBroadcast("10 segundos para comenzar");
            plugin.getGm().getPlayersInGame().forEach((players) -> {
                players.playSound(players.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            });
        } else if (count > 0 && count <= 5) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                new Title("&c&l" + count, "", 0, 1, 00).send(p);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            });
        } else if(count == 0){
            
            new CountdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
            GameState.state = GameState.COUNTDOWN;
            this.cancel();
        }
        --count;
    }

}
