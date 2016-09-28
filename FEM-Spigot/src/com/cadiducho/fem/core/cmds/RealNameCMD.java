package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;

public class RealNameCMD extends FEMCmd {

    public RealNameCMD(){
        super("realname", Grupo.Admin, Arrays.asList("nombrereal"));
    }
    
    @Override
    public void run (FEMUser user, String lbl, String[] args) {
        if(args.length < 1){
            user.sendMessage("*realname.uso");
            return;
        }
        
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        
        user.sendMessage("*realname.mensaje", target.getName());
    }
    
}
