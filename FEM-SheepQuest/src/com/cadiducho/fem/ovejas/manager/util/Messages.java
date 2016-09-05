package com.cadiducho.fem.ovejas.manager.util;

import com.cadiducho.fem.ovejas.SheepQuest;
import java.lang.reflect.Field;
import java.util.logging.Level;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Messages {

    private final SheepQuest plugin;

    public Messages(SheepQuest instance) {
        plugin = instance;
    }

    private String prefix;
    
    private final String ALERT_ICON = "\u26A0";

    public void init() {
        prefix = ChatColor.translateAlternateColorCodes('&', "&6Sheep&7Quest");
    }

    public void sendMessage(Player player, String msg) {
        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void sendColorMessage(Player player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));

    }
    
    public void sendBroadcast(String msg){
        plugin.getServer().broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', msg));
    }
    
    public void sendAlert(String msg){
        plugin.getServer().getOnlinePlayers().stream().map((players) -> {
            if(players.hasPermission("UHCRun.Admin")){
                sendMessage(players, "&e" + ALERT_ICON + " &c" + msg);
            }
            return players;
        }).forEach((_item) -> {
            plugin.getLogger().log(Level.INFO, msg);
        });
    }

    public void sendActionBar(Player p, String msg) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer
                .a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', msg) + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }

    public void sendHeaderAndFooter(Player p, String head, String foot) {
        CraftPlayer craftplayer = (CraftPlayer) p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent header = IChatBaseComponent.ChatSerializer
                .a("{'color': '', 'text': '" + ChatColor.translateAlternateColorCodes('&', head) + "'}");
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer
                .a("{'color': '', 'text': '" + ChatColor.translateAlternateColorCodes('&', foot) + "'}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, header);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footer);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
        }
        connection.sendPacket(packet);
    }

    public String getAlertIcon() {
        return ALERT_ICON;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public static String colorizar(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
