package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.royale.BattlePlayer;
import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import java.util.HashMap;
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
                new Title("&b&l¡Ármate para la batalla!", "Recuerda que puedes comerciar con aldeanos", 1, 3, 1).send(p);
            });
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                plugin.getAm().fixPlayer(p.getLocation());
                
                final BattlePlayer bp = BattleRoyale.getPlayer(p);
                bp.loadKit();
                HashMap<Integer, Integer> plays = bp.getUserData().getPlays();
                plays.replace(5, plays.get(5) + 1);
                bp.getUserData().setPlays(plays);
                bp.save();
            });
            GameState.state = GameState.PVE;
            new GameCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }
        --count;
    }
}