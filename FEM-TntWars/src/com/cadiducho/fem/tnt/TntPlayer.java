package com.cadiducho.fem.tnt;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.tnt.manager.GameState;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class TntPlayer {

    private final TntWars plugin = TntWars.getInstance();
    @Getter private final FEMUser base;
    
    public TntPlayer(FEMUser instance) {
        base = instance;
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("§d§lTnt§e§lWars", "lobby");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (base.getPlayer() == null) cancel();
                
                if (plugin.getGm().isInLobby()) {
                    board.setName("§d§lTnt§e§lWars");
                    board.text(5, "§d ");
                    board.text(4, "§6" + plugin.getGm().getPlayersInGame().size() + "§d/§6" + plugin.getAm().getMaxPlayers());
                    board.text(3, "§a ");
                    board.text(2, "§eEsperando...");
                    board.text(1, "§e ");
                    board.text(0, "§cmc.undergames.es");
                    if (base.getPlayer() != null) board.build(base.getPlayer());
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
                if (base.getPlayer() == null) cancel();
                
                if (GameState.state == GameState.GAME) {
                    board.setName("§d§lTnt§e§lWars");
                    board.text(0, "§cmc.undergames.es");
                    board.text(1, "§f ");
                    
                    int i = 2; //Int para ir aumentando en la posición del scoreboard
                    for (TntIsland isla : plugin.getAm().getIslas()) {
                        if (!"centro".equals(isla.getId()) && isla.getOwner() != null) { //Solo islas con jugadores en juego
                            String prefijo = "&a✔  ";
                            String interfijo = isla.getOwner().equals(base.getUuid()) ? "&o" : "";
                            if (isla.getDestroyed()) {
                                //Tachar si ha sido eliminado, si no solo mostrar la cruz roja
                                if (!plugin.getGm().getPlayersInGame().contains(plugin.getServer().getPlayer(isla.getOwner()))) {
                                    interfijo = interfijo + "&m";
                                }
                                prefijo = "&c✘  ";
                            }

                            board.text(i, Metodos.colorizar(prefijo) + isla.getColor() + Metodos.colorizar(interfijo) + plugin.getServer().getPlayer(isla.getOwner()).getDisplayName());
                            i++;    
                        }
                    }
                    
                    board.text(i, "§7"); i++; //espacio en blanco
                    board.text(i, "§7Islas:");
                } else {
                    board.reset();
                    cancel();
                }

                if (base.getPlayer() != null) board.build(base.getPlayer());
            }
        }.runTaskTimer(plugin, 1l, 1l);
    }
    
    public void setLobbyPlayer() {
        setWaitScoreboard();
        plugin.getGm().addPlayerToGame(base.getPlayer());
        setCleanPlayer(GameMode.ADVENTURE);
    }
    
    public void setSpectator() {
        setCleanPlayer(GameMode.SPECTATOR);
        plugin.getGm().getSpectators().add(base.getPlayer());
        plugin.getGm().getPlayersInGame().stream().forEach(ig -> ig.hidePlayer(base.getPlayer()));
    }

    public void setCleanPlayer(GameMode gameMode) {
        base.getPlayer().setHealth(base.getPlayer().getMaxHealth());
        base.getPlayer().setFoodLevel(20);
        base.getPlayer().setExp(0);
        base.getPlayer().setTotalExperience(0);
        base.getPlayer().setLevel(0);
        base.getPlayer().setFireTicks(0);
        base.getPlayer().getInventory().clear();
        base.getPlayer().getInventory().setArmorContents(null);
        base.getPlayer().setGameMode(gameMode);
        base.getPlayer().getActivePotionEffects().forEach(ef -> base.getPlayer().removePotionEffect(ef.getType())); 
    }
    
    public void death() {
        getBase().getPlayer().getInventory().clear();
        setSpectator();
        plugin.getGm().removePlayerFromGame(base.getPlayer());
        new Title("&b&l¡Has muerto!", "Puedes volver al lobby cuando desees, o ver la partida", 1, 2, 1).send(getBase().getPlayer()); 
        base.sendMessage("Escribe &e/lobby &fpara volver al Lobby");
        base.repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
        HashMap<Integer, Integer> deaths = base.getUserData().getDeaths();
        deaths.replace(1, deaths.get(1) + 1);
        base.getUserData().setDeaths(deaths);
        base.save();
    }

    public void spawn() {
        getBase().getPlayer().teleport(Metodos.centre(TntIsland.getIsland(getBase().getUuid()).getSpawn()));
    }
}
