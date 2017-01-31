package com.cadiducho.fem.lobby.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.lobby.LobbyMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class PruebasCMD extends FEMCmd {

    public PruebasCMD() {
        super("pruebas", Grupo.Admin, Arrays.asList(""));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args){
        LobbyMenu.openMenu(user, LobbyMenu.Menu.PARTICULAS);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
