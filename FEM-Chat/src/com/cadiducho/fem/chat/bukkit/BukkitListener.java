package com.cadiducho.fem.chat.bukkit;

import com.cadiducho.fem.chat.FEMChat;
import com.cadiducho.fem.core.api.FEMServer;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BukkitListener implements Listener {
        
    private final BukkitPlugin bukkitPlugin;
    
    public BukkitListener(BukkitPlugin instance) {
        bukkitPlugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        
        String texto = FEMChat.SINTAXIS + FEMChat.CHAR + event.getMessage();
        try {
            processChatMessage(event.getPlayer(), texto);
        } catch (IOException ex) {
            event.getPlayer().sendMessage(c("&cError interno procesando el mensaje"));
            ex.printStackTrace();
        }
        event.setCancelled(true);
    }
    
    private void processChatMessage(Player player, String text) throws IOException {
        if (text.contains("%group%")){
            text = text.replace("%"  + "group%", c(FEMServer.getUser(player).getUserData().getGrupo().name()));
        }
        if (text.contains("%prefix%")){
            text = text.replace("%" + "prefix%", c(FEMServer.getUser(player).getUserData().getGrupo().name()));
        }
        if (text.contains("%suffix%")){
            text = text.replace("%" + "suffix%", c(""));
        }
        if (text.contains("%displayName%")){
            text = text.replace("%" + "displayName%", c(player.getDisplayName()));
        }
        
        if (player.hasPermission("FEMChat.colores")) {
            text = c(text);
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
            
        out.writeUTF(FEMChat.MAIN_SUBCHANNEL);
        out.writeUTF(text);
        out.flush();
        out.close();
        
        player.sendPluginMessage(bukkitPlugin, FEMChat.MAIN_CHANNEL, b.toByteArray());
    }
    
    String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
