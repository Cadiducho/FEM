package com.cadiducho.fem.tnt;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.tnt.manager.GameState;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class TntPlayer extends FEMUser {

    private final TntWars plugin = TntWars.getInstance();

    public TntPlayer(UUID id) {
        super(id);
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("§d§lTnt§e§lWars", "lobby");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (plugin.getGm().isInLobby()) {
                    board.setName("§d§lTnt§e§lWars");
                    board.text(5, "§d ");
                    board.text(4, "§6" + plugin.getGm().getPlayersInGame().size() + "§d/§6" + plugin.getAm().getMaxPlayers());
                    board.text(3, "§a ");
                    board.text(2, "§eEsperando...");
                    board.text(1, "§e ");
                    board.text(0, "§cmc.undergames.es");
                    if (getPlayer() != null) board.build(getPlayer());
                } else {
                    board.reset();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public void setGameScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("§d§lTnt§e§lWars", "game");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (GameState.state == GameState.GAME) {
                    board.setName("§d§lTnt§e§lWars");
                    board.text(0, "§cmc.undergames.es");
                    board.text(1, "§f ");
                    board.text(2, "§f" + plugin.getGm().getPlayersInGame().size() + "§d/§f" + plugin.getAm().getMaxPlayers());
                    board.text(3, "§7Jugadores: ");
                    
                    int i = 4; //Int para ir aumentando en la posición del scoreboard
                    for (TntIsland isla : plugin.getAm().getIslas()) {
                        if (!"centro".equals(isla.getId()) && isla.getOwner() != null) { //Solo islas con jugadores en juego
                            String prefijo = "&a✔  ";
                            String interfijo = isla.getOwner().equals(getUuid()) ? "&o" : "";
                            if (isla.getDestroyed()) {
                                //Tachar si ha sido eliminado, si no solo mostrar la cruz roja
                                if (!plugin.getGm().getPlayersInGame().contains(plugin.getServer().getPlayer(isla.getOwner()))) {
                                    interfijo = interfijo + "&m";
                                }
                                prefijo = "&c✘  ";
                            }

                            board.text(i, Metodos.colorizar(prefijo + isla.getColor() + interfijo) + plugin.getServer().getOfflinePlayer(isla.getOwner()).getName());
                            i++;    
                        }
                    }
                    
                    board.text(i, "§7"); i++; //espacio en blanco
                    board.text(i, "§7Islas:");
                } else {
                    board.reset();
                    cancel();
                }

                if (getPlayer() != null) board.build(getPlayer());
            }
        }.runTaskTimer(plugin, 1l, 1l);
    }
    
    public void setLobbyPlayer() {
        setWaitScoreboard();
        plugin.getGm().addPlayerToGame(getPlayer());
        setCleanPlayer(GameMode.ADVENTURE);
    }
    
    public void setSpectator() {
        setCleanPlayer(GameMode.SPECTATOR);
        plugin.getGm().getSpectators().add(getPlayer());
        plugin.getGm().getPlayersInGame().stream().forEach(ig -> ig.hidePlayer(getPlayer()));
    }

    public void setCleanPlayer(GameMode gameMode) {
        getPlayer().setHealth(getPlayer().getMaxHealth());
        getPlayer().setFoodLevel(20);
        getPlayer().setExp(0);
        getPlayer().setTotalExperience(0);
        getPlayer().setLevel(0);
        getPlayer().setFireTicks(0);
        getPlayer().getInventory().clear();
        getPlayer().getInventory().setArmorContents(null);
        getPlayer().setGameMode(gameMode);
        getPlayer().getActivePotionEffects().forEach(ef -> getPlayer().removePotionEffect(ef.getType())); 
    }
    
    public void death() {
        getPlayer().getInventory().clear();
        setSpectator();
        plugin.getGm().removePlayerFromGame(getPlayer());
        new Title("&b&l¡Has muerto!", "Puedes volver al lobby cuando desees, o ver la partida", 1, 2, 1).send(getPlayer()); 
        sendMessage("Escribe &e/lobby &fpara volver al Lobby");
        repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
        HashMap<Integer, Integer> deaths = getUserData().getDeaths();
        deaths.replace(1, deaths.get(1) + 1);
        getUserData().setDeaths(deaths);
        save();
    }

    public void spawn() {
        getPlayer().teleport(TntIsland.getIsland(getUuid()).getSpawn());
    }
}
