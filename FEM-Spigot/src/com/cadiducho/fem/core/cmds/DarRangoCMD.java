package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd.Grupo;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DarRangoCMD extends FEMCmd {

    public DarRangoCMD(){
        super("darrango", Grupo.Admin, Arrays.asList("dargrupo", "setgrupo", "setgroup"));
    }
    
    @Override
    public void run (FEMUser user, String lbl, String[] args) {
        if(args.length < 2){
            user.sendMessage("*darrango.uso", lbl, Grupo.values().length - 1);
            return;
        }
        Integer i;
        try {
            i = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            user.sendMessage("*darrango.uso", lbl, Grupo.values().length - 1);
            return;
        }
        
        if (i > Grupo.values().length) {
            user.sendMessage("*darrango.uso", lbl, Grupo.values().length - 1);
            return;
        }
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        target.getUserData().setGrupo(Grupo.values()[i]);
        target.save();
        user.sendMessage("*darrango.mensaje", target.getName(), Grupo.values()[i].name());
    }
    
    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}