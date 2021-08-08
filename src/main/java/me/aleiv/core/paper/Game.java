package me.aleiv.core.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.objects.Challenge;
import me.aleiv.core.paper.objects.Team;

@Data
@EqualsAndHashCode(callSuper = false)
public class Game extends BukkitRunnable {
    Core instance;

    long gameTime = 0;
    long startTime = 0;

    HashMap<String, Challenge> challenges = new HashMap<>();
    HashMap<TeamColor, Team> teams = new HashMap<>();

    HashMap<Integer, String> negativeSpaces = new HashMap<>();

    GameStage gameStage;

    BossBar bossBar;

    String bar = Character.toString('\uE000');
    String red = Character.toString('\uE001');
    String blue = Character.toString('\uE002');
    String blank = Character.toString('\uE003');
    int blueBlank = 259;
    int redBlank = -293;
    int bluePerm = 259;
    int redPerm = -329;
    int fix = 35;
    int middleFix = 0;

    public void updateBossBar(){
        var blueTeam = teams.get(TeamColor.BLUE);
        var redTeam = teams.get(TeamColor.RED);

        var rP = redTeam.getPoints();
        var bP = blueTeam.getPoints();
        var negRed = rP == 0 ? "" : getN(rP);
        var negBlue = bP == 0 ? "" : getN(bP);

        bossBar.setTitle(getN(fix) + bar + getN(redBlank) + blank + getN(blueBlank) + blank + getN(redPerm) + negRed + red + getN(middleFix) + getN(bluePerm) + negBlue + blue);
    }

    public void addPoint(TeamColor teamColor){
        var redTeam = teams.get(TeamColor.RED);
        var blueTeam = teams.get(TeamColor.BLUE);

        var Btotal = blueTeam.getPoints();
        var Rtotal = redTeam.getPoints();

        if(teamColor == TeamColor.BLUE){

            //ADD BLUE
            if(Btotal-1 < -114) return;

            blueTeam.removePoint();
            removeFix();
            
        }else if(teamColor == TeamColor.RED ){

            //ADD RED
            if(Rtotal+1 > 114) return;

            redTeam.addPoint();
            removeMFix();

        }

        updateBossBar();

    }

    public void removePoint(TeamColor teamColor){
        var redTeam = teams.get(TeamColor.RED);
        var blueTeam = teams.get(TeamColor.BLUE);

        var Btotal = blueTeam.getPoints();
        var Rtotal = redTeam.getPoints();

        if(teamColor == TeamColor.BLUE){

            //REMOVE BLUE
            if(Btotal+1 > 0) return;

            blueTeam.addPoint();
            addFix();

        }else if(teamColor == TeamColor.RED){

            //REMOVE RED
            if(Rtotal-1 < 0) return;

            redTeam.removePoint();
            addMFix();
        }

        updateBossBar();
    }

    public Game(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        teams.put(TeamColor.RED, new Team(TeamColor.RED));
        teams.put(TeamColor.BLUE, new Team(TeamColor.BLUE));

        gameStage = GameStage.INGAME;

        bossBar = Bukkit.createBossBar(new NamespacedKey(instance, "boss-raid"), "BOSSBAR", BarColor.BLUE, BarStyle.SOLID);
        bossBar.setVisible(true);

        //NEGATIVE SPACES

        negativeSpaces.put(-1, Character.toString('\uF801'));
        negativeSpaces.put(-2, Character.toString('\uF802'));
        negativeSpaces.put(-3, Character.toString('\uF803'));
        negativeSpaces.put(-4, Character.toString('\uF804'));
        negativeSpaces.put(-5, Character.toString('\uF805'));
        negativeSpaces.put(-6, Character.toString('\uF806'));
        negativeSpaces.put(-7, Character.toString('\uF807'));
        negativeSpaces.put(-8, Character.toString('\uF808'));

        negativeSpaces.put(-16, Character.toString('\uF809'));
        negativeSpaces.put(-32, Character.toString('\uF80A'));
        negativeSpaces.put(-64, Character.toString('\uF80B'));
        negativeSpaces.put(-128, Character.toString('\uF80C'));
        negativeSpaces.put(-256, Character.toString('\uF80D'));
        negativeSpaces.put(-512, Character.toString('\uF80E'));
        negativeSpaces.put(-1024, Character.toString('\uF80F'));

        negativeSpaces.put(1, Character.toString('\uF821'));
        negativeSpaces.put(2, Character.toString('\uF822'));
        negativeSpaces.put(3, Character.toString('\uF823'));
        negativeSpaces.put(4, Character.toString('\uF824'));
        negativeSpaces.put(5, Character.toString('\uF825'));
        negativeSpaces.put(6, Character.toString('\uF826'));
        negativeSpaces.put(7, Character.toString('\uF827'));
        negativeSpaces.put(8, Character.toString('\uF828'));

        negativeSpaces.put(16, Character.toString('\uF829'));
        negativeSpaces.put(32, Character.toString('\uF82A'));
        negativeSpaces.put(64, Character.toString('\uF82B'));
        negativeSpaces.put(128, Character.toString('\uF82C'));
        negativeSpaces.put(256, Character.toString('\uF82D'));
        negativeSpaces.put(512, Character.toString('\uF82E'));
        negativeSpaces.put(1024, Character.toString('\uF82F'));
        
        //CHALLENGES

        challenges.put("PESCAR", new Challenge("PESCAR", Difficulty.EASY, "PESCA UN PEZ"));
    }

    public int getMax(int i){
        var nums = negativeSpaces.keySet().stream().filter(ne -> ne > 0 && ne <= i).mapToInt(v -> v).max();

        return nums.isPresent() ? nums.getAsInt() : 0;
    }


    public String getN(int number){

        if(number == 0) return "";

        var neg = number < 0;
        var n = Math.abs(number);
        final var fn = n;
        
        var nums = negativeSpaces.keySet().stream().filter(nu -> nu > 0 && nu <= fn).collect(Collectors.toList());
        List<Integer> count = new ArrayList<>();
        var negativeSpace = new StringBuilder();

        while(!nums.isEmpty()){
            var r = getMax(n);

            n -= r;
            count.add(r);
            final var fn2 = n;
            nums.clear();
            nums = negativeSpaces.keySet().stream().filter(nu -> nu > 0 && nu <= fn2).collect(Collectors.toList());

        }

        if(neg){
            for (var i : count) {
                negativeSpace.append(negativeSpaces.get(i-i*2));
            }
        }else{
            for (var i : count) {
                negativeSpace.append(negativeSpaces.get(i));
            }
        }

        return negativeSpace.toString();

    }

    public void addFix(){
        this.fix++;
    }

    public void removeFix(){
        this.fix--;
    }

    public void addMFix(){
        this.middleFix++;
    }

    public void removeMFix(){
        this.middleFix--;
    }

    @Override
    public void run() {

        var new_time = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));

        gameTime = new_time;

        Bukkit.getPluginManager().callEvent(new GameTickEvent(new_time, true));
    }

    public enum TeamColor {
        RED, BLUE
    }

    public enum GameStage {
        LOBBY, INGAME
    }

    public enum Difficulty{
        EASY, MEDIUM, HARD
    }
}