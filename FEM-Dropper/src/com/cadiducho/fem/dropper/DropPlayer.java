package com.cadiducho.fem.dropper;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import java.util.Map;
import org.bukkit.GameMode;

import java.util.UUID;
import org.bukkit.Material;

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

        getUserData().getDropper().forEach((m, v) -> { 
            getPlayer().getInventory().addItem(ItemUtil.createItem(Material.DIAMOND, v, "&d" + m, "&a" + v + " &eveces superado"));
        });
    }

    public void sendToDropper(String id) {
        System.out.println("Teleportando a " +id);
        getPlayer().teleport(plugin.getServer().getWorld(id).getSpawnLocation());
        sendMessage("Estás en el mapa "+id);
        sendMessage("&a¡Suerte!");
    }

    public void endMap() {
        String map = getPlayer().getWorld().getName();
        sendMessage("&aHas ganado en el mapa &e" + map + "&a!");
        getPlayer().teleport(Dropper.getInstance().getAm().getLobby());
        setLobbyInventory();

        if (getUserData().getDropper().get(map) != null) {
            getUserData().getDropper().replace(map, getUserData().getDropper().get(map) + 1);
        } else {
            getUserData().getDropper().put(map, 1);
        }
        save();
    }
}
