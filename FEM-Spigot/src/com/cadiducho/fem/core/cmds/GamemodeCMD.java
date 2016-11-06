package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCMD extends FEMCmd {

    public GamemodeCMD(){
        super("gamemode", Grupo.Moderador, Arrays.asList("gm", "gmc", "gms", "gma", "survival", "creative", "adventure"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        FEMUser target = user;
        GameMode mode = null;
        switch (label.toLowerCase()) {
            case "gms":
            case "survial":
                mode = GameMode.SURVIVAL;
                break;
            case "gmc":
            case "creative":
                mode = GameMode.CREATIVE;
                break;
            case "gma":
            case "adventure":
                mode = GameMode.ADVENTURE;
                break;
            case "gmsp":
            case "spectator":
                mode = GameMode.SPECTATOR;
                break;
            case "gm":
            case "gamemode":
                if (args.length == 0) {
                    user.sendMessage("*gamemode.uso");
                    return;
                }
                if (args.length >= 2) {
                    Player pt = plugin.getServer().getPlayer(args[1]);
                    if (pt == null) {
                        user.sendMessage("*userDesconectado");
                        return;
                    }
                    target = FEMServer.getUser(pt);
                }
                
                switch (args[0].toLowerCase()) {
                    case "0":
                    case "s":
                    case "survival":
                        mode = GameMode.SURVIVAL;
                        break;
                    case "1":
                    case "c":
                    case "creative":
                        mode = GameMode.CREATIVE;
                        break;
                    case "2":
                    case "a":
                    case "adventure":
                        mode = GameMode.ADVENTURE;
                        break;
                    case "3":
                    case "sp":
                    case "spectator":
                        mode = GameMode.SPECTATOR;
                        break;
                    default:
                        user.sendMessage("*gamemode.uso");
                        break;
                }
                break;
            default:
                user.sendMessage("*gamemode.uso");
                break;
        }
        
        if (mode != null) {
            target.getPlayer().setGameMode(mode);
            String gm = (mode == GameMode.ADVENTURE ? "aventura" : (mode == GameMode.CREATIVE ? "creativo" : (mode == GameMode.SPECTATOR ? "espectador" : "survival")));
            target.sendMessage("*gamemode.mensaje", gm);
            if (user != target) user.sendMessage("*gamemode.mensaje", target.getName(), gm);
        }
    }
    
    @Override
    public void run(CommandSender sender, String lbl, String[] args) {
    	FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[1]));
    	if(args.length < 2) {
    		sender.sendMessage("Especifica un modo de juego!");
    		return;
    	}
    	if(target == null) {
    		sender.sendMessage("Ese jugador no está conectado");
    		return;
    	}
    	switch(args[0].toLowerCase()) {
    		case "0":
    			target.getPlayer().setGameMode(GameMode.SURVIVAL);
    			break;
    		case "1":
    			target.getPlayer().setGameMode(GameMode.SURVIVAL);
    			break;
    		case "2":
    			target.getPlayer().setGameMode(GameMode.SURVIVAL);
    			break;
    		case "3":
    			target.getPlayer().setGameMode(GameMode.SURVIVAL);
    			break;
    		default:
    			sender.sendMessage("Ese no es un modo de juego válido");
    			break;
    	}
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        ArrayList<String> rtrn = new ArrayList<>();
        if (alias.equalsIgnoreCase("gm") || alias.equalsIgnoreCase("gamemode")) {
            if (curn == 0) {
                for (GameMode g : GameMode.values()) {
                    rtrn.add(g.name().toLowerCase());
                }
                return rtrn;
            }
        }
        return null;
    }
}
