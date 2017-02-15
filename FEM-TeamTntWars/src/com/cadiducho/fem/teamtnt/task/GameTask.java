package com.cadiducho.fem.teamtnt.task;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.teamtnt.TeamTntWars;
import com.cadiducho.fem.teamtnt.TntIsland;
import com.cadiducho.fem.teamtnt.TntPlayer;
import com.cadiducho.fem.teamtnt.manager.GameState;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class GameTask extends BukkitRunnable {

    private final TeamTntWars plugin;
    public static GameTask instance;
    
    public GameTask(TeamTntWars instance) {
        plugin = instance;
    }

    private static int count = 0;

    @Override
    public void run() {
        instance = this;
        checkWinner();
        noPlayers();
        plugin.getGm().getPlayersInGame().stream()
                .filter(p -> !TeamTntWars.getPlayer(p).isRespawning())
                .forEach(p -> plugin.getMsg().sendActionBar(p, "&a&lTiempo jugado: " + count)
        );


        switch (count){
            case 0:
                plugin.getGm().getPlayersInGame().forEach(p -> {
                    System.out.println("getIsland de " + p.getName() + " (" + plugin.getTm().getTeam(p).getDisplayName() + ")");
                    Team t = plugin.getTm().getTeam(p);
                    TntIsland isla = TntIsland.getIsland(t);
                    Title.sendTitle(p, 1, 5, 1, "", isla.getColor() + "¡Destruye el resto de islas!");
                    p.playSound(p.getLocation(), Sound.EXPLODE, 1F, 1F);
                    p.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());

                    final TntPlayer tp = TeamTntWars.getPlayer(p);
                    tp.setCleanPlayer(GameMode.SURVIVAL);
                    tp.setGameScoreboard();
                    tp.getUserData().addPlay(GameID.TEAMTNT);
                    tp.save();


                    p.sendMessage(Metodos.colorizar(t.getPrefix() + "Estás en el equipo a " + plugin.getTm().getTeam(p).getDisplayName()));
                });
                plugin.getAm().getIslas().forEach(i -> i.destroyCapsule());
                break;
            case 5: //Desactivar a los 5 segundos la inmunidad por caidas
                plugin.getGm().setDañoEnCaida(true);
                plugin.getAm().getGeneradores().forEach(gen -> gen.init());
                break;
        }
        ++count;
    }
    
    public void checkWinner() {
        if (plugin.getGm().getPlayersInGame().size() <= 1) {
            Player winner = plugin.getGm().getPlayersInGame().get(0);
            Team team = plugin.getTm().getTeam(winner);

            for (Player p : plugin.getGm().getPlayersInGame()) {
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                Title.sendTitle(winner, 1, 7, 1, team.getPrefix() + team.getDisplayName(), " &aha ganado la partida!");
            }
            plugin.getMsg().sendBroadcast("El equipo " + team.getPrefix() + team.getDisplayName() + "&r ha ganado la partida!");

            team.getEntries().forEach(s -> {
                TntPlayer tp = TeamTntWars.getPlayer(plugin.getServer().getPlayerExact(s));
                
                tp.getUserData().setCoins(tp.getUserData().getCoins() + 5);
                tp.getUserData().addWin(GameID.TEAMTNT);
                tp.save();
            });
            end();
            cancel();
        }
    }

    private void noPlayers(){
        if (plugin.getGm().getPlayersInGame().isEmpty()){
            plugin.getServer().shutdown();
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
}
