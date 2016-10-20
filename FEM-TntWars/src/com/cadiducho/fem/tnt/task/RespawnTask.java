package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import java.util.HashMap;
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
        if (player.getBase().getPlayer() == null) cancel();
        
        TntWars.getInstance().getMsg().sendActionBar(player.getBase().getPlayer(), "&a&lReaparecerás en: " + count);
        if (count == 9) {
            new Title("&b&l¡Has muerto!", "Reaparecerás al estar tu isla intacta", 1, 2, 1).send(player.getBase().getPlayer()); 
            HashMap<Integer, Integer> deaths = player.getBase().getUserData().getDeaths();
            deaths.replace(1, deaths.get(1) + 1);
            player.getBase().getUserData().setDeaths(deaths);
            player.getBase().save();
            player.getBase().sendMessage("Reaparecerás en 9 segundos");
        } else if (count > 0 && count <= 5) {
            player.getBase().getPlayer().playSound(player.getBase().getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F);
        } else if (count == 0) {
            player.spawn();
            player.setCleanPlayer(GameMode.SURVIVAL);
            player.getBase().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3*20, 5));
            cancel();
        }
        
        count--;
    }
    
}
