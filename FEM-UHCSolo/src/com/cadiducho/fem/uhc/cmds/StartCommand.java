package com.cadiducho.fem.uhc.cmds;

import com.cadiducho.fem.uhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    public Main plugin;

    public StartCommand(Main plugin) {
        this.plugin = plugin;
        register();
    }

    public void register() {
        plugin.getCommand("start").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("start")){
            if(player.hasPermission("DonkeyUHC.StartGame")){
                plugin.gm.startGame();
            }
        }
        return false;
    }

}
