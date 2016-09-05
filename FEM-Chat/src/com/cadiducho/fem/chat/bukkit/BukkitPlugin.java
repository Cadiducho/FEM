package com.cadiducho.fem.chat.bukkit;

import com.cadiducho.fem.chat.bukkit.commands.IgnoreCommand;
import com.cadiducho.fem.chat.bukkit.commands.RCommand;
import com.cadiducho.fem.chat.bukkit.commands.IgnoradosCommand;
import com.cadiducho.fem.chat.bukkit.commands.TCommand;
import com.cadiducho.fem.chat.FEMChat;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {
 
    private BukkitListener bukkitListener;

    @Override
    public void onEnable() {
        bukkitListener = new BukkitListener(this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, FEMChat.MAIN_CHANNEL);
        getServer().getPluginManager().registerEvents(bukkitListener, this);
        getCommand("tell").setExecutor(new TCommand(this));
        getCommand("r").setExecutor(new RCommand(this));
        getCommand("ignore").setExecutor(new IgnoreCommand(this));
        getCommand("ignorados").setExecutor(new IgnoradosCommand(this));
    }

/*
    public String getGroup(Player player) {
        try {
            if (permission != null && permission.getPrimaryGroup(player) != null) {
                return permission.getPrimaryGroup(player);
            }
        } catch (UnsupportedOperationException ignored) {
        }
        return "Miembro";
    }

    public String getPrefix(Player player) {
        if (chat != null) {
            return chat.getPlayerPrefix(player);
        }
        return "";
    }

    public String getSuffix(Player player) {
        if (chat != null) {
            return chat.getPlayerSuffix(player);
        }
        return "";
    }*/
    
    public void sendPrivateMessage(String target, String from, String mensaje) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        out.writeUTF(FEMChat.PRIVATE_SUBCHANNEL);
        out.writeUTF(target);
        out.writeUTF(from);
        out.writeUTF(mensaje);

        Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
        Player p = (Player) c.toArray()[0];
        p.sendPluginMessage(this, FEMChat.MAIN_CHANNEL, b.toByteArray());
    }
    
    public void ignore(String target, String from) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        out.writeUTF(FEMChat.IGNORE_SUBCHANNEL);
        out.writeUTF(target);
        out.writeUTF(from);

        Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
        Player p = (Player) c.toArray()[0];
        p.sendPluginMessage(this, FEMChat.MAIN_CHANNEL, b.toByteArray());
    }
}
