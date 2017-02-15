package com.cadiducho.fem.teamtnt.task;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.teamtnt.TeamTntWars;
import com.cadiducho.fem.teamtnt.TntPlayer;
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

        TeamTntWars.getInstance().getMsg().sendActionBar(player.getPlayer(), "&a&lReaparecerás en: " + count);

        switch (count){
            case 9:
                player.setRespawning(true);
                Title.sendTitle(player.getPlayer(), 1, 3, 1, "&b&l¡Has muerto!", "Reaparecerás al estar tu isla intacta");
                player.getUserData().addDeath(GameID.TEAMTNT);
                player.save();
                player.sendMessage("Reaparecerás en 9 segundos");
                break;
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 1F, 1F);
                break;
            case 0:
                player.spawn();
                player.setCleanPlayer(GameMode.SURVIVAL);
                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3*20, 5));
                player.setRespawning(false);
                cancel();
                break;
        }
        count--;
    }
    
}
