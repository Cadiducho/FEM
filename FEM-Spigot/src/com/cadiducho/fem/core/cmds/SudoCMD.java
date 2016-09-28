package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SudoCMD extends FEMCmd {
    
    public SudoCMD() {
        super("sudo", Grupo.Owner, Arrays.asList());
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length < 2) {
            user.sendMessage("*sudo.uso");
            return;
        }
        
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        if (target == null) {
            user.sendMessage("*userDesconectado");
            return;
        }
        
        String comando = Metodos.buildString(args, 1);
        plugin.getServer().dispatchCommand(target.getPlayer(), comando);
        user.sendMessage("*sudo.mensaje", comando, target.getName());
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Metodos.colorizar(FEMFileLoader.getLang().getString("sudo.uso")));
            return;
        }

        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        if (target == null) {
            sender.sendMessage(Metodos.colorizar(FEMFileLoader.getLang().getString("userDesconectado")));
            return;
        }

        String comando = Metodos.buildString(args, 1);
        plugin.getServer().dispatchCommand(target.getPlayer(), comando);
        sender.sendMessage(Metodos.colorizar(FEMFileLoader.getLang().getString("sudo.mensaje").replace("{0}", comando).replace("{1}", target.getName())));
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
