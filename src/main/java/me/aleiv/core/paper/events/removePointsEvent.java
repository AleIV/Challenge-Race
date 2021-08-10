package me.aleiv.core.paper.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.aleiv.core.paper.Game.TeamColor;

public class removePointsEvent extends Event {
    
    private static final @Getter HandlerList HandlerList = new HandlerList();
    @SuppressWarnings({"java:S116", "java:S1170"})
    private final @Getter HandlerList Handlers = HandlerList;
    private final @Getter int points;
    private final @Getter TeamColor teamColor;


    public removePointsEvent(int points, TeamColor teamColor, boolean async) {
        super(async);
        this.points = points;
        this.teamColor = teamColor;
    }

    public removePointsEvent(int points, TeamColor teamColor) {
        this(points, teamColor, false);
    }

}