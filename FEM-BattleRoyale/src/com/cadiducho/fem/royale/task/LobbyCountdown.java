package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyCountdown extends BukkitRunnable {

    private final BattleRoyale plugin;

    public LobbyCountdown(BattleRoyale instance) {
        plugin = instance;
    }

    private int count = 12;

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
            //Sound level up
        } else if (count == 0) {
            GameState.state = GameState.COUNTDOWN;
            new TeleportCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }
        --count;
    }

}
