package com.cadiducho.fem.pic.manager;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.task.GameTask;
import com.cadiducho.fem.pic.task.LobbyTask;
import com.cadiducho.fem.pic.task.ShutdownTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
    @Getter private Scoreboard board;
    @Getter private Objective objective;
    public DyeColor color = DyeColor.BLACK;
    
    public GameManager(Pictograma instance) {
        plugin = instance;
    }

    private boolean checkStart = false;

    public void checkStart() {
        if (checkStart == false && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = true;
            new LobbyTask(plugin).runTaskTimer(plugin, 20l, 20l);
            board = plugin.getServer().getScoreboardManager().getNewScoreboard();
            objective = board.registerNewObjective("puntos", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(Metodos.colorizar("&aPictograma: &6Puntuaciones"));
            plugin.getAm().getBuildZone().clear();
            plugin.getAm().getBuildZone().setWool(DyeColor.WHITE);
        }
    }
    
    public String word = "";
    public StringBuilder wordf;
    public Boolean acceptWords = true;
    public Player builder = null;
    
    //ToDo: Mejorar esto por completo
    public void startRound() {
        searchBuilder();
        hasFound.clear();
        acceptWords = true;
        playerFound = 0;
        word = plugin.getRandomWord();
        wordf = new StringBuilder(word.replaceAll("[a-zA-Z]", "_"));
        plugin.getAm().getBuildZone().clear();
        plugin.getAm().getBuildZone().setWool(DyeColor.WHITE);
        color = DyeColor.BLACK;
        
        Pictograma.getPlayer(builder).setArtist();
        Pictograma.getPlayer(builder).getBase().sendMessage("La palabara es &e" + word);
    
        new GameTask(plugin).runTaskTimer(plugin, 2l, 20l);    
    }
    
    public void endGame() {
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
        if (score.containsKey(winner)) {
            plugin.getMsg().sendBroadcast("&aPuntuaciones:");
            score.keySet().forEach(p -> plugin.getMsg().sendBroadcast("&b" + p.getName() + ":&e " + score.get(p)));
            plugin.getMsg().sendBroadcast("&6Ganador: " + winner.getName());
            HashMap<Integer, Integer> wins = FEMServer.getUser(winner).getUserData().getWins();
            wins.replace(4, wins.get(4) + 1);
            FEMServer.getUser(winner).getUserData().setWins(wins);
            FEMServer.getUser(winner).save();
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
            score.keySet().forEach(p -> p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F));
            FEMServer.getUser(player).getUserData().setPicAcertadas(FEMServer.getUser(player).getUserData().getPicAcertadas() + 1);
            FEMServer.getUser(player).save();
            
            int puntos;
            switch (playerFound) {
                case 0: //10 puntos
                    puntos = 10;
                    plugin.getMsg().sendMessage(builder, "&6+2 &aalguien ha adivinado tu palabra!");
                    increaseScore(builder, 2);
                    FEMServer.getUser(builder).getUserData().setPicDibujadas(FEMServer.getUser(builder).getUserData().getPicDibujadas() + 1);
                    FEMServer.getUser(builder).save();
                    break;
                case 1: //8 puntos
                    puntos = 8;
                    break;
                case 2: //6 puntos
                    puntos = 6;
                    break;
                case 3: //4 puntos
                    puntos = 4;
                    break;
                case 4: //2 puntos
                    puntos = 2;
                    break;
                default: //1 puntito
                    puntos = 1;
                    break;
            }
            increaseScore(player, puntos);
            plugin.getMsg().sendBroadcast("&6+" + puntos + " &a" + player.getName() + " ha encontrado la palabra, y ha sido el más rápido!");
            FEMServer.getUser(builder).getUserData().setPicDibujadas(FEMServer.getUser(builder).getUserData().getPicDibujadas() + 1);
            FEMServer.getUser(builder).save();
            
            playerFound += 1;
        }
        if (playerFound == (getPlayersInGame().size() - 1)) {
            GameTask.getGameInstance().prepareNextRound();
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
        for (Player p : plugin.getAm().getColaPintar()) { 
            if (p.getUniqueId() == player.getUniqueId()) { 
                if (plugin.getAm().getColaPintar().contains(p)) {
                    plugin.getAm().getColaPintar().remove(p); 
                }
            } 
        } 
        score.remove(player);
        board.resetScores(player.getName());
    }
    
    public boolean acceptPlayers() {
        return (GameState.state == GameState.PREPARING || GameState.state == GameState.LOBBY);
    }
    
    public boolean isInGame() {
        return GameState.state == GameState.GAME;
    }
}
