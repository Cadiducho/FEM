package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.GemPlayer;
import com.cadiducho.fem.gem.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class GameTask extends BukkitRunnable {

    private final GemHunters plugin;
    public static GameTask instance;
    
    public GameTask(GemHunters instance) {
        plugin = instance;
    }

    private static int count = 300;

    @Override
    public void run() {
        instance = this;
        
        switch (count) {
            case 300:
                plugin.getAm().muro(plugin.getServer().getWorlds().get(0));
                plugin.getGm().getPlayersInGame().stream().forEach(p -> p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f));
                break;
            case 30:
                plugin.getMsg().sendBroadcast("&7Sólo quedan 30 segundos!");
                plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                    Title.sendTitle(p, 1, 7, 1, "&b&lSólo quedan 30 segundos", "¡Dáte prisa!");
                });
                break;
            case 0:
                if (checkMinWinner() == null) {
                    plugin.getMsg().sendBroadcast("¡Empate! ¡No hay ningún ganador!");
                    plugin.getGm().getPlayersInGame().stream().forEach(p -> Title.sendTitle(p, 1, 7, 1, "&b&lEMPATE", ""));
                }
                end();
                break;
            default:
                break;
        }

        --count;
        plugin.getGm().getPlayersInGame().stream().forEach(pl -> pl.setLevel(count));
        noPlayers();
    }
    
    public Team checkMinWinner() {
        Team winner = null;
        Team a = plugin.getTm().azul;
        Team m = plugin.getTm().rojo;
        
        if (plugin.getTm().getPuntos(a) > plugin.getTm().getPuntos(m)) winner = a;
        if (plugin.getTm().getPuntos(a) < plugin.getTm().getPuntos(m)) winner = m;
        
        if (winner == null) return null;
        
        //Hay un ganador
        plugin.getMsg().sendBroadcast("Ha ganado el equipo " + winner.getPrefix() + winner.getDisplayName());
        
        Team loser = plugin.getTm().getOpositeTeam(winner);
        for (Player p : plugin.getTm().getJugadores().get(winner)) {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            Title.sendTitle(p, 1, 7, 1, "&a&lVICTORIA", "¡Tu equipo ha ganado :D!");
            
            final GemPlayer gp = GemHunters.getPlayer(p);
            gp.getUserData().addWins(GameID.GEMHUNTERS);
            gp.save();
        }
        plugin.getTm().getJugadores().get(loser).forEach(p -> {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            Title.sendTitle(p, 1, 7,1, "&c&lDERROTA", "¡Tu equipo ha perdido :C!");
        });
        return winner;
    }
    
    public static void end() {
        GameState.state = GameState.ENDING;

        //Cuenta atrás para envio a los lobbies y cierre del server
        //Iniciar hilo del juego
        new ShutdownTask(GemHunters.getInstance()).runTaskTimer(GemHunters.getInstance(), 1l, 20l);
        if (GameTask.instance != null) GameTask.instance.cancel();
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
