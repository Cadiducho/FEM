package com.cadiducho.fem.core.util;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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

    public void sendBroadcast(String msg) {
        plugin.getServer().broadcastMessage(prefix + " " + Metodos.colorizar(msg));
    }

    public void sendEmptyLine() {
        plugin.getServer().broadcastMessage("");
    }

    public void sendActionBar(Player p, String msg) {
        try {
            Constructor<?> constructor = Metodos.getNMSClass("PacketPlayOutChat").getConstructor(Metodos.getNMSClass("IChatBaseComponent"), byte.class);
            Object icbc = Metodos.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + Metodos.colorizar(msg) + "\"}");
            Object packet = constructor.newInstance(icbc, (byte) 2);

            ReflectionAPI.sendPacket(p, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendHeaderAndFooter(Player p, String headerText, String footerText) {
        try {
            Class chatSerializer = ReflectionAPI.getNmsClass("IChatBaseComponent$ChatSerializer");

            Object tabHeader = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{'text': '" + Metodos.colorizar(headerText) + "'}");
            Object tabFooter = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{'text': '" + Metodos.colorizar(footerText) + "'}");
            Object tab = ReflectionAPI.getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[]{ReflectionAPI.getNmsClass("IChatBaseComponent")}).newInstance(new Object[]{tabHeader});

            Field f = tab.getClass().getDeclaredField("b");
            f.setAccessible(true);
            f.set(tab, tabFooter);

            ReflectionAPI.sendPacket(p, tab);
        } catch (IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public String getPrefix() {
        return prefix;
    }
}
