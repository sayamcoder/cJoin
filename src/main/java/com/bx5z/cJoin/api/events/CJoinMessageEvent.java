package com.bx5z.cJoin.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called just before a cJoin join message is broadcast.
 * Allows for modification or cancellation of the message.
 */
public class CJoinMessageEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private String joinMessage;
    private boolean cancelled;

    public CJoinMessageEvent(Player player, String joinMessage) {
        this.player = player;
        this.joinMessage = joinMessage;
        this.cancelled = false;
    }

    /**
     * Gets the player who triggered the event.
     * @return The joining player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the join message that will be broadcast.
     * @return The formatted join message.
     */
    public String getJoinMessage() {
        return joinMessage;
    }

    /**
     * Sets the join message that will be broadcast.
     * @param joinMessage The new join message.
     */
    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}