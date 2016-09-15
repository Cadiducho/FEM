package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportCountdown extends BukkitRunnable {

    private final BattleRoyale plugin;

    public TeleportCountdown(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    private int count = 5;

    @Override
    public void run() {
        if (count == 5) {
            plugin.getGm().getPlayersInGame().stream().forEach((player) -> {
                plugin.getAm().teleport(player);
            });
            plugin.getAm().fillChests();
        } else if (count == 0) {
            plugin.getGm().getPlayersInGame().stream().forEach((Player player) -> {
                plugin.getAm().fixPlayer(player.getLocation());
                plugin.getPm().loadKit(player);
            });
            new GameCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }
        --count;
    }
}