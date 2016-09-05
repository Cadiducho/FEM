package com.cadiducho.fem.ovejas.manager;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.ovejas.SheepQuest;
import com.cadiducho.fem.ovejas.manager.util.Area;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {

    private final SheepQuest plugin;

    public TeamManager(SheepQuest instance) {
        plugin = instance;
    }

    public Scoreboard board;
    public Team rojo;
    public Team amarillo;
    public Team verde;
    public Team azul;

    public HashMap<Team, ArrayList<Sheep>> puntos = new HashMap<>();
    @Getter private final HashMap<Team, Area> areas = new HashMap<>();
    @Getter private List<Team> teams;

    public void initTeams() {
        board = plugin.getServer().getScoreboardManager().getMainScoreboard();

        rojo = board.getTeam("1Sheep_rojo") == null ? board.registerNewTeam("1Sheep_rojo") : board.getTeam("1Sheep_rojo");
        amarillo = board.getTeam("2Sheep_ama") == null ? board.registerNewTeam("2Sheep_ama") : board.getTeam("2Sheep_ama");
        verde = board.getTeam("3Sheep_verde") == null ? board.registerNewTeam("3Sheep_verde") : board.getTeam("3Sheep_verde");
        azul = board.getTeam("4Sheep_azul") == null ? board.registerNewTeam("4Sheep_azul") : board.getTeam("4Sheep_azul");

        rojo.setPrefix("§c");
        amarillo.setPrefix("§e");
        verde.setPrefix("§a");
        azul.setPrefix("§b");
        
        rojo.setDisplayName("Rojo");
        amarillo.setDisplayName("Amarillo");
        verde.setDisplayName("Verde");
        azul.setDisplayName("Azul");
        
        rojo.setAllowFriendlyFire(false);
        amarillo.setAllowFriendlyFire(false);
        verde.setAllowFriendlyFire(false);
        azul.setAllowFriendlyFire(false);
        
        puntos.put(rojo, new ArrayList<>(0));
        puntos.put(amarillo, new ArrayList<>());
        puntos.put(verde, new ArrayList<>());
        puntos.put(azul, new ArrayList<>());
        
        int radio = plugin.getConfig().getInt("SheepQuest.Arena.radio");
        areas.put(rojo, new Area(Metodos.stringToLocation(plugin.getConfig().getString("SheepQuest.Arena.Spawn.rojo")), radio));
        areas.put(amarillo, new Area(Metodos.stringToLocation(plugin.getConfig().getString("SheepQuest.Arena.Spawn.amarillo")), radio));
        areas.put(verde, new Area(Metodos.stringToLocation(plugin.getConfig().getString("SheepQuest.Arena.Spawn.verde")), radio));
        areas.put(azul, new Area(Metodos.stringToLocation(plugin.getConfig().getString("SheepQuest.Arena.Spawn.azul")), radio));
        
        teams = Arrays.asList(rojo, amarillo, verde, azul);
    }

    public int getPuntos(Team t) {
        return puntos.get(t).size();
    }
    
    public void tryAddPunto(Team t, Sheep sh) {
        ArrayList<Sheep> array = puntos.get(t);
        if (array == null) {
            System.out.println("Array null, intentamos fixear...");
            array = new ArrayList<>();
        }
        
        if (array.contains(sh)) {
            System.out.println("Oveja duplicada");
            return;
        } //No duplicar ovejas
        
        puntos.remove(t);
        array.add(sh);
        puntos.put(t, array);
        
        plugin.getMsg().sendBroadcast("El equipo " + t.getDisplayName() + " ha conseguido un punto");
    }
    
    public void tryRemovePunto(Sheep sh) {
        ArrayList<Sheep> array = new ArrayList<>();
        Team team = null;
        for (Team t : teams) {
            if (puntos.get(t).contains(sh)) { 
                array = puntos.get(t);
                team = t;
            }
        }
        
        if (team == null || array.isEmpty()) {
            System.out.println("vacio...");
            return;
        }
        
        if (array.contains(sh)) {
            array.remove(sh);
            puntos.remove(team);
            puntos.put(team, array);
            plugin.getMsg().sendBroadcast("El equipo " + team.getDisplayName() + " ha perdido un punto");
        }
    }
    
    public Team getTeam(Player pl) {
        return board.getEntryTeam(pl.getName());
    }
    
    public Color getColor(Player pl) {
        switch (getTeam(pl).getPrefix()) {
            case "§c":
                return Color.RED;
            case "§e":
                return Color.YELLOW;
            case "§a":
                return Color.GREEN;
            case "§b":
                return Color.AQUA;
        }
        return Color.PURPLE;
    }
    
    public void cleanTeams() {
        for (Team t : teams) {
            for (String str : t.getEntries()) {
                t.removeEntry(str);
            }
        }  
    }
    
    public void drawTeams(ArrayList<Player> pls) {
        ArrayList<Player> clon = (ArrayList<Player>) pls.clone();
        
        while (clon.size() > 0) {
            for (Team t : teams) {
                if (clon.size() <= 0) break;
                Player str = clon.get(clon.size() - 1);
                clon.remove(str);
                t.addEntry(str.getName());
                str.sendMessage("Has sido asignado al equipo " + t.getDisplayName());
            }
        }
    }
    
    public Team checkIfIsInArea(Location loc) {
        for (Entry<Team, Area> e : areas.entrySet()) {
            Area a = e.getValue();
            Team t = e.getKey();
            if (a.isOnArea(loc)) {
                return t;
            }
        }
        return null;
    }
}
