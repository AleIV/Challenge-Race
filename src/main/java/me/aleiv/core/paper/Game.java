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

        //EASY MODE
        FISH, 
        JUMP_BED,
        BREAK_HOE,
        CRAFT_PAINTING,
        PAINT_SHEEP,
        COOK_IRON,
        EAT_APPLE,
        CRAFT_POT,
        CACTUS_DAMAGE,
        BREED_SHEEPS,
        SWIMM_DOLPHIN,
        CRAFT_DIAMOND_SHOVEL,
        TRADE_VILLAGER,
        WATERDROP_50_BLOCKS,
        HIGH_LIMIT,
        CRAFT_SUSP_STEW,
        EAT_DRY_KELP,
        THROW_EGG,
        EAT_ROTTEN_FLESH,
        PUT_CHEST_DONKEY,

        //MEDIUM MODE
        CREATE_NETHER_PORTAL,
        KILL_STRIDER,
        MOUNT_PIGG,
        FIND_CHEST,
        REPAIR_ITEM,
        DISENCHANT,
        GROW_TREE_IN_NETHER,
        KILL_IRON_GOLEM,
        EAT_GOLDEN_APPLE,
        SLEEP_IN_NETHER,
        EAT_CAKE,
        BRING_WATER_NETHER,

        //HARD MODE
        BREAK_BEE_NEST,
        CRAFT_RABBIT_STEW,
        CRAFT_END_CRYSTAL,
        EAT_BEETROOT_ON_PIG,
        DRINK_MILK_WHILE_POISON,
        THROW_SNOWBALL_TO_PLAYER

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

        //EASY
        challenges.put(ChallengeType.FISH, new Challenge(ChallengeType.FISH, Difficulty.EASY, "Pesca un pez."));
        challenges.put(ChallengeType.BREAK_HOE, new Challenge(ChallengeType.BREAK_HOE, Difficulty.EASY, "Rompe una azada de madera usandola."));
        challenges.put(ChallengeType.CRAFT_PAINTING, new Challenge(ChallengeType.CRAFT_PAINTING, Difficulty.EASY, "Craftea un cuadro."));
        challenges.put(ChallengeType.JUMP_BED, new Challenge(ChallengeType.JUMP_BED, Difficulty.EASY, "Brinca en una cama."));
        challenges.put(ChallengeType.PAINT_SHEEP, new Challenge(ChallengeType.PAINT_SHEEP, Difficulty.EASY, "Pinta una oveja de morado."));
        challenges.put(ChallengeType.COOK_IRON, new Challenge(ChallengeType.COOK_IRON, Difficulty.EASY, "Cocina un lingote de hierro."));
        challenges.put(ChallengeType.EAT_APPLE, new Challenge(ChallengeType.EAT_APPLE, Difficulty.EASY, "Come una manzana."));
        challenges.put(ChallengeType.CRAFT_POT, new Challenge(ChallengeType.CRAFT_POT, Difficulty.EASY, "Craftea una maceta."));
        challenges.put(ChallengeType.CACTUS_DAMAGE, new Challenge(ChallengeType.CACTUS_DAMAGE, Difficulty.EASY, "Recibe daño de un cactus."));
        challenges.put(ChallengeType.BREED_SHEEPS, new Challenge(ChallengeType.BREED_SHEEPS, Difficulty.EASY, "Reproduce dos ovejas."));
        challenges.put(ChallengeType.SWIMM_DOLPHIN, new Challenge(ChallengeType.SWIMM_DOLPHIN, Difficulty.EASY, "Nada con un delfín."));
        challenges.put(ChallengeType.CRAFT_DIAMOND_SHOVEL, new Challenge(ChallengeType.CRAFT_DIAMOND_SHOVEL, Difficulty.EASY, "Craftea una pala de diamante."));
        challenges.put(ChallengeType.TRADE_VILLAGER, new Challenge(ChallengeType.TRADE_VILLAGER, Difficulty.EASY, "Tradea con un aldeano."));
        challenges.put(ChallengeType.WATERDROP_50_BLOCKS, new Challenge(ChallengeType.WATERDROP_50_BLOCKS, Difficulty.EASY, "Haz un waterdrop desde 50 bloques de altura."));
        challenges.put(ChallengeType.HIGH_LIMIT, new Challenge(ChallengeType.HIGH_LIMIT, Difficulty.EASY, "Sube a la altura máxima."));
        challenges.put(ChallengeType.CRAFT_SUSP_STEW, new Challenge(ChallengeType.CRAFT_SUSP_STEW, Difficulty.EASY, "Craftea una sopa sospechosa."));
        challenges.put(ChallengeType.EAT_DRY_KELP, new Challenge(ChallengeType.EAT_DRY_KELP, Difficulty.EASY, "Come algas secas."));
        challenges.put(ChallengeType.THROW_EGG, new Challenge(ChallengeType.THROW_EGG, Difficulty.EASY, "Lanza un huevo."));
        challenges.put(ChallengeType.EAT_ROTTEN_FLESH, new Challenge(ChallengeType.EAT_ROTTEN_FLESH, Difficulty.EASY, "Come carne podrida."));
        challenges.put(ChallengeType.PUT_CHEST_DONKEY, new Challenge(ChallengeType.PUT_CHEST_DONKEY, Difficulty.EASY, "Pon un cofre en un burro."));

        //MEDIUM
        challenges.put(ChallengeType.CREATE_NETHER_PORTAL, new Challenge(ChallengeType.CREATE_NETHER_PORTAL, Difficulty.MEDIUM, "Crea un portal al nether."));
        challenges.put(ChallengeType.KILL_STRIDER, new Challenge(ChallengeType.KILL_STRIDER, Difficulty.MEDIUM, "Mata a un strider."));
        challenges.put(ChallengeType.MOUNT_PIGG, new Challenge(ChallengeType.MOUNT_PIGG, Difficulty.MEDIUM, "Monta un cerdo."));
        challenges.put(ChallengeType.FIND_CHEST, new Challenge(ChallengeType.FIND_CHEST, Difficulty.MEDIUM, "Encuentra un cofre del tesoro."));
        challenges.put(ChallengeType.REPAIR_ITEM, new Challenge(ChallengeType.REPAIR_ITEM, Difficulty.MEDIUM, "Repara un item."));
        challenges.put(ChallengeType.DISENCHANT, new Challenge(ChallengeType.DISENCHANT, Difficulty.MEDIUM, "Desencanta un objeto."));
        challenges.put(ChallengeType.GROW_TREE_IN_NETHER, new Challenge(ChallengeType.GROW_TREE_IN_NETHER, Difficulty.MEDIUM, "Haz crecer un arbol en el Nether."));
        challenges.put(ChallengeType.KILL_IRON_GOLEM, new Challenge(ChallengeType.KILL_IRON_GOLEM, Difficulty.MEDIUM, "Mata un iron golem."));
        challenges.put(ChallengeType.EAT_GOLDEN_APPLE, new Challenge(ChallengeType.EAT_GOLDEN_APPLE, Difficulty.MEDIUM, "Come una golden apple."));
        challenges.put(ChallengeType.SLEEP_IN_NETHER, new Challenge(ChallengeType.SLEEP_IN_NETHER, Difficulty.MEDIUM, "Duerme en el nether."));
        challenges.put(ChallengeType.EAT_CAKE, new Challenge(ChallengeType.EAT_CAKE, Difficulty.MEDIUM, "Come un pastel."));
        challenges.put(ChallengeType.BRING_WATER_NETHER, new Challenge(ChallengeType.BRING_WATER_NETHER, Difficulty.MEDIUM, "Trae agua al nether mediante un caldero."));

        //HARD
        challenges.put(ChallengeType.BREAK_BEE_NEST, new Challenge(ChallengeType.BREAK_BEE_NEST, Difficulty.HARD, "Rompe un panal de abejas."));
        challenges.put(ChallengeType.CRAFT_RABBIT_STEW, new Challenge(ChallengeType.CRAFT_RABBIT_STEW, Difficulty.HARD, "Craftea un estofado de conejo."));
        challenges.put(ChallengeType.CRAFT_END_CRYSTAL, new Challenge(ChallengeType.CRAFT_END_CRYSTAL, Difficulty.HARD, "Craftea un end crystal."));
        challenges.put(ChallengeType.EAT_BEETROOT_ON_PIG, new Challenge(ChallengeType.EAT_BEETROOT_ON_PIG, Difficulty.HARD, "Come un rábano encima de un cerdo."));
        challenges.put(ChallengeType.DRINK_MILK_WHILE_POISON, new Challenge(ChallengeType.DRINK_MILK_WHILE_POISON, Difficulty.HARD, "Quitate el efecto de veneno con leche."));
        challenges.put(ChallengeType.THROW_SNOWBALL_TO_PLAYER, new Challenge(ChallengeType.THROW_SNOWBALL_TO_PLAYER, Difficulty.HARD, "Lanzale una bola de nieve a un jugador."));
        
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