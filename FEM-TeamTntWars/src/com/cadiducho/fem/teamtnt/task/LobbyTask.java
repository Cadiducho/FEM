package com.cadiducho.fem.teamtnt.task;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.teamtnt.TeamTntWars;
import com.cadiducho.fem.teamtnt.TntPlayer;
import com.cadiducho.fem.teamtnt.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.GameMode;

public class LobbyTask extends BukkitRunnable {

    private final TeamTntWars plugin;

    public LobbyTask(TeamTntWars instance) {
        plugin = instance;
    }

    private int count = 50;

    @Override
    public void run() {
        //Comprobar si sigue habiendo suficientes jugadores o cancelar
        if (plugin.getGm().getPlayersInGame().size() < plugin.getAm().getMinPlayers()) {
            plugin.getGm().setCheckStart(true);
            plugin.getServer().getOnlinePlayers().forEach(pl ->  pl.setLevel(0));
            GameState.state = GameState.LOBBY;
            cancel();
            return;
        }
        
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&lEl juego empieza en: " + count);
        });
        if (count == 30) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en 30 segundos");
        } else if (count == 5) {
            GameState.state = GameState.GAME;
            plugin.getGm().getPlayersInGame().forEach(p -> {
                randomTeam(p);
                TeamTntWars.getPlayer(p).setCleanPlayer(GameMode.SURVIVAL);
                p.closeInventory();
            });
            plugin.getTm().getTeams().keySet().forEach(t -> plugin.getAm().teleport(t));
        } else if (count > 0 && count <= 4) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en &c" + count + " &7segundos"); 
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.NOTE_PLING, 1F, 1F));
        } else if (count == 0) { 
            plugin.getGm().setDañoEnCaida(false);

            new GameTask(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }

        --count;      
    }


    private void randomTeam(Player p){
        if (plugin.getTm().hasTeam(p)) return;

        List<Team> teams = new ArrayList<>(plugin.getTm().getTeams().keySet());
        Team t = teams.get(new Random().nextInt(teams.size()));

        if (!plugin.getTm().isFilled(t)){
            plugin.getTm().asingTeam(t, p);
            return;
        }
        randomTeam(p);
    }
}
