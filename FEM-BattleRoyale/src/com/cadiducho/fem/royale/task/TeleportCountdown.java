package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.royale.BattlePlayer;
import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportCountdown extends BukkitRunnable {

    private final BattleRoyale plugin;

    public TeleportCountdown(BattleRoyale instance) {
        plugin = instance;
    }

    private int count = 6;

    @Override
    public void run() {
        if (count == 6) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                plugin.getAm().teleport(p);
                Title.sendTitle(p, 1, 4, 1, "&b&l¡Ármate para la batalla!", "Recuerda que puedes comerciar con aldeanos");
            });
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                plugin.getAm().fixPlayer(p.getLocation());
                
                final BattlePlayer bp = BattleRoyale.getPlayer(p);
                bp.loadKit();
                bp.getUserData().addPlay(GameID.BATTLEROYALE);
                bp.save();
            });
            GameState.state = GameState.PVE;
            new GameCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }
        --count;
    }
}