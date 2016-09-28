package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportHereCMD extends FEMCmd {

    public TeleportHereCMD() {
        super("tphere", Grupo.Owner, Arrays.asList("teleporthere", "s"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 0) {
            user.sendMessage("*tp.usoHere");
            return;
        }

        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        if (!target.isOnline()) {
            user.sendMessage("*userDesconectado");
            return;
        }

        target.getPlayer().teleport(user.getPlayer().getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
        user.sendMessage("*tp.here", target.getName());
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }

}
