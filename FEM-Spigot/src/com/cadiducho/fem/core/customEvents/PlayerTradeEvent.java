package com.cadiducho.fem.core.customEvents;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerTradeEvent extends Event implements Cancellable{

    private final Villager villager;
    private final Player player;
    private final InventoryClickEvent clickEvent;
    private final ItemStack[] tradeRecipe;

    public PlayerTradeEvent(Villager villager, Player player, InventoryClickEvent clickEvent, ItemStack[] tradeRecipe) {
        this.villager = villager;
        this.player = player;
        this.clickEvent = clickEvent;
        this.tradeRecipe = tradeRecipe;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Villager getVillager() {
        return this.villager;
    }

    public InventoryClickEvent getClickEvent() {
        return this.clickEvent;
    }

    public ItemStack[] getTradeRecipe() {
        return this.tradeRecipe;
    }

    public boolean isCancelled() {
        return this.clickEvent.isCancelled();
    }

    public void setCancelled(boolean cancelled) {
        this.clickEvent.setCancelled(cancelled);
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
