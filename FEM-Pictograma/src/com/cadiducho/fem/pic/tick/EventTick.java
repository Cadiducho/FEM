package com.cadiducho.fem.pic.tick;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventTick extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final TickType type;

    public EventTick(TickType type) {
        this.type = type;
    }

    public TickType getType() {
        return this.type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
