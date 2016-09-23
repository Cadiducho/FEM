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
        plugin.getGm().getPlayersInGame().stream()
                .filter(p -> plugin.getAm().deathMatchTime > 0)
                .forEach(p -> plugin.getMsg().sendActionBar(p, "&a&l" + plugin.getAm().deathMatchTime));
        plugin.getGm().getPlayersInGame().forEach(p -> {
            plugin.getMsg().sendActionBar(p, "&f&lDEATHMATCH: &a&l" + plugin.getAm().deathMatchTime);
        });
        if (plugin.getAm().deathMatchTime == plugin.getConfig().getInt("deathMatchTime")) {
            plugin.getGm().getPlayersInGame().forEach(p -> plugin.getAm().teleportDeathmatch(p));
        } else if (plugin.getAm().deathMatchTime == 0) {
            plugin.getGm().getPlayersInGame().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 1)));
        }
        --plugin.getAm().deathMatchTime;
    }
}