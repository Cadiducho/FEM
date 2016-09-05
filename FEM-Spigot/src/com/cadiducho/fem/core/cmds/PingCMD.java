package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCMD extends FEMCmd {
    
    public PingCMD() {
        super("ping", Grupo.Usuario, Arrays.asList("pong"));
    }
      
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 1 && user.isOnRank(Grupo.Moderador)) {
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) { 
                user.sendMessage("*userDesconectado");
                return;
            }
            
            user.sendMessage("*ping.otro", target.getName(), format(FEMServer.getUser(target).getPing()));
            return;
        }
        if (user.getPing() < 0) {
            user.sendMessage("&c¡Ha ocurrido un error obteniendo tu ping! Intentalo más tarde");
            return;
        }
        user.sendMessage("*ping.mensaje", format(user.getPing()));
    }
    
    private String format(int ping) {
        String color;
        if (ping <= 130) {
            color ="&a";
        } else if (ping <= 250) {
            color ="&e";
        } else if (ping <= 500) {
            color ="&c";
        } else {
            color="&4";
        }
        return color+ping;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
