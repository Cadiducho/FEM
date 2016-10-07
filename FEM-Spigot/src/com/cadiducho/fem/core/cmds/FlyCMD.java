package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FlyCMD extends FEMCmd {
    
    public FlyCMD() {
        super("fly", Grupo.YT, Arrays.asList("volar"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args){
        if(args.length < 1){
            volar(user);
        }
        
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        if (target == null) {
            user.sendMessage("*userDesconectado");
            return;
        }
        volar(target);
        user.sendMessage("*fly.otro", target.getName());

    }
    
    public void volar(FEMUser user){
        if(user.getPlayer().getAllowFlight()){
            user.getPlayer().setAllowFlight(false);
            user.sendMessage("*fly.desactivado");
        } else {
            user.getPlayer().setAllowFlight(true);
            user.sendMessage("*fly.activado");
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
