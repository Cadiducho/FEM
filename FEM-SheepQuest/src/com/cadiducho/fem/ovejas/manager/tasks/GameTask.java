package com.cadiducho.fem.ovejas.manager.tasks;

import com.cadiducho.fem.ovejas.SheepPlayer;
import com.cadiducho.fem.ovejas.SheepQuest;
import com.cadiducho.fem.ovejas.manager.GameState;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

public class GameTask extends BukkitRunnable {

    private final SheepQuest plugin;
    private final BukkitTask taskOvejas;
    
    public GameTask(SheepQuest instance, BukkitTask task) {
        plugin = instance;
        taskOvejas = task;
    }

    private static int count = 180;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
        
        if (count == 180) {
            plugin.getGm().getPlayersInGame().forEach((players) -> {
                players.playSound(players.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            });
        } else if (count == 30) {
            plugin.getMsg().sendBroadcast("&7Solo quedan 30 segundos! ");

        } else if (count == 0) {
            taskOvejas.cancel();
            GameState.state = GameState.ENDING;

            //CheckWinner
            plugin.getMsg().sendBroadcast("Gandor: " + getWinnerTeam().getDisplayName());

            this.cancel();
        }

        --count;
        for (Player p : plugin.getGm().getPlayersInGame()) {
            Team t = plugin.getTm().checkIfIsInArea(p.getLocation());
            if (t != null) {
                SheepPlayer sp = SheepQuest.getPlayer(p);
                sp.removeAllSheeps();
            }
        }
        
        for (LivingEntity e : plugin.getServer().getWorld("world").getLivingEntities()) {
            if (e instanceof Sheep) {
                Sheep sheep = (Sheep) e;
                Team t = plugin.getTm().checkIfIsInArea(sheep.getLocation());
                if (t != null) {
                    System.out.println("Intento añadir punto...");
                    plugin.getTm().tryAddPunto(t, sheep);
                } else {
                    System.out.println("Intento eliminar punto...");
                    plugin.getTm().tryRemovePunto(sheep);
                }
            }
        }
    }

    public Team getWinnerTeam() {
        ArrayList<Integer> list = new ArrayList(Arrays.asList(plugin.getTm().getPuntos(plugin.getTm().rojo),
                plugin.getTm().getPuntos(plugin.getTm().amarillo),
                plugin.getTm().getPuntos(plugin.getTm().verde),
                plugin.getTm().getPuntos(plugin.getTm().azul)));
        int max = list.get(0);

        for (Integer i : list) {
            if (i > max)  max = i;
        }
        
        for (Team teams : plugin.getTm().getTeams()) {
            if (plugin.getTm().getPuntos(teams) == max) return teams;
        }
        return null;
    }

    public static int getTimeLeft() {
        return count;
    }
}
