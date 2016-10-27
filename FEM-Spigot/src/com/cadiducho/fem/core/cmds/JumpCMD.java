package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;

public class JumpCMD extends FEMCmd {

    public JumpCMD(){
        super("jump", Grupo.Moderador, Arrays.asList("j"));
    }
    
    @Override
    public void run (FEMUser user, String lbl, String[] args) {
        Location target = user.getPlayer().getTargetBlock((Set<Material>) null, 30).getLocation();
        target.setY(target.getY() + 1);
        target.setYaw(user.getPlayer().getLocation().getYaw());
        target.setPitch(user.getPlayer().getLocation().getPitch());
        user.getPlayer().teleport(target);
    }
    
}
