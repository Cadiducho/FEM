package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SpeedCMD extends FEMCmd {
    
    public SpeedCMD() {
        super("speed", Grupo.Admin, Arrays.asList());
    }
      
    @Override
    public void run(FEMUser user, String label, String[] args) {
        FEMUser target = user;
        Float speed = 1F;
        switch (args.length) {
            case 1:
                try {
                    speed = Float.parseFloat(args[0]);
                } catch (NumberFormatException ex) {
                    user.sendMessage("*speed.uso");
                    return;
                }
                break;
            case 2:
                target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
                if (target.isOnline()) {
                    user.sendMessage("*userDesconectado");
                    return;
                }
                try {
                    speed = Float.parseFloat(args[1]);
                } catch (NumberFormatException ex) {
                    user.sendMessage("*speed.uso");
                    return;
                }
                break;
            default:
                user.sendMessage("*speed.uso");
                break;
        }
        
        if (speed > 10 || speed < 0) {
            user.sendMessage("*speed.limite");
            return;
        }
        
        target.getPlayer().setWalkSpeed(getSpeed(speed, false));
        target.getPlayer().setFlySpeed(getSpeed(speed, true));
        if (user.getUuid() == target.getUuid()) {
            user.sendMessage("*speed.nuevaVelocidad", speed);
        } else {
            user.sendMessage("*speed.nuevaVelocidadOtro", target.getName(), speed);
            target.sendMessage("*speed.nuevaVelocidad", speed);
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
    
    //Parsear velocidad a Minecraft según si vuela o no
    public static Float getSpeed(Float f, Boolean fly) {
        float userSpeed;
        userSpeed = f;
        if (userSpeed > 10.0F) {
            userSpeed = 10.0F;
        } else if (userSpeed < 1.0E-004F) {
            userSpeed = 1.0E-004F;
        }

        float defaultSpeed = fly ? 0.1F : 0.2F;
        float maxSpeed = 1.0F;
        if (userSpeed < 1.0F) {
            return defaultSpeed * userSpeed;
        }
        float ratio = (userSpeed - 1.0F) / 9.0F * (maxSpeed - defaultSpeed);
        return ratio + defaultSpeed;
    }
}
