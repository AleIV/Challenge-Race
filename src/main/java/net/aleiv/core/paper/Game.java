package net.aleiv.core.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.aleiv.core.paper.events.GameTickEvent;

@Data
@EqualsAndHashCode(callSuper = false)
public class Game extends BukkitRunnable {
    Core instance;

    long gameTime = 0;
    long startTime = 0;

    HashMap<String, Boolean> challenges = new HashMap<>();
    HashMap<TEAM, List<String>> teams = new HashMap<>();

    GameStage gameStage;

    public Game(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        teams.put(TEAM.RED, new ArrayList<>());
        teams.put(TEAM.BLUE, new ArrayList<>());

        gameStage = GameStage.LOBBY;

    }

    @Override
    public void run() {

        var new_time = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));

        gameTime = new_time;

        Bukkit.getPluginManager().callEvent(new GameTickEvent(new_time, true));
    }

    public enum TEAM {
        RED, BLUE
    }

    public enum GameStage {
        LOBBY, INGAME
    }
}