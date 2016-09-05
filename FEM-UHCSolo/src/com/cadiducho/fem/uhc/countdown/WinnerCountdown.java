package com.cadiducho.fem.uhc.countdown;

import com.cadiducho.fem.uhc.Main;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;

public class WinnerCountdown extends BukkitRunnable {

    public Main plugin;

    public WinnerCountdown(Main plugin) {
        this.plugin = plugin;
    }

    private int totalTime = 10;

    @Override
    public void run() {
        plugin.gm.PLAYERS_IN_GAME.stream().map(id -> plugin.getServer().getPlayer(id)).forEach((winner) -> {
            plugin.up.sendFirework(winner);
        });
        if (totalTime == 10) {
            plugin.gm.DESCONECTADOS.clear();
            plugin.gm.SPECTATORS.clear();
        } else if (totalTime == 3) {
            plugin.getServer().getOnlinePlayers().stream().forEach((players) -> {
                plugin.up.sendToServer(players, "LOBBYUHC001");
            });
        }
        if (totalTime == 1) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "restart");
        }
        if (totalTime == 0) {
            plugin.gm.PLAYERS_IN_GAME.clear();
            cancel();
        }
        --totalTime;
    }
}
