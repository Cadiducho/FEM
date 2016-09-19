package com.cadiducho.fem.tnt;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.GameMode;
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
                    board.text(0, "§cmc..net");
                    if (base.getPlayer() != null) board.build(base.getPlayer());
                } else {
                    board.reset();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public void setGameScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("§d§lTnt§e§lWars", "game");/*
        new BukkitRunnable() {
            @Override
            public void run() {
                if (base.getPlayer() == null) cancel();
                
                if (plugin.getGm().isInGame()) {
                    board.setName("§d§lTnt§e§lWars");
                    board.text(12, "§7Equipos:");
                    board.text(11, "§dMorado: §f" + plugin.getTm().getPuntos(plugin.getTm().morado));
                    board.text(10, "§eAmarillo: §f" + plugin.getTm().getPuntos(plugin.getTm().amarillo));
                    board.text(7, "§f☺ ");
                    board.text(6, "§7Tiempo restante:");
                    board.text(5, "§e" + GameTask.getTimeLeft() + " segundos");
                    board.text(4, "§f☺ ");
                    board.text(0, "§emc..es");
                } else {
                    board.reset();
                    cancel();
                }

                if (base.getPlayer() != null) board.build(base.getPlayer());
            }
        }.runTaskTimer(plugin, 1l, 1l);*/
    }
    
    public void setLobbyPlayer() {
        setWaitScoreboard();
        plugin.getGm().addPlayerToGame(base.getPlayer());
        setCleanPlayer(GameMode.ADVENTURE);
        plugin.getAm().teleport(base.getPlayer());
    }
    
    public void setSpectator() {
        setCleanPlayer(GameMode.SPECTATOR);
        plugin.getGm().getSpectators().add(base.getPlayer());
        plugin.getGm().getPlayersInGame().stream().forEach((inGamePlayers) -> {
            inGamePlayers.hidePlayer(base.getPlayer());
        });
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
