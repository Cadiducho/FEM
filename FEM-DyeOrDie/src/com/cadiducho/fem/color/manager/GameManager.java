package com.cadiducho.fem.color.manager;

import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.color.task.CountdownTask;
import com.cadiducho.fem.color.task.GameTask;
import java.util.ArrayList;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GameManager {

    private final DyeOrDie plugin;

    public GameManager(DyeOrDie instance) {
        playersInGame = new ArrayList<>();
        plugin = instance;
    }

    @Getter private final ArrayList<Player> playersInGame;

    private boolean checkStart = false;

    public void init() {
        playersInGame.clear();
    }

    public void checkStart() {
        if (checkStart == false && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = true;
            new CountdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
        }
    }
    
    public void startGame() {
        plugin.getAm().shuffleMats();
        plugin.getAm().replaceFloor();
        
        for (Player player : getPlayersInGame()) {
            player.getInventory().clear();
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 5, true), true);
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new GameTask(plugin), 5L);
    }

    public void addPlayerToGame(Player player) {
        if (playersInGame.contains(player)) {
            playersInGame.remove(player);
            playersInGame.add(player);
        } else {
            playersInGame.add(player);
        }
    }

    public void removePlayerFromGame(Player player) {
        playersInGame.remove(player);
    }

    public boolean isEnding() {
        return GameState.state == GameState.ENDING;
    }

    public boolean isInLobby() {
        return GameState.state == GameState.LOBBY;
    }
    
    public boolean isInCountdown() {
        return GameState.state == GameState.COUNTDOWN;
    }
    
    public boolean isInGame() {
        return GameState.state == GameState.GAME;
    }
}
