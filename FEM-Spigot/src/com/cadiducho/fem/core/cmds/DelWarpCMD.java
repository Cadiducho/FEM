package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMServer.Warp;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DelWarpCMD extends FEMCmd {
    
    public DelWarpCMD() {
        super("delwarp", Grupo.Admin, Arrays.asList("deletewarp", "elimwarp", "eliminarwarp"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (FEMServer.getWarpNames().isEmpty()) {
            user.sendMessage("*warp.noHay");
            return;
        }
        
        if (args.length == 0) {
            user.sendMessage("*warp.usoDel");
            return;
        }
        
        if (!FEMServer.getWarpNames().contains(args[0])) {
            user.sendMessage("*warp.noExiste", args[0]); 
            return;
        }
            
        Warp warp = FEMServer.getWarp(args[0]);
        FEMServer.removeWarp(warp);
        user.sendMessage("*warp.eliminado", args[0]);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return FEMServer.getWarpNames();
    }
}
