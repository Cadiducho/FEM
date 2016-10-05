package com.cadiducho.fem.color.task;

import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.color.manager.GameState;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.Title;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final DyeOrDie plugin;
    int ticksforround = 200;
    int tickstilendofround = ticksforround;
    int status = 1;
    int colorround = 0;
    Random rand = new Random();
    
    public GameTask(DyeOrDie instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        checkWinner();
        switch (status) {
            case 0:
                //Terminar ronda y recomenzar el bucle si quedan jugadores
                plugin.getAm().roundEnded();
                status += 1;
                colorround = 0;
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 15L);
                break;
            case 1:
                //Comenzar la ronda
                status += 1;
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 45L);
                break;
            case 2:
                //Remplazar colores del suelo y del inventario, crear confusión
                plugin.getAm().replaceFloor();
                plugin.getAm().spinColors(false);
                colorround += 1;
                status += 1;

                plugin.getMsg().sendBroadcast(DyeOrDie.colorize("D Y E  O R  D I E"));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 7L);
                break;
            case 3:
                //Cambiar 6 veces de color en el inventario (el mismo). Cambiar colores del suelo
                plugin.getAm().spinColors(true);
                plugin.getAm().shuffleMats();
                colorround += 1;
                
                switch (colorround) {
                    case 2:
                        plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F));
                        plugin.getMsg().sendBroadcast("&a3");
                        break;
                    case 4:
                        plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F));
                        plugin.getMsg().sendBroadcast("&a2");
                        break;
                    case 6:
                        plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F));
                        plugin.getMsg().sendBroadcast("&a1");
                        break;
                }
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 7L);
                if (colorround > 6) {
                    status += 1;
                }
                break;
            case 4:
                //Comenzar ronda. Calcular tiempos hasta el final de la ronda
                plugin.getAm().startRound();
                if (plugin.getAm().getRound() > 1) {
                    ticksforround = ((int) (ticksforround * (1.0D - plugin.getAm().getDecayvalue())));
                }
                tickstilendofround = ticksforround;
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 5L);
                status += 1;
                break;
            case 5:
                tickstilendofround -= 5;
                plugin.getAm().setTimeLeft(tickstilendofround);
                if (tickstilendofround < 5) {
                    status = 0;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, tickstilendofround);
                } else {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 5L);
                }
                break;
            default:
                break;
        }
    }
    
    public void checkWinner() {
        if (plugin.getGm().getPlayersInGame().size() <= 1) {
            Player winner = plugin.getGm().getPlayersInGame().get(0);
            new Title("&b&l¡Has ganado!", "Has llegado hasta la ronda " + plugin.getAm().getRound(), 1, 2, 1).send(winner);
            plugin.getMsg().sendBroadcast(winner.getName() + " ha ganado llegando hasta la ronda " + plugin.getAm().getRound() + "!");
            HashMap<Integer, Integer> wins = FEMServer.getUser(winner).getUserData().getWins();
            wins.replace(2, wins.get(2) + 1);
            FEMServer.getUser(winner).getUserData().setWins(wins);
            FEMServer.getUser(winner).save();
            
            end();
            plugin.getServer().getScheduler().cancelTask(getTaskId());
        }
    }
    
    public void end() {
        GameState.state = GameState.ENDING;
        plugin.getAm().replaceFloor();
        //Cuenta atrás para envio a los lobbies y cierre del server
        //Iniciar hilo del juego
        new ShutdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
    }
}
