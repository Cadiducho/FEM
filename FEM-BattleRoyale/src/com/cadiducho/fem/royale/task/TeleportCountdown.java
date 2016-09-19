package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.royale.BattleRoyale;
import java.util.HashMap;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportCountdown extends BukkitRunnable {

    private final BattleRoyale plugin;

    public TeleportCountdown(BattleRoyale instance) {
        plugin = instance;
    }

    private int count = 5;

    @Override
    public void run() {
        if (count == 5) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> plugin.getAm().teleport(p));
            plugin.getAm().fillChests();
        } else if (count == 0) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                plugin.getAm().fixPlayer(p.getLocation());
                plugin.getPm().loadKit(p);
                HashMap<Integer, Integer> plays = FEMServer.getUser(p).getUserData().getPlays();
                plays.replace(5, plays.get(5) + 1);
                FEMServer.getUser(p).getUserData().setPlays(plays);
                FEMServer.getUser(p).save();
            });
            new GameCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }
        --count;
    }
}