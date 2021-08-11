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

import co.aikar.taskchain.TaskChain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.events.addPointsEvent;
import me.aleiv.core.paper.events.removePointsEvent;
import me.aleiv.core.paper.objects.Challenge;
import me.aleiv.core.paper.objects.ItemCode;
import me.aleiv.core.paper.objects.Team;

@Data
@EqualsAndHashCode(callSuper = false)
public class Game extends BukkitRunnable {
    Core instance;

    long gameTime = 0;
    long startTime = 0;

    HashMap<ChallengeType, Challenge> challenges = new HashMap<>();
    HashMap<TeamColor, Team> teams = new HashMap<>();

    HashMap<Integer, String> negativeSpaces = new HashMap<>();

    GameStage gameStage;

    BossBar bossBar;

    String animation = "";

    String bar = Character.toString('\uE000');
    String red = Character.toString('\uE001');
    String blue = Character.toString('\uE002');
    String blank1 = Character.toString('\uE003');
    String blank2 = Character.toString('\uE009');
    String trophyBlank = Character.toString('\uE008');
    int blueBlank = 259;
    int redBlank = -293;
    int bluePerm = 259;
    int redPerm = -329;
    int fix = 35;
    int middleFix = 0;

    int test = 0;

    List<ItemCode> redAnimation = new ArrayList<>();
    List<ItemCode> blueAnimation = new ArrayList<>();
    List<ItemCode> countDownAnimation = new ArrayList<>();


    public enum ChallengeType {
        FISH, 
        JUMP_BED
    }

    public Game(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        teams.put(TeamColor.RED, new Team(TeamColor.RED));
        teams.put(TeamColor.BLUE, new Team(TeamColor.BLUE));

        gameStage = GameStage.INGAME;

        bossBar = Bukkit.createBossBar(new NamespacedKey(instance, "boss-raid"), "BOSSBAR", BarColor.BLUE, BarStyle.SOLID);
        bossBar.setVisible(true);

        registerCodes();

        //CHALLENGES

        challenges.put(ChallengeType.FISH, new Challenge(ChallengeType.FISH, Difficulty.EASY, "Pesca un pez."));
        challenges.put(ChallengeType.JUMP_BED, new Challenge(ChallengeType.JUMP_BED, Difficulty.EASY, "Brinca en una cama."));
        
    }

    public void updateBossBar(){
        var blueTeam = teams.get(TeamColor.BLUE);
        var redTeam = teams.get(TeamColor.RED);

        var rP = redTeam.getPoints();
        var bP = blueTeam.getPoints();
        var negRed = rP == 0 ? "" : getN(rP);
        var negBlue = bP == 0 ? "" : getN(bP);

        bossBar.setTitle(getN(fix) + bar + getN(redBlank) + blank1 + getN(blueBlank) + blank2 + getN(redPerm) + negRed + red + getN(middleFix) + getN(bluePerm) + negBlue + blue);
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

            Bukkit.getPluginManager().callEvent(new addPointsEvent(1, TeamColor.BLUE));
            
        }else if(teamColor == TeamColor.RED ){

            //ADD RED
            if(Rtotal+1 > 114) return;

            redTeam.addPoint();
            removeMFix();

            Bukkit.getPluginManager().callEvent(new addPointsEvent(1, TeamColor.RED));
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

            Bukkit.getPluginManager().callEvent(new removePointsEvent(1, TeamColor.BLUE));

        }else if(teamColor == TeamColor.RED){

            //REMOVE RED
            if(Rtotal-1 < 0) return;

            redTeam.removePoint();
            addMFix();

            Bukkit.getPluginManager().callEvent(new removePointsEvent(1, TeamColor.RED));
        }

        updateBossBar();
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

    public void challenge(ChallengeType challenge, TeamColor color){
        Bukkit.broadcastMessage(challenge + " -> " + color.toString());
    }

