package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyCountdown extends BukkitRunnable {

    private final BattleRoyale plugin;

    public LobbyCountdown(BattleRoyale instance) {
        plugin = instance;
    }

    private int count = 30;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach((players) -> {
            players.setLevel(count);
        });
        switch (count) {
            case 30:
                plugin.getServer().getOnlinePlayers().forEach(pl -> pl.hidePlayer(pl));
                break;
            case 29:
                plugin.getServer().getOnlinePlayers().forEach(pl -> pl.showPlayer(pl));
                break;
            case 5:
                plugin.getMsg().sendBroadcast("&7Serás llevado al mundo en 5 segundos");
                break;
            case 0:
                GameState.state = GameState.COUNTDOWN;
                new TeleportCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
                cancel();
                break;
            default:
                break;
        }
        --count;
    }

}
