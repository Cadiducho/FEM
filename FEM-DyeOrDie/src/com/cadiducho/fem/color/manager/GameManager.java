package com.cadiducho.fem.color.manager;

import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.color.task.LobbyTask;
import com.cadiducho.fem.color.task.GameTask;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GameManager {

    private final DyeOrDie plugin;

    public GameManager(DyeOrDie instance) {
        plugin = instance;
    }

    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final ArrayList<Player> spectators = new ArrayList<>();

    //¿Ha de comprobar el inicio del juego?
    @Getter @Setter private boolean checkStart = true;

    public void checkStart() {
        if (checkStart == true && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = false;
            new LobbyTask(plugin).runTaskTimer(plugin, 1l, 20l);
        }
    }
    
    public void startGame() {
        plugin.getAm().shuffleMats();
        plugin.getAm().replaceFloor();
        
        for (Player player : getPlayersInGame()) {
            DyeOrDie.getPlayer(player).setGameScoreboard();
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
    
    public boolean acceptPlayers() {
        return (GameState.state == GameState.PREPARING || GameState.state == GameState.LOBBY);
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
