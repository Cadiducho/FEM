package com.cadiducho.fem.color;

import com.cadiducho.fem.color.util.ScoreboardUtil;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Title;
import java.util.Random;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class DyePlayer {

    private final DyeOrDie plugin = DyeOrDie.getInstance();
    @Getter private final FEMUser base;
    
    public DyePlayer(FEMUser instance) {
        base = instance;
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = plugin.getLobbyBoard();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (base.getPlayer() == null) cancel();
                
                if (plugin.getGm().isInLobby()) {
                    board.setName(DyeOrDie.colorize("Dye or Die"));
                    board.text(5, "§d ");
                    board.text(4, "§6" + plugin.getGm().getPlayersInGame().size() + "§d/§6" + plugin.getAm().getMaxPlayers());
                    board.text(3, "§a ");
                    board.text(2, "§eEsperando...");
                    board.text(1, "§e ");
                    board.text(0, "§cmc.undergames.es");
                    if (base.getPlayer() != null) {
                        board.build(base.getPlayer());
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
        tJugadores.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        tJugadores.addEntry(base.getPlayer().getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (base.getPlayer() == null) cancel();

                board.setName(DyeOrDie.colorize("Dye or Die"));
                board.text(5, "§d ");
                board.text(4, "§6" + plugin.getGm().getPlayersInGame().size() + "§d/§6" + plugin.getAm().getMaxPlayers());
                board.text(3, "§aRonda: §e" + plugin.getAm().getRound());
                board.text(2, "§aTiempo restante: §e" + plugin.getAm().getTimeleft().intValue());
                board.text(1, "§e ");
                board.text(0, "§cmc.undergames.es");
                if (base.getPlayer() != null) {
                    board.build(base.getPlayer());
                }

            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public void setLobbyPlayer() {
        setWaitScoreboard();
        plugin.getGm().addPlayerToGame(base.getPlayer());
        setCleanPlayer(GameMode.ADVENTURE);
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

    public void spawn() { 
        base.getPlayer().teleport(plugin.getAm().getWhiteblocks().get(new Random().nextInt(plugin.getAm().getWhiteblocks().size())));
    }
    
    public void endGame() {
        base.getPlayer().getInventory().clear();
        setSpectator();
        base.getPlayer().teleport(base.getPlayer().getLocation().add(0, 50, 0));
        plugin.getGm().removePlayerFromGame(base.getPlayer());
        new Title("&b&l¡Has sido eliminado!", "Has caído en la ronda " + plugin.getAm().getRound(), 1, 2, 1).send(base.getPlayer());
        base.sendMessage("Escribe &e/lobby &fpara volver al Lobby");
        base.repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
        plugin.getMsg().sendBroadcast(base.getName() + " ha caido en la ronda " + plugin.getAm().getRound() + "!");
        base.sendMessage("¡Enhorabuena! Has llegado hasta la ronda " + plugin.getAm().getRound());
    }
}
