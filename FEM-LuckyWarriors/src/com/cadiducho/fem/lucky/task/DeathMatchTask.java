package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathMatchTask extends BukkitRunnable {

    private final LuckyWarriors plugin;

    public DeathMatchTask(LuckyWarriors instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        if (GameState.state == GameState.ENDING) {
            cancel();
        }
        if(plugin.getAm().deathMatchTime == plugin.getConfig().getInt("deathMatchTime")){
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
            });
        }

        if (plugin.getAm().deathMatchTime > 0) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                plugin.getMsg().sendActionBar(p, "&f&lDEATHMATCH: &a&l" + plugin.getAm().deathMatchTime);
            });
        }
        
        if (plugin.getAm().deathMatchTime == 6) {
            plugin.getMsg().sendBroadcast("&7En 5 segundos sereís envenenados");
        } else if (plugin.getAm().deathMatchTime > 1 && plugin.getAm().deathMatchTime <= 5) {
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));  
        } else if (plugin.getAm().deathMatchTime == 0) {
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 1));
                new Title("&b&lHas sido envenenado", "", 1, 2, 1).send(p);
            });
        }
        --plugin.getAm().deathMatchTime;
    }
}