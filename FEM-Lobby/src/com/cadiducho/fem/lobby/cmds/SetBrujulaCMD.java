package com.cadiducho.fem.lobby.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.core.util.Metodos;
import java.util.Arrays;

public class SetBrujulaCMD extends FEMCmd {

    public SetBrujulaCMD() {
        super("setbrujula", Grupo.Admin, Arrays.asList());
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if (args.length == 0) {
            user.sendMessage("&aUsa: &e/setbrujula <pic/tnt/dod/lg/gem/br/ttnw>");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "pic":
                plugin.getConfig().set("brujula.pic", Metodos.locationToString(user.getPlayer().getLocation()));
                plugin.saveConfig();
                System.out.println(Metodos.locationToString(user.getPlayer().getLocation()));
                break;
            case "tnt":
                plugin.getConfig().set("brujula.tnt", Metodos.locationToString(user.getPlayer().getLocation()));
                plugin.saveConfig();
                System.out.println(Metodos.locationToString(user.getPlayer().getLocation()));
                break;
            case "dod":
                plugin.getConfig().set("brujula.dod", Metodos.locationToString(user.getPlayer().getLocation()));
                plugin.saveConfig();
                System.out.println(Metodos.locationToString(user.getPlayer().getLocation()));
                break;
            case "lg":
                plugin.getConfig().set("brujula.lg", Metodos.locationToString(user.getPlayer().getLocation()));
                plugin.saveConfig();
                System.out.println(Metodos.locationToString(user.getPlayer().getLocation()));
                break;
            case "gem":
                plugin.getConfig().set("brujula.gem", Metodos.locationToString(user.getPlayer().getLocation()));
                plugin.saveConfig();
                System.out.println(Metodos.locationToString(user.getPlayer().getLocation()));
                break;
            case "br":
                plugin.getConfig().set("brujula.br", Metodos.locationToString(user.getPlayer().getLocation()));
                plugin.saveConfig();
                System.out.println(Metodos.locationToString(user.getPlayer().getLocation()));
                break;
            case "ttnw":
                plugin.getConfig().set("brujula.ttnw", Metodos.locationToString(user.getPlayer().getLocation()));
                plugin.saveConfig();
                System.out.println(Metodos.locationToString(user.getPlayer().getLocation()));
                break;
            default:
                user.sendMessage("&aUsa: &e/setbrujula <pic/tnt/dod/lg/gem/br/ttnw>");
                break;
        }  
    }
}
