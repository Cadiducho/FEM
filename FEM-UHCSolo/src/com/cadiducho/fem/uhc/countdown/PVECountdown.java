package com.cadiducho.fem.uhc.countdown;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PVECountdown extends BukkitRunnable {

    public int totalTime = 1200;
    private final Main plugin;

    public PVECountdown(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.gm.PLAYERS_IN_GAME.stream()
                .map(id -> plugin.getServer().getPlayer(id))
                .forEach(pl -> plugin.msg.sendActionBar(pl, "&c&lDonkey&b&lUHC &8| &fPVE: &e" + plugin.t.tiempoPVE(totalTime)));
        if (totalTime % 300 == 0 && totalTime > 0) {
            plugin.gm.PLAYERS_IN_GAME.stream().forEach(id -> {
                plugin.msg.sendTitle(plugin.getServer().getPlayer(id), "", "&c&l" + (totalTime / 60) + " minutos" + " para el PVP", 0, 10, 0);
            });
            plugin.msg.sendBroadcast("&c&l" + (totalTime / 60) + " &7minutos para el PVP.");
        } else if (totalTime > 0 && totalTime <= 5) {
            plugin.gm.PLAYERS_IN_GAME.stream().forEach(id -> {
                String s = (totalTime == 1 ? "" : "s");
                plugin.msg.sendTitle(plugin.getServer().getPlayer(id), "", "&c&l" + totalTime + " segundo" + s + " para el PVP", 0, 10, 0);
            });
            plugin.msg.sendBroadcast("&c&l" + totalTime + " &7segundo(s) para el PVP.");
        } else if (totalTime == 0) {
            plugin.gm.PLAYERS_IN_GAME.stream()
                    .map(id -> plugin.getServer().getPlayer(id))
                    .forEach((pl) -> {
                        plugin.msg.sendTitle(pl, "", "&c&lPVP Activado", 0, 10, 0);
                        pl.playSound(pl.getLocation(), Sound.ENTITY_WOLF_HOWL, 1F, 1F);
                    });
            GameState.state = GameState.JUEGO;
            plugin.msg.sendBroadcast("&c&lPVP ACTIVADO");
            new UHCGameCountdown(plugin).runTaskTimer(plugin, 0, 20);
            plugin.gm.DESCONECTADOS.clear();
            this.cancel();
        }
        --totalTime;
    }

}
