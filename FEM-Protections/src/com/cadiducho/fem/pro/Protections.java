package com.cadiducho.fem.pro;

import com.cadiducho.fem.pro.files.Files;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Protections extends JavaPlugin{

    @Getter private static Protections instance;

    @Getter private Files files = new Files();

    public void onEnable(){
        instance = this;
    }
}
