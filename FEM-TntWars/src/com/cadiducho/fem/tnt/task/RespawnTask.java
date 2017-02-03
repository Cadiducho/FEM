package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnTask extends BukkitRunnable {
    
    private final TntPlayer player;
    
    public RespawnTask(TntPlayer instance) {
        player = instance;
    }
    
    private int count = 9;
    
    @Override
    public void run() {
        if (player.getPlayer() == null) cancel();
        
        TntWars.getInstance().getMsg().sendActionBar(player.getPlayer(), "&a&lReaparecerás en: " + count);
        if (count == 9) {
            Title.sendTitle(player.getPlayer(), 1, 7, 1, "&b&l¡Has muerto!", "Reaparecerás al estar tu isla intacta");
            player.getUserData().addDeath(GameID.TNTWARS);
            player.save();
            player.sendMessage("Reaparecerás en 9 segundos");
        } else if (count > 0 && count <= 5) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 1F, 1F);
        } else if (count == 0) {
            player.spawn();
            player.setCleanPlayer(GameMode.SURVIVAL);
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3*20, 5));
            player.setRespawning(false);
            cancel();
        }
        
        count--;
    }
    
}
