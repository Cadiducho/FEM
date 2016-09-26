package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.FEMCommands;
import com.cadiducho.fem.core.cmds.SpawnCMD;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.lobby.cmds.DropPuntosCMD;
import com.cadiducho.fem.lobby.cmds.SetBrujulaCMD;
import com.cadiducho.fem.lobby.listeners.PlayerListener;
import com.cadiducho.fem.lobby.listeners.WorldListener;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Lobby extends JavaPlugin {

    @Getter private static Lobby instance;

    @Getter @Setter private ArrayList<FEMServerInfo> servers;
    @Getter private ItemStack libro;
    
    @Override
    public void onEnable() {
        instance = this;
        servers = new ArrayList<>();

        File fConf = new File(getDataFolder(), "config.yml");
        if (!fConf.exists()) {
            try {
                getConfig().options().copyDefaults(true);
                saveConfig();
            } catch (Exception e) { }
        }

        //Eventos y canales
        PluginManager pluginManager = getServer().getPluginManager();
        PlayerListener pl = new PlayerListener(instance);
        pluginManager.registerEvents(pl, instance);
        pluginManager.registerEvents(new WorldListener(instance), instance);
        
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "FEM");
        getServer().getMessenger().registerIncomingPluginChannel(instance, "FEM", pl);
        
        try {
            //Comandos solo para el lobby
            FEMCommands.registrar(new DropPuntosCMD());
            FEMCommands.registrar(new SetBrujulaCMD());
            FEMCommands.registrar(new SpawnCMD());
            getLogger().log(Level.INFO, "Lobby: Registrado sus comandos");
        } catch (Exception ex) {
            getLogger().log(Level.INFO, "Lobby: No se han podido cargar sus comandos");
        }
        
        //Establecer libro de nuevos
        libro = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) libro.getItemMeta();
        meta.setDisplayName(Metodos.colorizar("Guía del novato"));
        meta.setAuthor(Metodos.colorizar("&6Staff"));

        meta.addPage(Metodos.colorizar("&8&lIntroducción&r:\n"
                + " \n"
                + "&lUnderGames&r es un servidor apoyado por &a&lNVIDIA&r, en el cual puedes disfrutar de muchos minijuegos con tus amigos. Para saber más sobre estos minijuegos, primero lee las normas."));
        meta.addPage(Metodos.colorizar("&8&lNormas&r:\n"
                + " \n"
                + "◾&lNo insultes&r, ni ofendas a otros jugadores\n"
                + "◾&lNo hagas spam&r de productos, webs o enlaces.\n"
                + "◾&lNo escribas abusivamente&r en el chat (flood).\n"
                + "◾&lNo uses hacks/mods&r, excepto Optifine.\n"
                + "◾&lNo uses bugs&r, repórtalos al staff!"));
        meta.addPage(Metodos.colorizar("&e&lPICTOGRAMA:&r\n"
                + " \n"
                + "En cada ronda será seleccionado un artista, el cual intentará representar la palabra oculta, mientras los demás usuarios intentan adivinarla. El jugador con más puntos, ¡gana!"));
        meta.addPage(Metodos.colorizar("&1&lTNT WARS:&r\n"
                + " \n"
                + "Compite contra el resto de jugadores comprando armas, defensas y bloques. Pero ten cuidado, si algún jugador hace estallar tu isla colocando la TNT que aparece en el centro, perderás la oportunidad de revivir. ¡Ganará el último jugador vivo!"));
        meta.addPage(Metodos.colorizar("&5&lDYE or DIE:&r\n"
                + " \n"
                + "Sitúate sobre el color que haya sido seleccionado en la ronda, tras unos segundos toda la pista caerá excepto el color correcto. Sobrevive todas las rondas posibles, si caes al vacío perderás."));
        meta.addPage(Metodos.colorizar("&4&lGLADIATOR:&r\n"
                + " \n"
                + "Rompe los &oluckyblocks&r, que te darán objetos aleatorios. Pasado un tiempo, podrás cocinar, craftear, encantar y equiparte. Una vez listos, apareceréis en la arena donde solo uno de vosotros vencerá!"));
        meta.addPage(Metodos.colorizar("&a&lGEM HUNTERS:&r\n"
                + " \n"
                + "Dos equipos deberán esconder sus gemas por el mapa. Tras ese tiempo, el muro divisorio se abrirá y tu objetivo será encontrar las gemas del enemigo y defender las propias, atacando al equipo contrario."));
        meta.addPage(Metodos.colorizar("&6&lBATTLE ROYALE:&r\n"
                + " \n"
                + "Busca los cofres repartidos por el mapa. Equípate, compra o vende lo que necesites en las tiendas y se el último jugador con vida. Recuerda que los limites del mapa disminuyen y te pueden eliminar.\n"
                + " \n"
                + "&oTiendas:\n"
                + "◾Aldeano blanco (-): Compra objetos\n"
                + "◾Aldeano negro (+): Vende objetos&r"));
        meta.addPage(Metodos.colorizar("Síguenos en Twitter:\n"
                + "&8&l@UnderGames_info\n"
                + " \n"
                + " \n"
                + "¡Bienvenido al servidor y disfruta! ;)"));
        libro.setItemMeta(meta);
        
        //Mini task para que los usuarios no caigan al vacío
        getServer().getScheduler().runTaskTimer(instance, () -> {
            getServer().getOnlinePlayers().stream().forEach(p -> {
                if (p.getLocation().getBlockY() < 0) {
                    p.teleport(p.getWorld().getSpawnLocation());
                }
            });
        }, 20, 20);
        
        getLogger().log(Level.INFO, "Lobby: Activado correctamente");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Lobby: Desativado correctamente");
    }

    @Data
    @AllArgsConstructor
    public class FEMServerInfo {
        private String name;
        private Integer players;
        private ArrayList<String> users;
    }
}
