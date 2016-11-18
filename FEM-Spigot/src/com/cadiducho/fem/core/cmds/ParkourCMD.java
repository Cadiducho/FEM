package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ParkourCMD extends FEMCmd {

    public ParkourCMD() {
        super("pakour", Grupo.Usuario, Arrays.asList("pk"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if (user.getUserData().getParkourStartTime() != -1L) { //Está en un parkour
            user.sendMessage("&eLlevandote al checkpoint...");
            user.getPlayer().teleport(user.getUserData().getParkourCheckpoint());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
