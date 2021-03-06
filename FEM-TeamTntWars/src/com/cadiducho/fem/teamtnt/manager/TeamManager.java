package com.cadiducho.fem.teamtnt.manager;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import com.cadiducho.fem.teamtnt.TeamTntWars;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class TeamManager {

    private final TeamTntWars plugin;

    public TeamManager(TeamTntWars instance) {
        plugin = instance;
    }

    @Getter ScoreboardUtil boardUtil;
    Scoreboard board;

    public Team rojo;
    public Team azul;
    public Team verde;
    public Team amarillo;
    public Team morado;

    public Team gris;

    public Team dead;

    @Getter private final HashMap<Team, Location> teams = new HashMap<>();

    public void initTeams() {
        boardUtil = new ScoreboardUtil(TeamTntWars.getPrefix(), "game");
        board = boardUtil.getScoreboard();
        cleanTeams();
        
        rojo = board.getTeam("1tnt_rojo") == null ? board.registerNewTeam("1tnt_rojo") : board.getTeam("1tnt_rojo");
        azul = board.getTeam("2tnt_azul") == null ? board.registerNewTeam("2tnt_azul") : board.getTeam("2tnt_azul");
        verde = board.getTeam("3tnt_verde") == null ? board.registerNewTeam("3tnt_verde") : board.getTeam("3tnt_verde");
        amarillo = board.getTeam("4tnt_amarillo") == null ? board.registerNewTeam("4tnt_amarillo") : board.getTeam("4tnt_amarillo");
        morado = board.getTeam("5tnt_morado") == null ? board.registerNewTeam("5tnt_morado") : board.getTeam("5tnt_morado");
        gris = board.getTeam("6tnt_gris") == null ? board.registerNewTeam("6tnt_gris") : board.getTeam("6tnt_gris");
        dead = board.getTeam("7tnt_dead") == null ? board.registerNewTeam("7tnt_dead") : board.getTeam("7tnt_dead");

        rojo.setPrefix("§c");
        rojo.setDisplayName("§cRojo");
        azul.setPrefix("§b");
        azul.setDisplayName("§bAzul");
        verde.setPrefix("§a");
        verde.setDisplayName("§aVerde");
        amarillo.setPrefix("§e");
        amarillo.setDisplayName("§eAmarillo");
        morado.setPrefix("§d");
        morado.setDisplayName("§dMorado");
        gris.setDisplayName("§8Gris");
        dead.setPrefix("§o");
        
        rojo.setAllowFriendlyFire(false);
        azul.setAllowFriendlyFire(false);
        verde.setAllowFriendlyFire(false);
        amarillo.setAllowFriendlyFire(false);
        morado.setAllowFriendlyFire(false);

        gris.setAllowFriendlyFire(false);
        
        teams.put(rojo, Metodos.stringToLocation(plugin.getConfig().getString("TeamTntWars.Arena.Spawn.rojo")));
        teams.put(azul, Metodos.stringToLocation(plugin.getConfig().getString("TeamTntWars.Arena.Spawn.azul")));
        teams.put(verde, Metodos.stringToLocation(plugin.getConfig().getString("TeamTntWars.Arena.Spawn.verde")));
        teams.put(amarillo, Metodos.stringToLocation(plugin.getConfig().getString("TeamTntWars.Arena.Spawn.amarillo")));
        teams.put(morado, Metodos.stringToLocation(plugin.getConfig().getString("TeamTntWars.Arena.Spawn.morado")));
        teams.put(gris, Metodos.stringToLocation(plugin.getConfig().getString("TeamTntWars.Arena.Spawn.gris")));
    }

    
    public Team getTeam(Player p) {
        return board.getEntryTeam(p.getName());
    }

    public void deadPlayer(Player p){
        removeTeam(p);
        asingTeam(dead, p);
    }

    public void removeTeam(Player p){
        if (getTeam(p) == null) return;
        getTeam(p).removeEntry(p.getName());
    }

    public void asingTeam(Team team, Player p) {
        if (isFilled(team)){
            p.sendMessage(ChatColor.RED + "El equipo " + team.getPrefix() + team.getDisplayName() + ChatColor.RED + " está lleno");
            return;
        }
        teams.keySet().forEach(t -> {
            if (t.getEntries().isEmpty()){
                System.out.println(p.getName() + " asignado a " + t.getDisplayName());
                team.addEntry(p.getName());
                p.setScoreboard(board);

                p.playSound(p.getLocation(), Sound.CLICK, 1F, 1F);
                p.sendMessage(ChatColor.GREEN + "Asignado al equipo " + t.getPrefix() + t.getDisplayName() + ChatColor.GREEN + " para balancear el juego");
                return;
            }
        });

        System.out.println(p.getName() + " asignado a " + team.getDisplayName());
        team.addEntry(p.getName());
        p.setScoreboard(board);

        p.playSound(p.getLocation(), Sound.CLICK, 1F, 1F);
        p.sendMessage(ChatColor.GREEN + "Asignado al equipo " + team.getPrefix() + team.getDisplayName());
    }

    public boolean hasTeam(Player p){
        for (Team t : teams.keySet()) {
            if (t.getEntries().contains(p.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isFilled(Team team){
        int players = Bukkit.getOnlinePlayers().size();
        int pl = 4;

        if (players >= 5) pl = 5;

        return team != dead && team.getEntries().size() == pl;
    }

    public void cleanTeams() {
        for (Team t : teams.keySet()) {
            for (String str : t.getEntries()) {
                t.removeEntry(str);
            }
        }  
    }
}
