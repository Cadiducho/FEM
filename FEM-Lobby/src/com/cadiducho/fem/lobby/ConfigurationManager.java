package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.scheduler.BukkitRunnable;

public class ConfigurationManager {

    private final Lobby plugin;

    public ConfigurationManager(Lobby plugin) {
        this.plugin = plugin;
    }

    /**
     * Creamos la config ya está to bonito.
     *
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InvalidConfigurationException
     */
    public void load() throws IOException,
            FileNotFoundException, InvalidConfigurationException {

        // Save the default configuration file if it does not exists.
        if (true != new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveDefaultConfig();
        }
    }


}
