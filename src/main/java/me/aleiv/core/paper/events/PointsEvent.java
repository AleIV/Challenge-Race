package me.aleiv.core.paper.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

public class PointsEvent extends Event {
    
    private static final @Getter HandlerList HandlerList = new HandlerList();
    @SuppressWarnings({"java:S116", "java:S1170"})
    private final @Getter HandlerList Handlers = HandlerList;
    private final @Getter int points;


    public PointsEvent(int points, boolean async) {
        super(async);
        this.points = points;
    }

    public PointsEvent(int points) {
        this(points, false);
    }

}