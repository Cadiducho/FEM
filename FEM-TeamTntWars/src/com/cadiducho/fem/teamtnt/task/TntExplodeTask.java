package com.cadiducho.fem.teamtnt.task;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.teamtnt.TeamTntWars;
import com.cadiducho.fem.teamtnt.TntIsland;
import com.cadiducho.fem.teamtnt.TntPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class TntExplodeTask extends BukkitRunnable {
    
    private final TntIsland isla;
    private final UUID exploder;
    
    public TntExplodeTask(TntIsland instance, UUID p) {
        isla = instance;
        exploder = p;
    }
    
    private int count = 10;
    
    @Override
    public void run() {
        final TntPlayer tpExploder = TeamTntWars.getPlayer(TeamTntWars.getInstance().getServer().getOfflinePlayer(exploder));
        final Team team = isla.getTeam();

        switch (count){
            case 10:
                tpExploder.getUserData().setTeamTntPuestas(tpExploder.getUserData().getTeamTntPuestas() + 1);
                tpExploder.save();
                tpExploder.sendMessage("&aHas puesto la TNT y explotará en 10 segundos");
                team.getEntries().forEach(e -> Bukkit.getPlayer(e).sendMessage(Metodos.colorizar("&cTu isla explotará en 10 segundos si no lo evitas")));
                break;
            case 4:
            case 3:
            case 2:
            case 1:
                TeamTntWars.getInstance().getGm().getPlayersInGame().forEach(p -> p.playSound(isla.getBedrockCore().getLocation(), Sound.ANVIL_LAND, (9F + count), 1F));
                team.getEntries().forEach(e -> Bukkit.getPlayer(e).sendMessage(Metodos.colorizar("&c¡Tu isla explotará en " + count + " segundo" + (count == 1 ? "" : "s") + " si no lo evitas!")));
                break;
            case 0:
                isla.explode();
                tpExploder.getUserData().setTeamTntExplotadas(tpExploder.getUserData().getTeamTntExplotadas() + 1);
                tpExploder.save();
                TeamTntWars.getInstance().getMsg().sendBroadcast("&eLa isla de " + team.getPrefix() + team.getDisplayName() + " ha sido destruida por " + tpExploder.getName());
                tpExploder.getUserData().setCoins(tpExploder.getUserData().getCoins() + 1);
                tpExploder.save();
                cancel();
                break;
        }
        count--;
    }
    
}
