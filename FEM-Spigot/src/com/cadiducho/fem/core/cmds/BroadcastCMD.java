package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;

public class BroadcastCMD extends FEMCmd {

    public BroadcastCMD() {
        super("broadcast", Grupo.Moderador, Arrays.asList("bcast", "bc", "decir"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if (args.length < 1) {
            user.sendMessage("*broadcast.uso");
            return;
        }
        String msg = Metodos.buildString(args);
        metodos.broadcastMsg(msg);
    }

    @Override
    public void run(ConsoleCommandSender sender, String lbl, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Metodos.colorizar(FEMFileLoader.getLang().getString("broadcast.uso")));
            return;
        }

        String msg = Metodos.buildString(args);
        metodos.broadcastMsg(msg);
    }
}
