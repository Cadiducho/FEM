package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class AdminChatCMD extends FEMCmd {

    public AdminChatCMD() {
        super("a", Grupo.Admin, Arrays.asList("adminchat"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        if(args.length > 0) {
            String message = Metodos.buildString(args);
            metodos.enviarPorAdminChat(user.getDisplayName(), message);
        } else {
            user.toggleAdminChat();
        }
    }
    
    @Override
    public void run(ConsoleCommandSender sender, String label, String[] args) {
        if(args.length < 0) {
            sender.sendMessage(Metodos.colorizar(FEMFileLoader.getEsLang().getString("adminchat.uso")));
        } else {
            String message = Metodos.buildString(args);
            metodos.enviarPorAdminChat("Consola", message);
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
