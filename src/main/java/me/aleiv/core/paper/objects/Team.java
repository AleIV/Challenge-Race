package me.aleiv.core.paper.objects;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.aleiv.core.paper.Game.TeamColor;

@Data
public class Team {
    
    TeamColor teamColor;
    List<String> players;
    int points;

    public Team(TeamColor teamColor){
        this.teamColor = teamColor;
        this.players = new ArrayList<>();
        this.points = 0;
    }

    public void addPoint(){
        points++;
    }

    public void removePoint(){
        points--;
    }


}