package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class BurnCMD extends FEMCmd {
    
    public BurnCMD() {
        super("burn", Grupo.Moderador, Arrays.asList("fire", "ignite"));
    }
      
    @Override
    public void run(FEMUser user, String label, String[] args) {
        FEMUser target = user;
        Double time = 10.0;
        switch (args.length) {
            case 2:
                target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
                if (!target.isOnline()) {
                    user.sendMessage("*userDesconectado");
                    return;
                }
                try {
                    time = Double.parseDouble(args[1]);
                } catch (NumberFormatException ex) {
                    user.sendMessage("*burn.uso");
                    return;
                }
                break;
            default:
                user.sendMessage("*burn.uso");
                break;
        }
        
        if (time < 0D) {
            user.sendMessage("*burn.limite");
            return;
        }
        
        time = time * 20; //ticks <3
        target.getPlayer().setFireTicks(time.intValue());
        user.sendMessage("*burn.mensaje", target.getDisplayName(), time / 20);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
