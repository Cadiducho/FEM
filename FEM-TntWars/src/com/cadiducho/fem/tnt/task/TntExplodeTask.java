package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class TntExplodeTask extends BukkitRunnable {
    
    private final TntIsland isla;
    private final UUID exploder;
    
    public TntExplodeTask(TntIsland instance, UUID p) {
        isla = instance;
        exploder = p;
    }
    
    private int count = 10;
    
    @Override
    public void run() {
        final TntPlayer tpExploder = TntWars.getPlayer(TntWars.getInstance().getServer().getOfflinePlayer(exploder));
        final TntPlayer tpOwner = TntWars.getPlayer(TntWars.getInstance().getServer().getOfflinePlayer(isla.getOwner()));
        
        if (count == 10) {
            tpExploder.getUserData().setTntPuestas(tpExploder.getUserData().getTntPuestas() + 1);
            tpExploder.save();
            tpExploder.sendMessage("&aHas puesto la TNT y explotará en 10 segundos");
            tpOwner.sendMessage("&cTu isla explotará en 10 segundos si no lo evitas");
        } else if (count > 0 && count < 4) {
            TntWars.getInstance().getGm().getPlayersInGame().forEach(p -> p.playSound(isla.getBedrockCore().getLocation(), Sound.BLOCK_ANVIL_PLACE, (9F + count), 1F));
            tpOwner.sendMessage("&c¡Tu isla explotará en " + count + " segundo" + (count == 1 ? "" : "s") + " si no lo evitas!");
        } else if (count == 0) {
            isla.explode();
            tpExploder.getUserData().setTntExplotadas(tpExploder.getUserData().getTntExplotadas() + 1);
            tpExploder.save();
            TntWars.getInstance().getMsg().sendBroadcast("&eLa isla de " + tpOwner.getName() + " ha sido destruida por " + tpExploder.getName());
            cancel();
        }
        
        count--;
    }
    
}
