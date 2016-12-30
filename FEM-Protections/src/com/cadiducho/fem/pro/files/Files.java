package com.cadiducho.fem.pro.files;

import com.cadiducho.fem.pro.Protections;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Files {

    private Protections pro = Protections.getInstance();

    private File fileAreas = new File("plugins/Protections/", "areas.yml");
    @Getter private YamlConfiguration areas = YamlConfiguration.loadConfiguration(fileAreas);

    private File fileConfig = new File("plugins/Protections/", "config.yml");
    @Getter private YamlConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);

    public void setupFiles(){
        if (!fileAreas.exists()){
            areas.options().copyDefaults(true);
        }
        if (!fileConfig.exists()){
            config.options().copyDefaults(true);
        }
        saveFiles();
    }

    public void saveFiles(){
        try{
            areas.save(fileAreas);
            areas.load(fileAreas);
            config.save(fileConfig);
            config.load(fileConfig);
        } catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    private void nextID(){
        int id = getCurrentID();
        id++;
        areas.set("total", id);
        saveFiles();
    }

    public int getID(){
        int previousID = getCurrentID();
        nextID();
        return previousID;
    }

    public int getCurrentID(){
        return areas.getInt("total");
    }
}
