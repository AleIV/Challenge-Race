package me.aleiv.core.paper.objects;

import lombok.Data;
import me.aleiv.core.paper.Game.Difficulty;

@Data
public class Challenge {

    Boolean isEnabled;
    String name;
    String description;
    Difficulty difficulty;

    public Challenge(String name, Difficulty difficulty, String description){
        this.isEnabled = false;
        this.name = name;
        this.difficulty = difficulty;
        this.description = description;
        
    }
}
