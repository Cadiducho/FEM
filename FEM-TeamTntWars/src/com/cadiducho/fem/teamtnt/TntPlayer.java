package com.cadiducho.fem.teamtnt;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.teamtnt.manager.GameState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TntPlayer extends FEMUser {

    private final TeamTntWars plugin = TeamTntWars.getInstance();

    public TntPlayer(UUID id) {
        super(id);
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil(TeamTntWars.getPrefix(), "lobby");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (plugin.getGm().isInLobby()) {
                    board.setName(TeamTntWars.getPrefix());
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
        ScoreboardUtil board = new ScoreboardUtil(TeamTntWars.getPrefix(), "game");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (GameState.state == GameState.GAME) {
                    board.setName(TeamTntWars.getPrefix());
                    board.text(0, "§cmc.undergames.es");
                    board.text(1, "§f ");
                    board.text(2, "§f" + plugin.getGm().getPlayersInGame().size() + "§d/§f" + plugin.getAm().getMaxPlayers());
                    board.text(3, "§7Jugadores: ");
                    
                    int i = 4; //Int para ir aumentando en la posición del scoreboard
                    for (TntIsland isla : plugin.getAm().getIslas()) {
                        if (!"centro".equals(isla.getId()) && isla.getTeam().getEntries() != null) { //Solo islas con jugadores en juego
                            String prefijo = "&a✔  ";
                            String interfijo = isla.getTeam().getName().equalsIgnoreCase(plugin.getTm().getBoard().getEntryTeam(getName()).getName()) ? "&o" : "";
                            if (isla.getDestroyed()) {
                                //Tachar si ha sido eliminado, si no solo mostrar la cruz roja
                                if (isla.getTeam().getEntries() != null) {
                                    interfijo = interfijo + "&m";
                                }
                                prefijo = "&c✘  ";
                            }

                            board.text(i, Metodos.colorizar(prefijo + isla.getColor() + interfijo) + isla.getTeam().getName());
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
        getPlayer().getInventory().setItem(0, ItemUtil.createItem(Material.COMPASS, "&aVuelve al Lobby", "Pulsa para volver al lobby principal de Undergames"));
        getPlayer().getInventory().setItem(8, ItemUtil.createItem(Material.NETHER_STAR, "&cSelección de Equipo"));
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
        Title.sendTitle(getPlayer(), 1, 7, 1, "&b&l¡Has muerto!", "Puedes volver al lobby cuando desees, o ver la partida");
        sendMessage("Escribe &e/lobby &fpara volver al Lobby");
        repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
        getUserData().addDeath(GameID.TNTWARS);
        save();
    }

    public void spawn() {
        getPlayer().teleport(TntIsland.getIsland(plugin.getTm().getBoard().getEntryTeam(getName())).getSpawn());
    }
}
