package com.cadiducho.fem.lucky.manager;

import java.util.HashMap;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.utils.ScoreboardUtil;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerManager {

    private final LuckyWarriors plugin;

    public PlayerManager(LuckyWarriors instance) {
        plugin = instance;
    }

    @Getter private final HashMap<Player, Integer> kills = new HashMap<>();

    public void setScoreboard(Player player) {
        ScoreboardUtil board = new ScoreboardUtil("LuckyWarriors");
        new BukkitRunnable() {
            @Override
            public void run() {
                board.text(10, "§c ");
                board.text(9, "Jugadores");
                board.text(8, "§f" + plugin.getGm().getPlayersInGame().size() + "/" + plugin.getAm().getMaxPlayers());
                board.text(7, "§3 ");
                board.text(6, "Asesinatos");
                board.text(5, "§f" + getKillsToString(player));

                board.build(player);
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public void setLobbyPlayer(Player player) {
        plugin.getGm().addPlayerToGame(player);
        setCleanPlayer(player, GameMode.ADVENTURE);
        setScoreboard(player);
    }

    public void setSpectator(Player player) {
        setCleanPlayer(player, GameMode.SPECTATOR);
        plugin.getGm().getSpectators().add(player);
        plugin.getGm().getPlayersInGame().stream().forEach(ig -> ig.hidePlayer(player));
    }
    
    public void setCleanPlayer(Player player, GameMode gameMode) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setExp(0);
        player.setTotalExperience(0);
        player.setLevel(0);
        player.setFireTicks(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(gameMode);
        player.getActivePotionEffects().stream().forEach((effect) -> {
            player.removePotionEffect(effect.getType());
        });
    }

    public int getKillsToString(Player player) {
        if (kills.containsKey(player)) {
            return kills.get(player);
        }
        return 0;
    }

    public void addKillToPlayer(Player player) {
        int actual = 0;
        if (kills.containsKey(player)) {
            actual = kills.get(player);
        }
        kills.put(player, actual + 1);
    }

    /*public HashMap<Player, Integer> getKills() {
        return kills;
    }*/
}
