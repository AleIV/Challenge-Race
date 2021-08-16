package me.aleiv.core.paper.objects;

import lombok.Data;
import me.aleiv.core.paper.Game.ChallengeType;
import me.aleiv.core.paper.Game.Difficulty;

@Data
public class Challenge {

    ChallengeType type;
    String description;
    Difficulty difficulty;

    public Challenge(ChallengeType type, Difficulty difficulty, String description){
        this.type = type;
        this.difficulty = difficulty;
        this.description = description;
        
    }



}
