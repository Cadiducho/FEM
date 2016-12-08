package com.cadiducho.fem.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ReflectionAPI {
    private static HashMap<Class<? extends Entity>, Method> handles = new HashMap<Class<? extends Entity>, Method>();
    private static Field player_connection = null;
    private static Method player_sendPacket = null;

    public static String getVersion(){
        String[] array = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
        if (array.length == 4)
            return array[3] + ".";
        return "";
    }

    public static Class<?> getNmsClass(String name){
        String version = getVersion();
        String className = "net.minecraft.server." + version + name;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return clazz;
    }

    public static Field getFirstFieldByType(Class<?> clazz, Class<?> type){
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == type) {
                return field;
            }
        }
        return null;
    }

    public static Object getHandle(Entity entity){
        try {
            if (handles.get(entity.getClass()) != null)
                return handles.get(entity.getClass()).invoke(entity);
            else {
                Method entity_getHandle = entity.getClass().getMethod("getHandle");
                handles.put(entity.getClass(), entity_getHandle);
                return entity_getHandle.invoke(entity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void sendPacket(Player p, Object packet) throws IllegalArgumentException{
        try {
            if (player_connection == null) {
                player_connection = ReflectionAPI.getHandle(p).getClass().getField("playerConnection");
                for (Method m : player_connection.get(ReflectionAPI.getHandle(p)).getClass().getMethods()) {
                    if (m.getName().equalsIgnoreCase("sendPacket")) {
                        player_sendPacket = m;
                    }
                }
            }
            player_sendPacket.invoke(player_connection.get(ReflectionAPI.getHandle(p)), packet);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }
}
