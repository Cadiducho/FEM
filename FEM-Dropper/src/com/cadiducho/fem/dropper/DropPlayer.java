package com.cadiducho.fem.dropper;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.FireworkAPI;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import com.cadiducho.fem.core.util.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

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
        getPlayer().getInventory().setItem(0, ItemUtil.createItem(Material.COMPASS, "&aVuelve al Lobby", "Te lleva al lobby principal"));
        getPlayer().getInventory().setItem(7, ItemUtil.createItem(Material.DIAMOND, "&aMapas superados"));
        getPlayer().getInventory().setItem(8, ItemUtil.createItem(Material.EMERALD, "&aTus insignias"));
        getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
    
    public void setMapInventory() {
        setCleanPlayer(GameMode.ADVENTURE);
        
        String world = getPlayer().getWorld().getName();
        getPlayer().getInventory().addItem(ItemUtil.createItem(Material.BED, "&aVuelve al Lobby de Dropper", "&aMapa actual: &e" + world));
        if (FEMCore.getInstance().getMysql().checkInsignia(this, world, false)) {
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

                board.text(2, "Mapa: §b" + id);
                board.text(0, "§cmc.undergames.es");

                if (getPlayer() != null) board.build(getPlayer());
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public void checkInsignia() {
        String world = getPlayer().getWorld().getName();
        if (FEMCore.getInstance().getMysql().checkInsignia(this, world, true)) {
            sendMessage("&a¡Ya tienes esa insignia!");
        } else {
            sendMessage("&a¡Has obtenido la insignia del mapa &e" + world + "&a!");
            getPlayer().playSound(getPlayer().getLocation(), Sound.LEVEL_UP, 1F, 1F);
            getPlayer().getInventory().addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + world));
            save();
            if (plugin.getConfig().getString("Dropper.mapas").length() == getUserData().getDropperInsignias().size()) {
                sendMessage("&e¡Has conseguido las insignias secretas de todos los mapas!");
                FireworkAPI.spawnFirework(getPlayer().getLocation(), FireworkEffect.Type.STAR, Color.AQUA, Color.PURPLE, 2);
                getPlayer().getInventory().addItem(ItemUtil.createItem(Material.NETHER_STAR, "&l&6Trofeo de Todos los mapas"));
            }
        }
    }

    public void endMap() {
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
