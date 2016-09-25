package com.cadiducho.fem.lucky.utils;

import com.cadiducho.fem.lucky.LuckyGladiators;
import java.lang.reflect.Field;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Messages {

    private final LuckyGladiators plugin;

    public Messages(LuckyGladiators instance) {
        plugin = instance;
    }

    private String prefix;

    public void init() {
        prefix = ChatColor.translateAlternateColorCodes('&', "&eLucky&aWarriors");
    }

    public void sendMessage(Player player, String msg) {
        player.sendMessage(prefix + " " + ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void sendColorMessage(Player player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    
    public void sendBroadcast(String msg){
        plugin.getServer().broadcastMessage(prefix + " " + ChatColor.translateAlternateColorCodes('&', msg));
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

    public String getPrefix() {
        return prefix;
    }
    
    public static String colorizar(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
