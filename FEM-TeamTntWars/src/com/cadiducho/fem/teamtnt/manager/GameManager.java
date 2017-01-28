package com.cadiducho.fem.teamtnt.manager;

import com.cadiducho.fem.teamtnt.TeamTntWars;
import com.cadiducho.fem.teamtnt.TntIsland;
import com.cadiducho.fem.teamtnt.task.LobbyTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GameManager {

    private final TeamTntWars plugin;

    public GameManager(TeamTntWars instance) {
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
        TntIsland isla = TntIsland.getIsland(plugin.getTm().getBoard().getEntryTeam(p.getName()));
        if (isla != null) plugin.getAm().getUnAssignedIslas().add(isla);
        playersInGame.remove(p);
        plugin.getTm().deadPlayer(p);
    }

    public boolean acceptPlayers() {
        return (GameState.state == GameState.PREPARING || GameState.state == GameState.LOBBY);
    }
     
    public boolean isInLobby() {
        return GameState.state == GameState.LOBBY;
    }
}