    public TeamColor getPlayerTeam(String uuid){
        for (Team team : teams.values()) {
            if(team.getPlayers().contains(uuid)){
                return team.getTeamColor();
            }
        }

        return TeamColor.NONE;
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

    public void animation(int frames, List<ItemCode> animationList){

        var chain = Core.newChain();

        animationList.forEach(an ->{
            chain.delay(frames).sync(() -> {

                var anim = Character.toString(an.getCode());
                animation = anim;

                Bukkit.getOnlinePlayers().forEach(p->{
                    p.sendTitle(animation, "", 0, 20, 20);
                   
                });

            });
        });
    
        chain.sync(TaskChain::abort).execute();

    }


    @Override
    public void run() {

        var new_time = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));

        gameTime = new_time;

        Bukkit.getPluginManager().callEvent(new GameTickEvent(new_time, true));
    }

    public enum TeamColor {
        RED, BLUE, NONE
    }

    public enum GameStage {
        LOBBY, INGAME
    }

    public enum Difficulty{
        EASY, MEDIUM, HARD
    }

    public void registerCodes(){
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
        
        redAnimation.add(new ItemCode('\uE010'));
        redAnimation.add(new ItemCode('\uE011'));
        redAnimation.add(new ItemCode('\uE012'));
        redAnimation.add(new ItemCode('\uE013'));

        blueAnimation.add(new ItemCode('\uE014'));
        blueAnimation.add(new ItemCode('\uE015'));
        blueAnimation.add(new ItemCode('\uE016'));
        blueAnimation.add(new ItemCode('\uE017'));

        countDownAnimation.add(new ItemCode('\uE050'));
        countDownAnimation.add(new ItemCode('\uE051'));
        countDownAnimation.add(new ItemCode('\uE052'));
        countDownAnimation.add(new ItemCode('\uE053'));
        countDownAnimation.add(new ItemCode('\uE054'));
        countDownAnimation.add(new ItemCode('\uE055'));
        countDownAnimation.add(new ItemCode('\uE056'));
        countDownAnimation.add(new ItemCode('\uE057'));
        countDownAnimation.add(new ItemCode('\uE058'));
        countDownAnimation.add(new ItemCode('\uE059'));
        countDownAnimation.add(new ItemCode('\uE060'));
        countDownAnimation.add(new ItemCode('\uE061'));
        countDownAnimation.add(new ItemCode('\uE062'));
        countDownAnimation.add(new ItemCode('\uE063'));
        countDownAnimation.add(new ItemCode('\uE064'));
        countDownAnimation.add(new ItemCode('\uE065'));
        countDownAnimation.add(new ItemCode('\uE066'));
        countDownAnimation.add(new ItemCode('\uE067'));
        countDownAnimation.add(new ItemCode('\uE068'));
        countDownAnimation.add(new ItemCode('\uE069'));
        countDownAnimation.add(new ItemCode('\uE070'));
        countDownAnimation.add(new ItemCode('\uE071'));
        countDownAnimation.add(new ItemCode('\uE072'));
        countDownAnimation.add(new ItemCode('\uE073'));
        countDownAnimation.add(new ItemCode('\uE074'));
        countDownAnimation.add(new ItemCode('\uE075'));
        countDownAnimation.add(new ItemCode('\uE076'));
        countDownAnimation.add(new ItemCode('\uE077'));
        countDownAnimation.add(new ItemCode('\uE078'));
        countDownAnimation.add(new ItemCode('\uE079'));
        countDownAnimation.add(new ItemCode('\uE080'));
        countDownAnimation.add(new ItemCode('\uE081'));
        countDownAnimation.add(new ItemCode('\uE082'));
        countDownAnimation.add(new ItemCode('\uE083'));
        countDownAnimation.add(new ItemCode('\uE084'));
        countDownAnimation.add(new ItemCode('\uE085'));
        countDownAnimation.add(new ItemCode('\uE086'));
        countDownAnimation.add(new ItemCode('\uE087'));
        countDownAnimation.add(new ItemCode('\uE088'));
        countDownAnimation.add(new ItemCode('\uE089'));
        countDownAnimation.add(new ItemCode('\uE090'));
        countDownAnimation.add(new ItemCode('\uE091'));
    }
}