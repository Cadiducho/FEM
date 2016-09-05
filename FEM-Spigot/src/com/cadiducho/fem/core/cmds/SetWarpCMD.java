package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMServer.Warp;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.Random;
import org.bukkit.Material;

public class SetWarpCMD extends FEMCmd {
    
    public SetWarpCMD() {
        super("setwarp", Grupo.Moderador, Arrays.asList("addwarp"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 0) {
            user.sendMessage("*warp.usoSet");
            return;
        }
        
        String name = args[0];
        if (FEMServer.getWarp(name) != null) {
            user.sendMessage("*warp.yaExiste");
            return;
        } 
        String desc = "";
        Random rand = new Random();
        Material icon = Material.values()[rand.nextInt(Material.values().length)];
        if (args.length >= 2) desc = args[1];
        if (args.length >= 3) {
            Material posibleIcon = Material.matchMaterial(args[2]);
            if (posibleIcon != null) icon = posibleIcon;
        }

        Warp warp = new Warp(name, desc, metodos.locationToString(user.getPlayer().getLocation()), icon.name());
        FEMServer.addWarp(warp);
        user.sendMessage("*warps.creado", args[0]);
    }
}
