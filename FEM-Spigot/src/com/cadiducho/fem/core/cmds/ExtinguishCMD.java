package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ExtinguishCMD extends FEMCmd {

    public ExtinguishCMD() {
        super("extinguish", Grupo.Moderador, Arrays.asList("ext", "unfire"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        FEMUser target = user;
        if (args.length == 1) {
            target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
            if (target.isOnline()) {
                user.sendMessage("*userDesconectado");
                return;
            }
        }

        target.getPlayer().setFireTicks(0);
        if (user.getUuid() == target.getUuid()) {
            user.sendMessage("*extinguish.mensaje");
        } else {
            user.sendMessage("*extinguish.mensajeOtro", target.getName());
            target.sendMessage("*extinguish.mensaje");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
