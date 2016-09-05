package com.cadiducho.fem.core.cmds;


import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Metodos;

import java.util.Arrays;
import org.bukkit.command.ConsoleCommandSender;

public class WhoisCMD extends FEMCmd {

    public WhoisCMD(){
        super("whois", Grupo.Moderador, Arrays.asList("usuario", "informacion", "quienes"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        String op, god, fly, afk;

        if (args.length == 0) {
            user.sendMessage("*whois.uso");
            return;
        }
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));

        user.sendMessage("&6== &a"+target.getDisplayName() + " &6==");
        user.sendMessage("&7Vida: " + target.getPlayer().getHealth());
        user.sendMessage("&7Hambre: " + target.getPlayer().getFoodLevel() + "/" + target.getPlayer().getSaturation());
        user.sendMessage("&7Ubicación: (" + target.getPlayer().getWorld().getName() + ", " + target.getPlayer().getLocation().getBlockX() + ", " + target.getPlayer().getLocation().getBlockY() + ", " + target.getPlayer().getLocation().getBlockZ() + ")");
        user.sendMessage("&7Modo de juego: " +  target.getPlayer().getGameMode().toString());
        if(target.getUserData().getGod()) {god = "&aSí";} else {god = "&cNo";}
        user.sendMessage("&7God: " + god);
        if(target.getPlayer().isOp()) {op = "&aSí";} else {op = "&cNo";}
        user.sendMessage("&7Op: " +  op);
        if(target.getPlayer().getAllowFlight()) {fly = "&aSí";} else {fly = "&cNo";}
        user.sendMessage("&7Fly: " + fly);
        if(FEMServer.afkMode.contains(target)){afk = "&aSí";} else {afk = "&cNo";}
        user.sendMessage("&7Afk: " + afk);
        if(args.length > 0 && args[1].equalsIgnoreCase("-a")){
            user.sendMessage("&6== &a"+target.getDisplayName() + " &6==");
            user.sendMessage("&7Vida: " + target.getPlayer().getHealth());
            user.sendMessage("&7Hambre: " + target.getPlayer().getFoodLevel() + "/" + target.getPlayer().getSaturation());
            user.sendMessage("&7Exp: " + target.getPlayer().getExp());
            user.sendMessage("&7Ubicación: (" + target.getPlayer().getWorld().getName() + ", " + target.getPlayer().getLocation().getBlockX() + ", " + target.getPlayer().getLocation().getBlockY() + ", " + target.getPlayer().getLocation().getBlockZ() + ")");
            user.sendMessage("&7Modo de juego: " +  target.getPlayer().getGameMode().toString());
            if(target.getUserData().getGod()) {god = "&aSí";} else {god = "&cNo";}
            user.sendMessage("&7God: " + god);
            if(target.getPlayer().isOp()) {op = "&aSí";} else {op = "&cNo";}
            user.sendMessage("&7Op: " +  op);
            if(target.getPlayer().getAllowFlight()) {fly = "&aSí";} else {fly = "&cNo";}
            user.sendMessage("&7Fly: " + fly);
            user.sendMessage("&7Ip: " +  target.getUserData().getIp().getAddress().getHostAddress());
        }
    }
    
    @Override
    public void run(ConsoleCommandSender sender, String label, String[] args) {
        if (args.length == 0) {

            return;
        }
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        
        sender.sendMessage("Usuario: " + target.getDisplayName());
        sender.sendMessage("Grupo: " + target.getUserData().getGrupo().name());
        sender.sendMessage("Conectado: " + target.getUserData().getTimeJoin());
    }

}
