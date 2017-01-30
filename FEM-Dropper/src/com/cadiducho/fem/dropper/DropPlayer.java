package com.cadiducho.fem.dropper;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.Title;
import org.bukkit.GameMode;

import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.Sound;

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
        setCleanPlayer(GameMode.SURVIVAL);

        getUserData().getDropperInsignias().forEach(m -> { 
            getPlayer().getInventory().addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + m));
        });
        getUserData().getDropper().forEach((m, v) -> { 
            getPlayer().getInventory().addItem(ItemUtil.createItem(Material.DIAMOND, v, "&d" + m, "&a" + v + " &eveces superado"));
        });
    }
    
    public void setMapInventory() {
        setCleanPlayer(GameMode.SURVIVAL);
        
        String world = getPlayer().getWorld().getName();
        getPlayer().getInventory().addItem(ItemUtil.createItem(Material.BED, "&aVuelve al Lobby de Dropper", "&aMapa actual: &e" + world));
        if (FEMCore.getInstance().getMysql().checkInsignea(this, world, false)) {
            getPlayer().getInventory().addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + world));
        }
    }

    public void sendToDropper(String id) {
        getPlayer().teleport(Metodos.stringToLocation(plugin.getConfig().getString("Dropper.spawns." + id)));
        sendMessage("Estás en el mapa " + id);
        sendMessage("&a¡Suerte!");
    }
    
    public void checkInsignea() {
        String world = getPlayer().getWorld().getName();
        if (FEMCore.getInstance().getMysql().checkInsignea(this, world, true)) {
            sendMessage("&a¡Ya tienes esa insignea!");
        } else {
            sendMessage("&a¡Has obtenido la insignea del mapa &e" + world + "&a!");
            getPlayer().playSound(getPlayer().getLocation(), Sound.LEVEL_UP, 1F, 1F);
            getPlayer().getInventory().addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + world));
            save();
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
        save();
        getPlayer().teleport(Dropper.getInstance().getAm().getLobby());
        setLobbyInventory();
    }
}
