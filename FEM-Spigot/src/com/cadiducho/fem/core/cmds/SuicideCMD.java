package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Cooldown;
import java.util.Arrays;

public class SuicideCMD extends FEMCmd {
    
    public SuicideCMD() {
        super("suicide", Grupo.Vip, Arrays.asList("suicidar", "suicidarme"));
    }
    
    private final Cooldown temp = new Cooldown(30);
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (temp.isCoolingDown(user.getPlayer())){
            user.sendMessage("*suicide.cooldown", temp.getTimeLeft(user.getPlayer()));
            return;
        }

        user.suicide();
        temp.setOnCooldown(user.getPlayer());
    }
}
