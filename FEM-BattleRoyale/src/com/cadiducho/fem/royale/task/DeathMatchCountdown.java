package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathMatchCountdown extends BukkitRunnable {

    private final BattleRoyale plugin;

    public DeathMatchCountdown(BattleRoyale instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        if (GameState.state == GameState.ENDING) {
            cancel();
        }
        plugin.getGm().getPlayersInGame().stream().forEach((players) -> {
            plugin.getMsg().sendActionBar(players, "&f&lDEATHMATCH: &a&l" + plugin.getAm().deathMatchTime);
        });
        if (plugin.getAm().deathMatchTime == plugin.getConfig().getInt("deathMatchTime")) {
            plugin.getGm().getPlayersInGame().stream().forEach((players) -> {
                plugin.getAm().teleport(players);
            });
        } else if (plugin.getAm().deathMatchTime == 0) {
            plugin.getGm().getPlayersInGame().stream().forEach((players) -> {
                players.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 1));
            });
        }
        --plugin.getAm().deathMatchTime;
    }
}