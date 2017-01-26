package com.cadiducho.fem.color;

import com.cadiducho.fem.color.util.ScoreboardUtil;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Title;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.Random;
import java.util.UUID;

public class DyePlayer extends FEMUser {

    private final DyeOrDie plugin = DyeOrDie.getInstance();

    public DyePlayer(UUID id) {
        super(id);
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = plugin.getLobbyBoard();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (plugin.getGm().isInLobby()) {
                    board.setName(DyeOrDie.colorize("Dye or Die"));
                    board.text(5, "§d ");
                    board.text(4, "§6" + plugin.getGm().getPlayersInGame().size() + "§d/§6" + plugin.getAm().getMaxPlayers());
                    board.text(3, "§a ");
                    board.text(2, "§eEsperando...");
                    board.text(1, "§e ");
                    board.text(0, "§cmc.undergames.es");
                    if (getPlayer() != null) {
                        board.build(getPlayer());
                    }
                } else {
                    board.reset();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }
    
    public void setGameScoreboard() {
        ScoreboardUtil board = plugin.getGameBoard();
        Team tJugadores = board.getScoreboard().getTeam("1DoD") == null ? 
                board.getScoreboard().registerNewTeam("1DoD") : board.getScoreboard().getTeam("1DoD");
        tJugadores.addEntry(getPlayer().getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();

                board.setName(DyeOrDie.colorize("Dye or Die"));
                board.text(7, "§d§f");
                board.text(6, "§dJugadores: ");
                board.text(5, "§6" + plugin.getGm().getPlayersInGame().size() + "§d/§6" + plugin.getAm().getMaxPlayers());
                board.text(4, "§d ");
                board.text(3, "§aRonda: §e" + plugin.getAm().getRound());
                board.text(2, "§aTiempo restante: §e" + plugin.getAm().getTimeleft().intValue());
                board.text(1, "§e ");
                board.text(0, "§cmc.undergames.es");
                if (getPlayer() != null) {
                    board.build(getPlayer());
                }

            }
        }.runTaskTimer(plugin, 20l, 20l);
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

    public void spawn() { 
        getPlayer().teleport(plugin.getAm().getBaseBlocks().get(new Random().nextInt(plugin.getAm().getBaseBlocks().size() - 1)));
    }
    
    public void endGame() {
        getPlayer().getInventory().clear();
        setSpectator();
        getPlayer().teleport(getPlayer().getLocation().add(0, 50, 0));
        plugin.getGm().removePlayerFromGame(getPlayer());
        new Title("&b&l¡Has sido eliminado!", "Has caído en la ronda " + plugin.getAm().getRound(), 1, 2, 1).send(getPlayer());
        sendMessage("Escribe &e/lobby &fpara volver al Lobby");
        repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
        plugin.getMsg().sendBroadcast("&e&l" + getName() + " &aha caido en la ronda &e" + plugin.getAm().getRound() + "&a!");
        sendMessage("¡Enhorabuena! Has llegado hasta la ronda " + plugin.getAm().getRound());
    }
}
