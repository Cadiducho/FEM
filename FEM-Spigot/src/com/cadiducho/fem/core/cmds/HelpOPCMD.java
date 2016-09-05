package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Cooldown;
import com.cadiducho.fem.core.util.Metodos;
import java.util.Arrays;

public class HelpOPCMD extends FEMCmd {
    
    public HelpOPCMD() {
        super("helpop", Grupo.Usuario, Arrays.asList("hp"));
    }
    
    private final Cooldown temp = new Cooldown(30);
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 0) {
            user.sendMessage("*helpop.uso");
            return;
        }
        
        if (temp.isCoolingDown(user.getPlayer())){
            user.sendMessage("*helpop.cooldown", temp.getTimeLeft(user.getPlayer()));
            return;
        }

        String message = Metodos.buildString(args);
        metodos.enviarPorHP(user.getDisplayName(), message);
        temp.setOnCooldown(user.getPlayer());
        user.sendMessage("*helpop.mensaje");
    }
    
}
