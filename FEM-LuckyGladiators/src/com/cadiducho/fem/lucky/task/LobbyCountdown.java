package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.lucky.LuckyGladiators;
import com.cadiducho.fem.lucky.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyCountdown extends BukkitRunnable {

    private final LuckyGladiators plugin;

    public LobbyCountdown(LuckyGladiators instance) {
        plugin = instance;
    }

    private int count = 30;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach((players) -> {
            players.setLevel(count);
        });
        if (count == 12) {
            plugin.getServer().getOnlinePlayers().stream().forEach((players) -> {
                players.hidePlayer(players);
            });
        } else if (count == 11) {
            plugin.getServer().getOnlinePlayers().stream().forEach((players) -> {
                players.showPlayer(players);
            });
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            GameState.state = GameState.LUCKY;
            new TeleportCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }
        --count;
    }

}
