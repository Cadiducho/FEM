package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class GodCMD extends FEMCmd {
    
    public GodCMD() {
        super("god", Grupo.Moderador, Arrays.asList("dios"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length >= 1 && user.isOnRank(Grupo.Admin)) {
            FEMUser target =  FEMServer.getUser(plugin.getServer().getPlayerExact(args[0]));
            if (!target.isOnline()) {
                user.sendMessage("*userDesconectado");
                return;
            }
            target.getUserData().setGod(!target.getUserData().getGod());
            target.save();
            target.sendMessage(!target.getUserData().getGod() ? "*god.desactivado" : "*god.activado");
            user.sendMessage(!target.getUserData().getGod() ? "*god.desactivadoOtro" : "*god.activadoOtro", target.getName());
            return;
        }
        
        user.getUserData().setGod(!user.getUserData().getGod());
        user.save();
        user.sendMessage(!user.getUserData().getGod() ? "*god.desactivado" : "*god.activado");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
