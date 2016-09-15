package com.cadiducho.fem.pic.manager;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.task.LobbyTask;
import com.cadiducho.fem.pic.task.ShutdownTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class GameManager {

    private final Pictograma plugin;
    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final ArrayList<Player> hasFound = new ArrayList<>();
    private int playerFound = 0;
    @Getter private final HashMap<Player, Integer> score = new HashMap<>();
    private ArrayList<BukkitTask> tasks = new ArrayList<>();
    private final BossBar barra;
    private final BossBar barraCheta; //Para los pros que saben la palabra
    @Getter private Scoreboard board;
    @Getter private Objective objective;
    
    public GameManager(Pictograma instance) {
        plugin = instance;
        barra = plugin.getServer().createBossBar("Pictograma", BarColor.BLUE, BarStyle.SOLID); 
        barraCheta = plugin.getServer().createBossBar("Pictograma", BarColor.BLUE, BarStyle.SOLID); 
    }

    private boolean checkStart = false;

    public void init() {
        playersInGame.clear();
    }

    public void checkStart() {
        if (checkStart == false && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = true;
            new LobbyTask(plugin).runTaskTimer(plugin, 20l, 20l);
            board = plugin.getServer().getScoreboardManager().getNewScoreboard();
            objective = board.registerNewObjective("puntos", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(Metodos.colorizar("&aPictograma: &6Puntuaciones"));
        }
    }
    
    public String word;
    public StringBuilder wordf;
    public Boolean wordHasBeenFound = false;
    public Boolean acceptWords = true;
    public Player builder = null;
    
    //ToDo: Mejorar esto por completo
    public void startRound() {
        cancelTasks();
        searchBuilder();
        hasFound.clear();
        playerFound = 0;
        word = plugin.getRandomWord();
        barraCheta.setTitle(Metodos.colorizar("&e" + word));
        wordf = new StringBuilder(word.replaceAll("[a-zA-Z]", "_"));
        plugin.getAm().getBuildZone().clear();
        plugin.getAm().getBuildZone().setWool(DyeColor.WHITE);
        getPlayersInGame().forEach(p -> {
            barra.removePlayer(p);
            barra.addPlayer(p);
        });
        barra.removePlayer(builder);
        barraCheta.addPlayer(builder);
        
        Pictograma.getPlayer(builder).setArtist();
        Pictograma.getPlayer(builder).getBase().sendMessage("La palabara es &e" + word);
        barra.setTitle(wordf.toString());
    
        tasks.add(plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.getMsg().sendBroadcast("&630 segundos para terminar la ronda!"), 600L));
        tasks.add(plugin.getServer().getScheduler().runTaskLater(plugin, () -> addRandomLetter(), 800L));
        tasks.add(plugin.getServer().getScheduler().runTaskLater(plugin, () -> addRandomLetter(), 1000L));
        tasks.add(plugin.getServer().getScheduler().runTaskLater(plugin, () -> addRandomLetter(), 1100L));
        tasks.add(plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.getMsg().sendBroadcast("&610 segundos para terminar la ronda!"), 1200L));
        prepareNextRound();     
    }

    public void cancelTasks() {
        tasks.stream().forEach(r -> r.cancel());
    }
    
    public void prepareNextRound() {
        tasks.add(plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            Pictograma.getPlayer(builder).setCleanPlayer(GameMode.ADVENTURE);
            Pictograma.getPlayer(builder).spawn();
            barraCheta.removePlayer(builder);
            builder = null;
            barra.setTitle(word);
            if (plugin.getAm().getColaPintar().isEmpty()) {
                end();
                return;
            }
            plugin.getMsg().sendBroadcast("&aSe acabó el tiempo! La siguiente ronda comenzará en 5 segundos"); 
        }, 1400L));
        tasks.add(plugin.getServer().getScheduler().runTaskLater(plugin, () -> startRound(), 1500L));
    }
    
    public void end() {
        plugin.getMsg().sendBroadcast("¡El juego ha acabado!");
        
        //Checkwinner          
        Player winner = null;
        for (Player p : score.keySet()) {
            if (winner != null) {
                if (score.get(p) > score.get(winner)) {
                    winner = p;
                }
            } else {
                winner = p;
            }
        }
        if (this.score.containsKey(winner)) {
            plugin.getMsg().sendBroadcast("&aPuntuaciones:");
            score.keySet().forEach(p -> plugin.getMsg().sendBroadcast("&b" + p.getName() + ":&e " + score.get(p)));
            plugin.getMsg().sendBroadcast("&6Ganador: " + winner.getName());
        }
        new ShutdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
    }
    
    public void searchBuilder() {
        for (Player p : plugin.getAm().getColaPintar()) {
            builder = p;
            plugin.getAm().getColaPintar().remove(p);
            break;
        }
    }
    
    public void addRandomLetter() {
        Random r = new Random();
        int randomLetter = r.nextInt(word.length());
        wordf.setCharAt(randomLetter, word.charAt(randomLetter));
        plugin.getMsg().sendBroadcast("&aLa palabra es &e" + wordf);
        barra.setTitle(Metodos.colorizar("&e"+wordf.toString()));
    }
   
    public void increaseScore(Player p, int value) {
        if (score.containsKey(p)) {
            score.put(p, (score.get(p)) + value);
            Score scoreBoard = objective.getScore(p.getName());
            scoreBoard.setScore((score.get(p)));
        }
    }
    
    public void wordFoundBy(Player player) {
        if (acceptWords && !hasFound.contains(player)) {
            hasFound.add(player);
            score.keySet().forEach(p -> p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 1.0F));
            if (!wordHasBeenFound) {
                plugin.getMsg().sendBroadcast("&6+3 &a" + player.getName() + " ha encontrado la palabra, y ha sido el más rápido!");
                plugin.getMsg().sendMessage(builder, "&6+2 &aalguien ha adivinado tu palabra!");
                increaseScore(player, 3);
                increaseScore(builder, 2);
                wordHasBeenFound = true;
            } else {
                plugin.getMsg().sendBroadcast("&6+1 &a" + player.getName() + " ha encontrado la palabra!");
                increaseScore(player, 1);
            }
            playerFound += 1;
        }
        if (playerFound == (getPlayersInGame().size() - 1)) {
            cancelTasks();
            prepareNextRound();
        }
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
