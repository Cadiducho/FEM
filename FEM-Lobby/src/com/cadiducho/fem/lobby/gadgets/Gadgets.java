package com.cadiducho.fem.lobby.gadgets;

import com.cadiducho.fem.lobby.Lobby;
import com.cadiducho.fem.lobby.gadgets.list.Bonfire;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Gadgets {

    public static Gadgets BONFIRE = new Bonfire();

    //Data
    private int id;
    private String name;
    private ItemStack item;

    protected Lobby pro = Lobby.getInstance();

    public Gadgets(int id, String name, ItemStack item){
        this.id = id;
        this.name = name;
        this.item = item;
    }

    public int getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public ItemStack getItem(){
        return item;
    }

    public abstract void effects(Player p);
}
