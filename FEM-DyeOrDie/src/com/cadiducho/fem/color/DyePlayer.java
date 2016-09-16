package com.cadiducho.fem.color;

import com.cadiducho.fem.color.util.ScoreboardUtil;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Random;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class DyePlayer {

    private final DyeOrDie plugin = DyeOrDie.getInstance();
    @Getter private final FEMUser base;
    
    public DyePlayer(FEMUser instance) {
        base = instance;
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("Dye or Die", "lobby");
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
                    board.text(0, "§cmc..net");
                    if (base.getPlayer() != null) board.build(base.getPlayer());
                } else {
                    board.reset();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public void setLobbyPlayer() {
        setWaitScoreboard();
        plugin.getGm().addPlayerToGame(base.getPlayer());
        setCleanPlayer(GameMode.ADVENTURE);
        //plugin.getAm().teleport(base.getPlayer());
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
        getBase().getPlayer().getInventory().clear();
        setSpectator();
        base.sendMessage("Escribe &e/lobby &fpara volver al Lobby");
        base.repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
        plugin.getMsg().sendBroadcast(getBase().getDisplayName() + " ha caido en la ronda " + plugin.getAm().getRound() + "!");
        getBase().sendMessage("¡Enhorabuena! Has llegado hasta la ronda " + plugin.getAm().getRound());
    }
}
