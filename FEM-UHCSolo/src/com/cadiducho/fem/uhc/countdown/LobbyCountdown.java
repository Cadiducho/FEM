package com.cadiducho.fem.uhc.countdown;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyCountdown extends BukkitRunnable {

    private final Main plugin;

    public LobbyCountdown(Main plugin) {
        this.plugin = plugin;
    }

    private int totalTime = 30;

    @Override
    public void run() {
        plugin.gm.PLAYERS_IN_GAME.stream()
                .map(id -> plugin.getServer().getPlayer(id))
                .forEach(pl -> pl.setLevel(totalTime));
        if (totalTime == 30) {
            plugin.gm.PLAYERS_IN_GAME.stream().map((jugadores) -> {
                plugin.msg.sendTitle(plugin.getServer().getPlayer(jugadores), "&c&l" + totalTime, "&7segundos para iniciar el juego", 0, 10, 0);
                return jugadores;
            }).forEach((jugadores) -> {
                plugin.getServer().getPlayer(jugadores).playSound(plugin.getServer().getPlayer(jugadores).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
            });
        } else if (totalTime == 25) {
            plugin.gm.PLAYERS_IN_GAME.stream().map((jugadores) -> {
                plugin.msg.sendTitle(plugin.getServer().getPlayer(jugadores), "&e⚠", "&c&lTEAMS NO PERMITIDOS", 0, 10, 0);
                return jugadores;
            }).forEach((jugadores) -> {
                plugin.getServer().getPlayer(jugadores).playSound(plugin.getServer().getPlayer(jugadores).getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
            });
        } else if (totalTime == 20) {
            plugin.gm.PLAYERS_IN_GAME.stream()
                    .map(id -> plugin.getServer().getPlayer(id))
                    .forEach(pl -> pl.playSound(pl.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1F, 1F));

        } else if (totalTime > 0 && totalTime <= 5) {
            plugin.gm.PLAYERS_IN_GAME.stream().map((jugadores) -> {
                String s = (totalTime == 1 ? "" : "s");
                plugin.msg.sendTitle(plugin.getServer().getPlayer(jugadores), "&c&l" + totalTime, "&7segundo" + s + " para iniciar el juego", 0, 10, 0);
                return jugadores;
            }).forEach((jugadores) -> {
                plugin.getServer().getPlayer(jugadores).playSound(plugin.getServer().getPlayer(jugadores).getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                plugin.getServer().getPlayer(jugadores).getInventory().clear();
                plugin.getServer().getPlayer(jugadores).closeInventory();
            });
        } else if (totalTime == 0) {
            for (UUID jugadores : plugin.gm.PLAYERS_IN_GAME) {
                plugin.grm.clear(plugin.getServer().getPlayer(jugadores).getLocation());
                plugin.msg.sendTitle(plugin.getServer().getPlayer(jugadores), "&a&lTeletransporte", "&7iniciando teletrasporte", 5, 8, 5);
            }
            plugin.msg.sendBroadcast("&7Iniciando teletransporte");
            GameState.state = GameState.TELETRANSPORTE;
            new TeletransporteCountdown(plugin).runTaskTimer(plugin, 0, 20);
            this.cancel();
        }
        --totalTime;
    }

}
