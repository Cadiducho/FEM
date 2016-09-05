package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Cooldown;
import java.util.Arrays;


public class MeCMD extends FEMCmd {
    
    public MeCMD() {
        super("me", Grupo.Vip, Arrays.asList());
    }

    private final Cooldown temp = new Cooldown(30);
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 0) {
            user.sendMessage("*me.uso");
            return;
        }
        
        if (temp.isCoolingDown(user.getPlayer())){
            user.sendMessage("*me.cooldown", temp.getTimeLeft(user.getPlayer()));
            return;
        }

        user.throwMe(args);
        temp.setOnCooldown(user.getPlayer());
    }
}