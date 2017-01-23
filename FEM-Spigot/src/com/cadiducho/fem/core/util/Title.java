package com.cadiducho.fem.core.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Minecraft 1.8 (1.9) Title
 *
 * @version 1.0.5
 * @author Maxim Van de Wynckel
 */
public class Title {
    private Class<?> packetClass = null;
    private Class<?> componentClass = null;
    private Class<?> packetTabClass = null;
    private Class<?> serializerClass = null;
    private Constructor<?> packetConstructor = null;
    private Constructor<?> packetTabConstructor = null;
    @SuppressWarnings("rawtypes")
    private Class<?> enumTitleAction = null;


    private int fadeIn;
    private int stay;
    private int fadeOut;
    private String title;
    private String subtitle;

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut){
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void send(Player p){
        packetClass = ReflectionAPI.getNmsClass("PacketPlayOutTitle");
        componentClass = ReflectionAPI.getNmsClass("IChatBaseComponent");
        serializerClass = ReflectionAPI.getNmsClass("IChatBaseComponent$ChatSerializer");
        enumTitleAction = ReflectionAPI.getNmsClass("PacketPlayOutTitle$EnumTitleAction");
        try {
            packetConstructor = packetClass.getConstructor(enumTitleAction, componentClass, int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (subtitle != null) {
            Object subTitleSer;
            Object subTitlePacket;
            try {
                subTitleSer = serializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
                subTitlePacket = packetConstructor.newInstance(enumTitleAction.getEnumConstants()[1], subTitleSer, fadeIn, stay, fadeOut);
                ReflectionAPI.sendPacket(p, subTitlePacket);
            } catch (IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
                System.out.println(enumTitleAction.getEnumConstants());
            }
        }
        if (title != null) {
            Object titleSer;
            Object titlePacket;
            try {
                titleSer = serializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
                titlePacket = packetConstructor.newInstance(enumTitleAction.getEnumConstants()[0], titleSer, fadeIn, stay, fadeOut);
                ReflectionAPI.sendPacket(p, titlePacket);
            } catch (IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }
}