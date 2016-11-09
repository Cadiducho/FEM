package com.cadiducho.fem.chat.bukkit;

import com.cadiducho.fem.chat.FEMChat;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {
 
    private BukkitListener bukkitListener;

    @Override
    public void onEnable() {
        bukkitListener = new BukkitListener(this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, FEMChat.MAIN_CHANNEL);
        getServer().getPluginManager().registerEvents(bukkitListener, this);
    }
}
