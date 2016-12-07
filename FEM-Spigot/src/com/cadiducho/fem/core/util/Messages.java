package com.cadiducho.fem.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_11_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Messages {

    private static String nmsver;
    
    private final Plugin plugin;
    private final String prefix;

    public Messages(Plugin instance, String prefix) {
        this.plugin = instance;
        this.prefix = Metodos.colorizar(prefix);
        
        nmsver = plugin.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
    }

    public void sendMessage(Player player, String msg) {
        player.sendMessage(prefix + " " + Metodos.colorizar(msg));
    }

    public void sendColorMessage(Player player, String msg) {
        player.sendMessage(Metodos.colorizar(msg));
    }
    
    public void sendBroadcast(String msg){
        plugin.getServer().broadcastMessage(prefix + " " + Metodos.colorizar(msg));
    }
    
    public void sendEmptyLine(){ 
        plugin.getServer().broadcastMessage(""); 
    }

    public void sendActionBar(Player p, String msg) { 
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer 
                .a("{\"text\": \"" + Metodos.colorizar(msg) + "\"}"); 
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2); 
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc); 
    }

    public void sendHeaderAndFooter(Player p, String head, String foot) {
        CraftPlayer craftplayer = (CraftPlayer) p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent header = IChatBaseComponent.ChatSerializer
                .a("{'color': '', 'text': '" + Metodos.colorizar(head) + "'}");
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer
                .a("{'color': '', 'text': '" + Metodos.colorizar(foot) + "'}");
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
}
