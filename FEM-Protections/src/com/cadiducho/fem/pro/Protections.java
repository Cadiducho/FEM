package com.cadiducho.fem.pro;

import com.cadiducho.fem.core.FEMCommands;
import com.cadiducho.fem.pro.cmd.Items;
import com.cadiducho.fem.pro.events.PlayerEvents;
import com.cadiducho.fem.pro.events.WorldEvents;
import com.cadiducho.fem.pro.files.Files;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Protections extends JavaPlugin{

    @Getter private static Protections instance;

    @Getter private Files files = new Files();

    public void onEnable(){
        instance = this;

        registerEvents();
        FEMCommands.registrar(new Items());
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new WorldEvents(this), this);
        pm.registerEvents(new PlayerEvents(this), this);
    }
}
