package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import org.bukkit.Location;

public class SetSpawnCMD extends FEMCmd {
    
    public SetSpawnCMD() {
        super("setspawn", Grupo.Owner, Arrays.asList());
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) { 
        Location nueva = user.getPlayer().getLocation();
        plugin.getConfig().set("teleports.spawn", metodos.locationToString(nueva));
        plugin.saveConfig();
        user.getPlayer().getWorld().setSpawnLocation(nueva.getBlockX(), nueva.getBlockY(), nueva.getBlockZ());
        
        user.sendMessage("*spawn.set", +nueva.getX(), nueva.getY(), nueva.getZ(), nueva.getYaw(), nueva.getPitch());
    }
}
