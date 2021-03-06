package com.cadiducho.fem.pic.manager;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.pic.PicPlayer;
import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.task.GameTask;
import com.cadiducho.fem.pic.task.LobbyTask;
import com.cadiducho.fem.pic.task.ShutdownTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private final Pictograma plugin;
    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final ArrayList<UUID> hasFound = new ArrayList<>();
    @Getter private final HashMap<Player, Integer> score = new HashMap<>();
    @Getter private final ArrayList<Block> blockList = new ArrayList<>();
    public DyeColor color = DyeColor.BLACK;
    public String word = "";
    public StringBuilder wordf;
    public Boolean acceptWords = true;
    public Player builder = null;
    private int playerFound = 0;
    
    //¿Ha de comprobar el inicio del juego?
    @Getter @Setter private boolean checkStart = true;
    
    public GameManager(Pictograma instance) {
        plugin = instance;
    }

    public void checkStart() {
        if (checkStart == true && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = false;
            new LobbyTask(plugin).runTaskTimer(plugin, 1l, 20l);
            plugin.getAm().getBuildZone().clear();
            plugin.getAm().getBuildZone().setWool(DyeColor.WHITE);
        }
    }

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

        Title.sendTitle(builder, 0, 5, 0, "&e&l" + word, "&3Dibuja esta palabra");
        Pictograma.getPlayer(builder).setArtist();
        Pictograma.getPlayer(builder).sendMessage("La palabara es &e" + word);

        new GameTask(plugin).runTaskTimer(plugin, 2l, 20l);
    }

    public void endGame() {
        plugin.getMsg().sendBroadcast("¡El juego ha acabado!");

        //Checkwinner          
        Player winner = null;
        for (Player p : score.keySet()) {
            int puntos = score.get(p);
            if (winner != null) {
                if (puntos > score.get(winner)) {
                    winner = p;
                }
            } else {
                winner = p;
            }

            final PicPlayer picPlayer = Pictograma.getPlayer(p);
            picPlayer.getUserData().setPicPuntosTotales(picPlayer.getUserData().getPicPuntosTotales() + puntos);
            picPlayer.getUserData().setCoins(picPlayer.getUserData().getCoins() + 5);
        }
        if (score.containsKey(winner)) {
            plugin.getMsg().sendBroadcast("&aPuntuaciones:");
            score.keySet().forEach(p -> plugin.getMsg().sendBroadcast("&b" + p.getName() + ":&e " + score.get(p)));
            plugin.getMsg().sendBroadcast("&6Ganador: " + winner.getName());
            for (Player p : plugin.getGm().getPlayersInGame()) {
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                Title.sendTitle(p, 1, 7, 1, "&c" + winner.getName(), "&aha ganado la partida!");
            }

            final PicPlayer pp = Pictograma.getPlayer(winner);
            pp.getUserData().addWin(GameID.PICTOGRAMA);
            pp.save();
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
        }
    }
    
    public ArrayList<Player> getTop() {
        ArrayList top = new ArrayList<>();
        HashMap<Player, Integer> clon = (HashMap) score.clone();
        int times = 3;

        if (clon.keySet().size() == 2) times = 2;
        
        for (int i = 0; i < times; i++) {
            int max = 0;
            Player mejor = null;
            for (Map.Entry<Player, Integer> entry : clon.entrySet()) {
                Player player = entry.getKey();
                Integer puntos = entry.getValue();
                if (puntos >= max) {
                    max = puntos;
                    mejor = player;
                }
            }
            top.add(i, mejor);
            clon.remove(mejor);
        }
        return top;
    }

    public void wordFoundBy(Player player) {
        final PicPlayer pp = Pictograma.getPlayer(player);
        final PicPlayer ppBuilder = Pictograma.getPlayer(builder);

        if (!acceptWords) {
            pp.sendMessage("&e'No puedes escribir una palabra fuera de tiempo!");
            return;
        }
        if (!hasFound.contains(player.getUniqueId())) {
            hasFound.add(player.getUniqueId());
            score.keySet().forEach(p -> p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F));
            pp.getUserData().setPicAcertadas(pp.getUserData().getPicAcertadas() + 1);
            pp.save();

            int puntos;
            String sufijo = "!";
            switch (playerFound) {
                case 0: //10 puntos
                    puntos = 10;
                    sufijo = ", y ha sido el más rápido!";
                    plugin.getMsg().sendMessage(builder, "&6+2 &aalguien ha adivinado tu palabra!");
                    increaseScore(builder, 2);

                    ppBuilder.getUserData().setPicDibujadas(ppBuilder.getUserData().getPicDibujadas() + 1);
                    ppBuilder.save();
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
            pp.getUserData().setCoins(pp.getUserData().getCoins() + 1);
            plugin.getMsg().sendBroadcast("&6+" + puntos + " &a" + player.getName() + " ha encontrado la palabra" + sufijo);
            pp.save();

            playerFound += 1;
        }
        if (playerFound == (getPlayersInGame().size() - 1)) {
            pp.getUserData().setCoins(pp.getUserData().getCoins() + 10);
            pp.save();
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

        if (builder != null && (player.getUniqueId() == builder.getUniqueId())) {
            GameTask.getGameInstance().prepareNextRound();
        }
        //Elminar de la cola
        for (Player p : plugin.getAm().getColaPintar()) {
            if (p.getUniqueId() == player.getUniqueId()) {
                if (plugin.getAm().getColaPintar().contains(p)) {
                    plugin.getAm().getColaPintar().remove(p);
                }
            }
        }

        //Eliminar sus puntos
        if (score != null) score.remove(player);
    }

    public boolean acceptPlayers() {
        return (GameState.state == GameState.PREPARING || GameState.state == GameState.LOBBY);
    }

    public boolean isInGame() {
        return GameState.state == GameState.GAME;
    }
}
