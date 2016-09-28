package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;

public class AfkCMD extends FEMCmd {

    public AfkCMD() {
        super("afk", Grupo.Admin, Arrays.asList("away", "aefecá"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if (FEMServer.afkMode.contains(user)) {
            plugin.getServer().getOnlinePlayers().stream()
                    .map(p -> FEMServer.getUser(p))
                    .forEach(b -> b.sendMessage("*afk.desactivado", user.getName()));
            FEMServer.afkMode.remove(user);
        } else {
            FEMServer.afkMode.add(user);
            plugin.getServer().getOnlinePlayers().stream()
                    .map(p -> FEMServer.getUser(p))
                    .forEach(b -> b.sendMessage("*afk.activado", user.getName()));
        }
    }
}
