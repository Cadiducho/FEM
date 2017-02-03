package com.cadiducho.fem.dropper;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.*;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class DropPlayer extends FEMUser {

    private final Dropper plugin = Dropper.getInstance();

    public DropPlayer(UUID id) {
        super(id);
    }

    public void setCleanPlayer(GameMode gameMode) {
        getPlayer().setHealth(getPlayer().getMaxHealth());
        getPlayer().setFoodLevel(20);
        getPlayer().setExp(0);
        getPlayer().setTotalExperience(0);
        getPlayer().setLevel(0);
        getPlayer().setFireTicks(0);
        getPlayer().getInventory().clear();
        getPlayer().getInventory().setArmorContents(null);
        getPlayer().setGameMode(gameMode);
        getPlayer().getActivePotionEffects().forEach(ef -> getPlayer().removePotionEffect(ef.getType()));
    }

    public void setLobbyInventory() {
        setCleanPlayer(GameMode.ADVENTURE);
        getPlayer().getInventory().setItem(0, ItemUtil.createItem(Material.COMPASS, "&aVuelve al Lobby", "Pulsa para volver al lobby principal de Undergames"));
        getPlayer().getInventory().setItem(7, ItemUtil.createItem(Material.DIAMOND, "&aMapas superados"));
        getPlayer().getInventory().setItem(8, ItemUtil.createItem(Material.EMERALD, "&aTus insignias"));
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> getPlayer().setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard()), 21L);
    }
    
    public void setMapInventory() {
        setCleanPlayer(GameMode.SURVIVAL);
        
        String world = getPlayer().getWorld().getName();
        getPlayer().getInventory().addItem(ItemUtil.createItem(Material.BED, "&aVuelve al Lobby de Dropper", "&aMapa actual: &e" + world));
        if (getUserData().getDropperInsignias().contains(world)) {
            if (plugin.getConfig().getString("Dropper.mapas").length() == getUserData().getDropperInsignias().size()) {
                getPlayer().getInventory().addItem(ItemUtil.createItem(Material.NETHER_STAR, "&l&6Trofeo de Todos los mapas"));
            }
            getPlayer().getInventory().addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + world));
        }
    }

    public void sendToDropper(String id) {
        getPlayer().teleport(Metodos.stringToLocation(plugin.getConfig().getString("Dropper.spawns." + id)));
        sendMessage("Estás en el mapa " + id);
        sendMessage("&a¡Suerte!");
        setGameScoreboard(id);
        getUserData().addPlay(GameID.DROPPER);
    }

    private void setGameScoreboard(String id) {
        ScoreboardUtil board = new ScoreboardUtil("§aDropper", "game");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                if (getPlayer().getWorld().getName().equals(plugin.getAm().getLobby().getWorld().getName())) cancel(); //No ejecutar en el lobby
    
                String map = getPlayer().getWorld().getName();
                int comple = (getUserData().getDropper().get(map) != null) ? getUserData().getDropper().get(map) : 0;
 
                board.text(3, "§dMapa: §b" + id);
                board.text(2, (comple > 0) ? "§a✔ Mapa completado (§e" + comple + "§a)" : "§c✘ Mapa no completado");
                board.text(1, (getUserData().getDropperInsignias().contains(map)) ? "§a✔ Insignia conseguida" : "§c✘ No tienes la insignia");
                board.text(0, "§cmc.undergames.es");

                if (getPlayer() != null && !getPlayer().getWorld().getName().equals(plugin.getAm().getLobby().getWorld().getName())) board.build(getPlayer());
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public void checkInsignia() {
        String world = getPlayer().getWorld().getName();
        if (FEMCore.getInstance().getMysql().checkInsignia(this, world)) {
            sendMessage("&a¡Ya tienes esa insignia!");
        } else {
            sendMessage("&a¡Has obtenido la insignia del mapa &e" + world + "&a!");
            getPlayer().playSound(getPlayer().getLocation(), Sound.LEVEL_UP, 1F, 1F);
            getPlayer().getInventory().addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + world));
            if (plugin.getConfig().getString("Dropper.mapas").length() == getUserData().getDropperInsignias().size()) {
                sendMessage("&e¡Has conseguido las insignias secretas de todos los mapas!");
                FireworkAPI.spawnFirework(getPlayer().getLocation(), FireworkEffect.Type.STAR, Color.AQUA, Color.PURPLE, 2);
                getPlayer().getInventory().addItem(ItemUtil.createItem(Material.NETHER_STAR, "&l&6Trofeo de Todos los mapas"));
            }
        }
    }

    public void endMap() {
        getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        String map = getPlayer().getWorld().getName();
        sendMessage("&aHas ganado en el mapa &e" + map + "&a!");
        Title.sendTitle(getPlayer(), 1, 5, 1, "&a" + map, "&e¡Mapa completado!");
        
        if (getUserData().getDropper().get(map) != null) {
            getUserData().getDropper().replace(map, getUserData().getDropper().get(map) + 1);
        } else {
            getUserData().getDropper().put(map, 1);
        }
        getUserData().addWin(GameID.DROPPER);
        save();
        getPlayer().teleport(Dropper.getInstance().getAm().getLobby());
        setLobbyInventory();
    }
}
