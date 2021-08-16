package me.aleiv.core.paper.objects;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.aleiv.core.paper.Game;
import me.aleiv.core.paper.Game.ChallengeType;
import me.aleiv.core.paper.Game.TeamColor;

@Data
public class Team {
    
    TeamColor teamColor;
    List<String> players;
    int points;
    int road;

    public Team(TeamColor teamColor){
        this.teamColor = teamColor;
        this.players = new ArrayList<>();
        this.points = 0;
        this.road = 0;
    }

    public void addPoint(){
        points++;
    }

    public void removePoint(){
        points--;
    }

    public boolean isChallengeEnabled(Game game, ChallengeType challenge){
        var roadList = game.getRoad();
        if(this.road < roadList.size()){
            var pos1 = roadList.get(this.road);

            if(pos1 == challenge){
                return true;
            }
        }
            
        return false;
        
    }


}