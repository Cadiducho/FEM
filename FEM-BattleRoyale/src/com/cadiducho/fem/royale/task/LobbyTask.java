package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTask extends BukkitRunnable {

    private final BattleRoyale plugin;

    public LobbyTask(BattleRoyale instance) {
        plugin = instance;
    }

    private int count = 30;

    @Override
    public void run() {
        //Comprobar si sigue habiendo suficientes jugadores o cancelar
        if (plugin.getGm().getPlayersInGame().size() < plugin.getAm().getMinPlayers()) {
            plugin.getGm().setCheckStart(true);
            plugin.getServer().getOnlinePlayers().forEach(pl ->  pl.setLevel(0));
            GameState.state = GameState.LOBBY;
            cancel();
            return;
        }
        
        plugin.getGm().getPlayersInGame().stream().forEach((players) -> {
            players.setLevel(count);
        });
        switch (count) {
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
