package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd.Grupo;
import java.util.Arrays;
import java.util.List;

import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class DarRangoCMD extends FEMCmd {

    public DarRangoCMD(){
        super("darrango", Grupo.Moderador, Arrays.asList("dargrupo", "setgrupo", "setgroup"));
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
    public void run(ConsoleCommandSender sender, String lbl, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Metodos.colorizar(FEMFileLoader.getEsLang().getString("darrango.uso")));
            return;
        }
        Integer i;
        try {
            i = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(Metodos.colorizar(FEMFileLoader.getEsLang().getString("darrango.uso")));
            return;
        }

        if (i > Grupo.values().length) {
            sender.sendMessage(Metodos.colorizar(FEMFileLoader.getEsLang().getString("darrango.uso")));
            return;
        }
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        target.getUserData().setGrupo(Grupo.values()[i]);
        target.save();
        sender.sendMessage(Metodos.colorizar(FEMFileLoader.getEsLang().getString("darrango.mensaje")));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}