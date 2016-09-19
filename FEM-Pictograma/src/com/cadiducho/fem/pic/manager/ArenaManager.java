package com.cadiducho.fem.pic.manager;

import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.pic.util.CuboidZone;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ArenaManager {

    private final Pictograma plugin;
    @Getter private int minPlayers = 2;
    @Getter private int maxPlayers = 16;
    @Getter private final Location pos1;
    @Getter private final Location pos2;
    @Getter private final Location spawn;
    @Getter private final Location lobby;
    @Getter private final Location paintLoc;
    @Getter @Setter private ArrayList<Player> colaPintar = new ArrayList<>();
    @Getter private final CuboidZone buildZone;
    
    public ArenaManager(Pictograma instance) {
        plugin = instance;
        minPlayers = plugin.getConfig().getInt("Pictograma.Arena.usersMin");
        maxPlayers = plugin.getConfig().getInt("Pictograma.Arena.usersMax");
        
        pos1 = Metodos.stringToLocation(plugin.getConfig().getString("Pictograma.Arena.borderPos1"));
        pos2 = Metodos.stringToLocation(plugin.getConfig().getString("Pictograma.Arena.borderPos2"));
        buildZone = new CuboidZone(pos1.getBlock(), pos2.getBlock());
        spawn = Metodos.stringToLocation(plugin.getConfig().getString("Pictograma.Arena.spawn"));
        paintLoc = Metodos.stringToLocation(plugin.getConfig().getString("Pictograma.Arena.paintLoc"));
        lobby = Metodos.stringToLocation(plugin.getConfig().getString("Pictograma.Arena.lobby"));
    }

    public void prepareWorld(World w) {
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setStorm(false);
        w.setTime(6000);    
        w.getLivingEntities().stream()
                    .filter(e -> !e.getType().equals(EntityType.PLAYER))
                    .forEach(e -> e.damage(e.getMaxHealth()));
        w.setAutoSave(false);
        plugin.getLogger().log(Level.INFO, "Mundo para {0}/{1} preparado", new Object[]{minPlayers, maxPlayers});
    }
}
