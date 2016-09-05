package com.cadiducho.fem.uhc.countdown;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class TeletransporteCountdown extends BukkitRunnable {

    private int totalTime = 10;
    private final Main plugin;

    public TeletransporteCountdown(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (totalTime == 10) {
            plugin.gm.PLAYERS_IN_GAME.stream()
                    .map(id -> plugin.getServer().getPlayer(id))
                    .forEach(pl -> plugin.wm.enviarJugadores(pl));

        } else if (totalTime > 0 && totalTime <= 5) {
            plugin.gm.PLAYERS_IN_GAME.stream()
                    .map(id -> plugin.getServer().getPlayer(id))
                    .forEach(pl -> plugin.msg.sendTitle(pl, "&7Serás liberado en ", "&c&l" + totalTime, 0, 5, 0));
            plugin.msg.sendBroadcast("&7Serás liberado en " + totalTime);
        } else if (totalTime == 0) {
            plugin.gm.PLAYERS_IN_GAME.stream()
                    .map(id -> plugin.getServer().getPlayer(id))
                    .forEach(pl -> {
                        FEMUser jp = FEMServer.getUser(pl);
                        jp.addPlayed(1, 1);
                        plugin.grm.clear(pl.getLocation());
                        plugin.msg.sendTitle(pl, "&b&lBuena suerte", "", 0, 5, 0);
                        plugin.up.setCleanPlayer(pl, GameMode.SURVIVAL);
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 5));
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 5));
                        pl.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 5));
                        pl.setFoodLevel(20);
                    });
            GameState.state = GameState.PVE;
            plugin.msg.sendBroadcast("&b&lBuena suerte");
            new PVECountdown(plugin).runTaskTimer(plugin, 0, 20);
            this.cancel();
        }
        --totalTime;
    }

}
