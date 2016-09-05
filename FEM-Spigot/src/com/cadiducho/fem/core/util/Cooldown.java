package com.cadiducho.fem.core.util;

import java.util.HashMap;
import org.bukkit.entity.Player;

public class Cooldown {

    private final int time;
    private final HashMap<String, Long> cooldowns;

    public Cooldown(int time) {
        this.time = time;
        this.cooldowns = new HashMap<>();
    }

    public int getTime() {
        return time;
    }

    private HashMap<String, Long> getCooldowns() {
        return cooldowns;
    }

    public int getTimeLeft(Player player) {
        if (!isCoolingDown(player)) {
            return 0;
        }
        return (int) (((getCooldowns().get(player.getName()) - (System.currentTimeMillis() - (getTime() * 1000))) / 1000) + 1);
    }
    
    public int getTimeLeft(String str) {
        if (!isCoolingDown(str)) {
            return 0;
        }
        return (int) (((getCooldowns().get(str) - (System.currentTimeMillis() - (getTime() * 1000))) / 1000) + 1);
    }

    public void setOnCooldown(Player player) {
        getCooldowns().put(player.getName(), System.currentTimeMillis());
    }
    
    public void setOnCooldown(String str) {
        getCooldowns().put(str, System.currentTimeMillis());
    }

    public boolean isCoolingDown(Player player) {
        if (!getCooldowns().containsKey(player.getName())) {
            return false;
        }
        return getCooldowns().get(player.getName()) >= (System.currentTimeMillis() - (getTime() * 1000));
    }
    
    public boolean isCoolingDown(String str) {
        if (!getCooldowns().containsKey(str)) {
            return false;
        }
        return getCooldowns().get(str) >= (System.currentTimeMillis() - (getTime() * 1000));
    }
}
