package me.aleiv.core.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import co.aikar.taskchain.TaskChain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.events.addPointsEvent;
import me.aleiv.core.paper.events.removePointsEvent;
import me.aleiv.core.paper.objects.Challenge;
import me.aleiv.core.paper.objects.Team;
import net.md_5.bungee.api.ChatColor;
import us.jcedeno.libs.rapidinv.RapidInv;

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

    String colorRED = ChatColor.of("#e91c1c") + "";
    String colorBLUE = ChatColor.of("#1c22e9") + "";

    RapidInv menu;

    List<ChallengeType> road = new ArrayList<>();
    int nRoad = 0;
     
    public enum ChallengeType {

        //EASY MODE
        FISH, 
        JUMP_BED,
        BREAK_HOE,
        CRAFT_PAINTING,
        PAINT_SHEEP,
        BREAK_IRON_ORE,
        EAT_APPLE,
        CRAFT_POT,
        CACTUS_DAMAGE,
        BREED_SHEEPS,
        SWIMM_DOLPHIN,
        CRAFT_DIAMOND_SHOVEL,
        TRADE_VILLAGER,
        HIGH_LIMIT,
        EAT_DRY_KELP,
        THROW_EGG,
        EAT_ROTTEN_FLESH,
        PUT_CHEST_DONKEY,

        //MEDIUM MODE
        CREATE_NETHER_PORTAL,
        KILL_STRIDER,
        MOUNT_PIGG,
        GROW_TREE_IN_NETHER,
        KILL_IRON_GOLEM,
        EAT_GOLDEN_APPLE,
        SLEEP_IN_NETHER,

        //HARD MODE
        BREAK_BEE_NEST,
        CRAFT_RABBIT_STEW,
        CRAFT_END_CRYSTAL,
        EAT_BEETROOT_ON_PIG,
        KILL_PLAYER

    }

    public void bossbarLobby(){
        bossBar.setTitle(Character.toString('\uE058') + "");
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

    public Game(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        teams.put(TeamColor.RED, new Team(TeamColor.RED));
        teams.put(TeamColor.BLUE, new Team(TeamColor.BLUE));

        gameStage = GameStage.LOBBY;

        bossBar = Bukkit.createBossBar(new NamespacedKey(instance, "boss-race"), Character.toString('\uE058') + "", BarColor.BLUE, BarStyle.SOLID);
        bossBar.setVisible(true);

        registerCodes();

        var name = getN(-8) + ChatColor.WHITE + Character.toString('\uE004');

        this.menu = new RapidInv(6 * 9, name);

        //CHALLENGES

        //EASY 
        challenges.put(ChallengeType.FISH, new Challenge(ChallengeType.FISH, Difficulty.EASY, "Pesca un pez.")); //OK
        challenges.put(ChallengeType.BREAK_HOE, new Challenge(ChallengeType.BREAK_HOE, Difficulty.EASY, "Rompe una azada de madera.")); //OK
        challenges.put(ChallengeType.CRAFT_PAINTING, new Challenge(ChallengeType.CRAFT_PAINTING, Difficulty.EASY, "Craftea un cuadro.")); //OK
        challenges.put(ChallengeType.JUMP_BED, new Challenge(ChallengeType.JUMP_BED, Difficulty.EASY, "Brinca en una cama."));//OK
        challenges.put(ChallengeType.PAINT_SHEEP, new Challenge(ChallengeType.PAINT_SHEEP, Difficulty.EASY, "Pinta una oveja de morado."));//OK
        challenges.put(ChallengeType.BREAK_IRON_ORE, new Challenge(ChallengeType.BREAK_IRON_ORE, Difficulty.EASY, "Rompe una mena de hierro."));//OK
        challenges.put(ChallengeType.EAT_APPLE, new Challenge(ChallengeType.EAT_APPLE, Difficulty.EASY, "Come una manzana.")); //OK
        challenges.put(ChallengeType.CRAFT_POT, new Challenge(ChallengeType.CRAFT_POT, Difficulty.EASY, "Craftea una maceta.")); //OK
        challenges.put(ChallengeType.CACTUS_DAMAGE, new Challenge(ChallengeType.CACTUS_DAMAGE, Difficulty.EASY, "Recibe daño de un cactus."));//OK
        challenges.put(ChallengeType.BREED_SHEEPS, new Challenge(ChallengeType.BREED_SHEEPS, Difficulty.EASY, "Reproduce dos ovejas.")); //OK
        challenges.put(ChallengeType.SWIMM_DOLPHIN, new Challenge(ChallengeType.SWIMM_DOLPHIN, Difficulty.EASY, "Nada con un delfín."));//OK
        challenges.put(ChallengeType.CRAFT_DIAMOND_SHOVEL, new Challenge(ChallengeType.CRAFT_DIAMOND_SHOVEL, Difficulty.EASY, "Craftea una pala de diamante."));//OK
        challenges.put(ChallengeType.EAT_DRY_KELP, new Challenge(ChallengeType.EAT_DRY_KELP, Difficulty.EASY, "Come algas secas."));//OK
        challenges.put(ChallengeType.THROW_EGG, new Challenge(ChallengeType.THROW_EGG, Difficulty.EASY, "Lanza un huevo."));//OK
        challenges.put(ChallengeType.EAT_ROTTEN_FLESH, new Challenge(ChallengeType.EAT_ROTTEN_FLESH, Difficulty.EASY, "Come carne podrida."));//OK
        challenges.put(ChallengeType.PUT_CHEST_DONKEY, new Challenge(ChallengeType.PUT_CHEST_DONKEY, Difficulty.EASY, "Pon un cofre en un burro."));//OK

        //MEDIUM 
        challenges.put(ChallengeType.TRADE_VILLAGER, new Challenge(ChallengeType.TRADE_VILLAGER, Difficulty.EASY, "Tradea con un aldeano."));//OK
        challenges.put(ChallengeType.HIGH_LIMIT, new Challenge(ChallengeType.HIGH_LIMIT, Difficulty.MEDIUM, "Sube a la altura máxima."));//OK
        challenges.put(ChallengeType.CREATE_NETHER_PORTAL, new Challenge(ChallengeType.CREATE_NETHER_PORTAL, Difficulty.MEDIUM, "Crea un portal al nether."));//OK
        challenges.put(ChallengeType.KILL_STRIDER, new Challenge(ChallengeType.KILL_STRIDER, Difficulty.MEDIUM, "Mata a un strider."));//OK
        challenges.put(ChallengeType.GROW_TREE_IN_NETHER, new Challenge(ChallengeType.GROW_TREE_IN_NETHER, Difficulty.MEDIUM, "Haz crecer un arbol en el Nether."));//OK
        challenges.put(ChallengeType.KILL_IRON_GOLEM, new Challenge(ChallengeType.KILL_IRON_GOLEM, Difficulty.MEDIUM, "Mata un iron golem."));//OK
        challenges.put(ChallengeType.EAT_GOLDEN_APPLE, new Challenge(ChallengeType.EAT_GOLDEN_APPLE, Difficulty.MEDIUM, "Come una golden apple."));//OK
        challenges.put(ChallengeType.SLEEP_IN_NETHER, new Challenge(ChallengeType.SLEEP_IN_NETHER, Difficulty.MEDIUM, "Duerme en el nether."));//OK
        challenges.put(ChallengeType.BREAK_BEE_NEST, new Challenge(ChallengeType.BREAK_BEE_NEST, Difficulty.HARD, "Rompe un panal de abejas."));//OK

        //HARD 
        challenges.put(ChallengeType.CRAFT_RABBIT_STEW, new Challenge(ChallengeType.CRAFT_RABBIT_STEW, Difficulty.HARD, "Craftea un estofado de conejo."));//OK
        challenges.put(ChallengeType.CRAFT_END_CRYSTAL, new Challenge(ChallengeType.CRAFT_END_CRYSTAL, Difficulty.HARD, "Craftea un end crystal."));//OK
        challenges.put(ChallengeType.EAT_BEETROOT_ON_PIG, new Challenge(ChallengeType.EAT_BEETROOT_ON_PIG, Difficulty.HARD, "Come un rábano encima de un cerdo.")); //OK
        challenges.put(ChallengeType.KILL_PLAYER, new Challenge(ChallengeType.KILL_PLAYER, Difficulty.HARD, "Mata a un jugador."));//OK
        
    }

    public void viewChallenge(TeamColor color){
        var game = instance.getGame();
        var team = teams.get(color);

        var nRoad = team.getRoad();
        var road = game.getRoad();

        var current = road.get(nRoad);
        var challenge = challenges.get(current);

        var players = team.getPlayers();
        Bukkit.getOnlinePlayers().forEach(p ->{
            players.forEach(p2 ->{
                var p1 = (Player) p;
                if(p1.getName() == p2){
                    if(color == TeamColor.RED){
                        p.sendMessage(colorRED + "[NEW CHALLENGE] " + ChatColor.WHITE + "[" + challenge.getDescription() + "]");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);

                    }else if(color == TeamColor.BLUE){
                        p.sendMessage(colorBLUE + "[NEW CHALLENGE] " + ChatColor.WHITE + "[" + challenge.getDescription() + "]");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);
                    }
                }
            });
        });

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
            Bukkit.getOnlinePlayers().forEach(p ->{
                var player = (Player) p;
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);

            });
            
        }else if(teamColor == TeamColor.RED ){

            //ADD RED
            if(Rtotal+1 > 114) return;

            redTeam.addPoint();
            removeMFix();

            Bukkit.getPluginManager().callEvent(new addPointsEvent(1, TeamColor.RED));
            Bukkit.getOnlinePlayers().forEach(p ->{
                var player = (Player) p;
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);

            });
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

    public void sound(Sound sound){
        Bukkit.getOnlinePlayers().forEach(p ->{
            var player = (Player) p;
            player.playSound(player.getLocation(), sound, 1, 0.1f);

        });
    }

    public void challenge(ChallengeType challenge, TeamColor color){

        var chain = Core.newChain();

        for (int i = 0; i < 6; i++) {
            chain.delay(1).sync(() -> {
                addPoint(color);
                sound(Sound.BLOCK_FIRE_EXTINGUISH);
            });
        }
    
        chain.sync(TaskChain::abort).execute();

        switch (color) {
            case RED:{
                Bukkit.broadcastMessage(colorRED + "Red team did a challenge!");

                sound(Sound.BLOCK_NOTE_BLOCK_BELL);

            }break;

            case BLUE:{

                Bukkit.broadcastMessage(colorBLUE + "Blue team did a challenge!");
                
                sound(Sound.BLOCK_NOTE_BLOCK_BELL);
            }break;
        
            default:
                break;
        }

        var team = teams.get(color);
        team.setRoad(team.getRoad()+1);
        
        Bukkit.getScheduler().runTaskLater(instance, task ->{
            if(team.getPoints() < 114)
                viewChallenge(color);
        
        }, 20*3);

    }

    public void resetGame(){
        var game = instance.getGame();
        Bukkit.getScheduler().runTaskLater(instance, task ->{
            game.setGameStage(GameStage.LOBBY);

            Bukkit.getOnlinePlayers().forEach(p ->{
                var player = (Player) p;
                var loc = Bukkit.getWorld("world").getSpawnLocation();
                player.teleport(loc);
                player.setHealth(20);
                player.setSaturation(20);
                player.setGameMode(GameMode.SURVIVAL);

            });

            var chain = Core.newChain();

            for (int i = 0; i < 114; i++) {
                chain.delay(1).sync(() -> {
                    removePoint(TeamColor.RED);
                    removePoint(TeamColor.BLUE);
                });
            }
    
            chain.sync(TaskChain::abort).execute();

            var red = game.getTeams().get(TeamColor.RED);
            var blue = game.getTeams().get(TeamColor.BLUE);

            red.setRoad(0);
            blue.setRoad(0);
            game.bossbarLobby();

        }, 20*11);



    }

    public TeamColor getPlayerTeam(String name){
        for (Team team : teams.values()) {
            if(team.getPlayers().contains(name)){
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

    public void animation(int frames, List<Character> animationList){

        var chain = Core.newChain();

        animationList.forEach(charac ->{
            chain.delay(frames).sync(() -> {

                Bukkit.getOnlinePlayers().forEach(p->{
                    p.sendTitle(charac + "", "", 0, 20, 20);
                   
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
        LOBBY, INGAME, STARTING
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
        
    }
}