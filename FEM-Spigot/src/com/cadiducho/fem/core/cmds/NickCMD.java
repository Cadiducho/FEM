package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Metodos;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCMD extends FEMCmd {
    
    public NickCMD() {
        super("nick", Grupo.Moderador, Arrays.asList("nickname"));
    }
      
    @Override
    public void run(FEMUser user, String label, String[] args) {
        FEMUser target = user;
        String nick;
        switch (args.length) {
            case 1:
                nick = args[0];
                break;
            case 2:
                target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
                if (!target.isOnline()) {
                    user.sendMessage("*userDesconectado");
                    return;
                }
                nick = args[1];
                break;
            default:
                user.sendMessage("*nick.uso");
                return;
        }
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.equals(target.getPlayer())) {
                continue;
            }
            if (p.getName().equalsIgnoreCase(ChatColor.stripColor(nick)) || FEMServer.getUser(p).getDisplayName().equalsIgnoreCase(ChatColor.stripColor(nick))) {
                user.sendMessage("*nick.yaEnUso", p.getName());
                return;
            }
        }
        nick = Metodos.colorizar(nick);
        target.getPlayer().setDisplayName(nick.equalsIgnoreCase("off") ? target.getName() : nick);
        target.getUserData().setNickname(nick.equalsIgnoreCase("off") ? null : nick);
        if (user.getUuid() == target.getUuid()) {
            user.sendMessage("*nick.nuevoNick", nick.equalsIgnoreCase("off") ? target.getName() : nick);
        } else {
            user.sendMessage("*nick.nuevoNickOtro", target.getName(), nick.equalsIgnoreCase("off") ? target.getName() : nick);
            target.sendMessage("*nick.nuevoNick", nick.equalsIgnoreCase("off") ? target.getName() : nick);
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
