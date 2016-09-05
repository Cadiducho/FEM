package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    public Main plugin;

    public PlayerInteract(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (GameState.state == GameState.LOBBY) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (e.getItem() != null && e.getItem().getType().equals(Material.SLIME_BALL)) {
                    e.getPlayer().sendMessage("§bDonkey§cCraft §8» §7Enviando al lobby");
                    plugin.up.sendToServer(e.getPlayer(), "LOBBYUHC001");
                }
                if (e.getItem() != null && e.getItem().getType().equals(Material.PAPER)) {
                    plugin.iv.openInventory(e.getPlayer());
                }
                if (e.getItem() != null && e.getItem().getType().equals(Material.DIAMOND)) {
                    if(e.getPlayer().hasPermission("DonkeyUHC.VIP")){
                        plugin.ivip.openVIPMenu(e.getPlayer());
                    } else {
                        plugin.msg.sendMessage(e.getPlayer(), "&c&lNecesitas ser &a&lVIP");
                    }
                }
            }
        }
    }
}
