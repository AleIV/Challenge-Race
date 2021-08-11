package me.aleiv.core.paper.objects;

import lombok.Data;
import me.aleiv.core.paper.Game.ChallengeType;
import me.aleiv.core.paper.Game.Difficulty;

@Data
public class Challenge {

    Boolean enabled;
    ChallengeType type;
    String description;
    Difficulty difficulty;

    public Challenge(ChallengeType type, Difficulty difficulty, String description){
        this.enabled = false;
        this.type = type;
        this.difficulty = difficulty;
        this.description = description;
        
    }
}
