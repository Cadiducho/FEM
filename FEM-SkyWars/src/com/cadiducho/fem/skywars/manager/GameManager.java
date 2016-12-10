package com.cadiducho.fem.skywars.manager;

import com.cadiducho.fem.skywars.SkyIsland;
import com.cadiducho.fem.skywars.SkyWars;
import com.cadiducho.fem.skywars.task.LobbyTask;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class GameManager {

    private final SkyWars plugin;

    public GameManager(SkyWars instance) {
        plugin = instance;
    }

    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final ArrayList<Player> spectators = new ArrayList<>();
    @Getter @Setter private boolean dañoEnCaida = true;

    //¿Ha de comprobar el inicio del juego?
    @Getter @Setter private boolean checkStart = true;

    public void checkStart() {
        if (checkStart == true && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = false;
            new LobbyTask(plugin).runTaskTimer(plugin, 1l, 20l);
        }
    }

    public void addPlayerToGame(Player player) {
        if (playersInGame.contains(player)) {
            playersInGame.remove(player);
            playersInGame.add(player);
        } else {
            playersInGame.add(player);
        }
    }

    public void removePlayerFromGame(Player p) {
        SkyIsland isla = SkyIsland.getIsland(p.getUniqueId());
        if (isla != null) plugin.getAm().getUnAssignedIslas().add(SkyIsland.getIsland(p.getUniqueId()));
        playersInGame.remove(p);
    }

    public boolean acceptPlayers() {
        return (GameState.state == GameState.PREPARING || GameState.state == GameState.LOBBY);
    }
     
    public boolean isInLobby() {
        return GameState.state == GameState.LOBBY;
    }
}
