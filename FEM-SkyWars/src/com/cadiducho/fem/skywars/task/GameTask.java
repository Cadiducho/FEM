package com.cadiducho.fem.skywars.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.skywars.SkyPlayer;
import com.cadiducho.fem.skywars.SkyWars;
import com.cadiducho.fem.skywars.manager.GameState;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class GameTask extends BukkitRunnable {

    private final SkyWars plugin;
    public static GameTask instance;
    
    public GameTask(SkyWars plugin) {
        this.plugin = plugin;
    }

    private static int count = 0;
    private boolean timePlayed = false;

    @Override
    public void run() {
        instance = this;
        checkWinner();
        noPlayers();
        if (timePlayed) {
            plugin.getGm().getPlayersInGame().stream().forEach(players -> {
                plugin.getMsg().sendActionBar(players, "&a&lTiempo jugado: " + count);
            });
        }
        if (count == 0) {
            for (final Player p : plugin.getGm().getPlayersInGame()) {
                new Title("", "&e¡Elimina al resto de enemigos!", 1, 2, 1).send(p);
                p.playSound(p.getLocation(), Sound.EXPLODE, 1F, 1F);
                p.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
                
                final SkyPlayer sp = SkyWars.getPlayer(p);
                sp.setCleanPlayer(GameMode.SURVIVAL);
                sp.setGameScoreboard();
                HashMap<Integer, Integer> plays = sp.getUserData().getPlays();
                plays.replace(7, plays.get(7) + 1);
                sp.getUserData().setPlays(plays);
                sp.save();
            }
            plugin.getAm().getIslas().forEach(i -> i.destroyCapsule());
            timePlayed = true;
        }
        if (count == 5) { //Desactivar a los 5 segundos la inmunidad por caidas
            plugin.getGm().setDañoEnCaida(true);
        }

        ++count;
    }
    
    public void checkWinner() {
        if (plugin.getGm().getPlayersInGame().size() <= 1) {
            Player winner = plugin.getGm().getPlayersInGame().get(0);
            plugin.getGm().getPlayersInGame().forEach(p -> {
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                new Title("&a" + p.getName(), "&aha ganado la partida!", 1, 2, 1).send(winner);
            });
            plugin.getMsg().sendBroadcast(winner.getDisplayName() + " ha ganado la partida!");
            
            final SkyPlayer sp = SkyWars.getPlayer(winner);
            HashMap<Integer, Integer> wins = sp.getUserData().getWins();
            wins.replace(7, wins.get(7) + 1);
            sp.getUserData().setWins(wins);
            sp.save();
            end();
            cancel();
        }
    }
    
    public void end() {
        GameState.state = GameState.ENDING;

        //Cuenta atrás para envio a los lobbies y cierre del server
        //Iniciar hilo del juego
        new ShutdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
    }

    public static int getTimeLeft() {
        return count;
    }

    private void noPlayers(){
        if (plugin.getGm().getPlayersInGame().isEmpty()){
            plugin.getServer().shutdown();
        }
    }
}
