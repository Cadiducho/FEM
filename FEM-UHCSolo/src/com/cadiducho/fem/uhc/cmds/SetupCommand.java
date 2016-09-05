package com.cadiducho.fem.uhc.cmds;

import com.cadiducho.fem.uhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetupCommand implements CommandExecutor {

    public Main plugin;

    public SetupCommand(Main plugin) {
        this.plugin = plugin;
        register();
    }

    public void register() {
        plugin.getCommand("setup").setExecutor(this);
        plugin.getCommand("lobby").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equals("setup")) {
            if (player.hasPermission("DonkeyCraft.UHC.Setup")) {
                if (args.length < 1) {
                    plugin.msg.sendMessage(player, "&cFalta de argumentos");
                } else {
                    if (args[0].equalsIgnoreCase("setspawn")) {
                        plugin.lm.setSpawn(player);
                    }
                    if(args[0].equalsIgnoreCase("generar")){
                        plugin.lm.generateSpawns();
                    }
                    if(args[0].equalsIgnoreCase("caja")){
                        plugin.ic.openCage(player);
                    }
                    if (args[0].equalsIgnoreCase("setserver")) {
                        if (args.length < 1) {
                            plugin.msg.sendMessage(player, "&7Específica el servidor.");
                        } else {
                            plugin.getConfig().set("SERVER", args[1]);
                        }
                    }
                }
            }
        }
        if(cmd.getName().equalsIgnoreCase("lobby")){
            plugin.up.sendToServer(player, "LOBBYUHC001");
            plugin.msg.sendMessage(player, "&7Llevándote al &e&lLOBBY&7.");
        }
        return false;
    }

}
