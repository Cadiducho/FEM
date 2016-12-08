package com.cadiducho.fem.core.customEvents;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerTradeCompleteEvent extends Event{

    private final PlayerTradeEvent completedTrade;

    public PlayerTradeCompleteEvent(PlayerTradeEvent completedTrade) {
        this.completedTrade = completedTrade;
    }

    public Player getPlayer()
    {
        return this.completedTrade.getPlayer();
    }

    public Villager getShopkeeper() {
        return this.completedTrade.getVillager();
    }

    public InventoryClickEvent getClickEvent() {
        return this.completedTrade.getClickEvent();
    }

    public PlayerTradeEvent getCompletedTrade() {
        return this.completedTrade;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
