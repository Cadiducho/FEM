package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HealCMD extends FEMCmd {

    public HealCMD() {
        super("heal", Grupo.Owner, Arrays.asList("cura", "curar", "feed", "alimentar"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if (args.length < 1) {
            user.getPlayer().setHealth(20);
            user.getPlayer().setFoodLevel(20);
            user.sendMessage("*heal.mensaje");
            return;
        }
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        target.getPlayer().setHealth(20);
        target.getPlayer().setFoodLevel(20);
        target.sendMessage("*heal.mensaje");
        user.sendMessage("*heal.otro", target.getName());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
