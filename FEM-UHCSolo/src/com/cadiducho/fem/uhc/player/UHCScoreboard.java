package com.cadiducho.fem.uhc.player;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UHCScoreboard {

    public Main plugin;

    public UHCScoreboard(Main plugin) {
        this.plugin = plugin;
    }

    public void setScoreboard(Player player) {
        com.cadiducho.fem.uhc.utils.ScoreboardUtil board = new com.cadiducho.fem.uhc.utils.ScoreboardUtil("     §c§lDonkey§b§lUHC     ");
        double Y = player.getLocation().getY();
        board.text(9, "§7UHC Solo | 1 Hora");
        board.text(8, "§7----- Juego -----");
        board.text(2, "§7----- Servidor -----");
        board.text(1, "§fServer: §e" + plugin.getConfig().getString("SERVER"));
        board.text(0, "§emc.donkeycraft.net");
        new BukkitRunnable() {
            @Override
            public void run() {
                double Y = player.getLocation().getY();
                String YSTRING = String.format("%.0f", Y);
                int jugadores = plugin.gm.getPlayers().size();
                board.text(7, "§fJugadores: §e" + jugadores + "§d/§e40");
                board.text(6, "§fEstado: §e" + getEstado());
                if (GameState.state == GameState.PVE || GameState.state == GameState.JUEGO || GameState.state == GameState.ELIMINACION) {
                    board.text(5, "§fBorde: §e" + plugin.wm.getArenaWorld().getWorldBorder().getSize() / 2 + "§d/§f-§e" + plugin.wm.getArenaWorld().getWorldBorder().getSize() / 2);
                    board.text(4, "§fCentro:§e" + plugin.t.getCentro(player));
                    board.text(3, "§fY: §e" + YSTRING);
                }
            }
        }.runTaskTimer(plugin, 0l, 20l);
        board.build(player);
    }

    public String getEstado() {
        switch (GameState.state) {
            case LOBBY:
                return "Esperando";
            case TELETRANSPORTE:
                return "Teletransporte";
            case PVE:
                return "PVE";
            case JUEGO:
                return "En Juego";
            case ELIMINACION:
                return "Eliminación";
            case FIN:
                return "Fin";
            default:
                return "NULL";
        }
    }

}
