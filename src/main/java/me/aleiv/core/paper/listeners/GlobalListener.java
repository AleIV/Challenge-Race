package me.aleiv.core.paper.listeners;

import java.util.Collections;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.ItemMeta;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Frames;
import me.aleiv.core.paper.Game.GameStage;
import me.aleiv.core.paper.Game.TeamColor;
import me.aleiv.core.paper.events.addPointsEvent;
import net.md_5.bungee.api.ChatColor;
import us.jcedeno.libs.rapidinv.ItemBuilder;

public class GlobalListener implements Listener {

    Core instance;

    public GlobalListener(Core instance) {
        this.instance = instance;

        updateStart();
        updateTeams();
        updatePromo();

    }

    public enum START_BUTTON {
        START, START_PRESS, STOP, STOP_PRESS
    }

    public void updatePromo() {

        var menu = instance.getGame().getMenu();

        var discord = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(1))
                .name(ChatColor.of("#4a4eba") + "Discord").addLore(ChatColor.GREEN + "LOS ADMINS", ChatColor.WHITE + "https://discord.gg/qSEhmpaEdX").build();
        var spigot = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(3))
                .name(ChatColor.of("#dfa126") + "Spigot").addLore(ChatColor.WHITE + "https://www.spigotmc.org/members/aleiv.374689/").build();
        var twitter = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(2))
                .name(ChatColor.of("#3ac1cb") + "Twitter").addLore(ChatColor.of("#26d2df") + "Development: " + ChatColor.WHITE + "AleIV https://twitter.com/AleIVCR",
                ChatColor.of("#3ac1cb") + "Art: " + ChatColor.WHITE + "Apocalix https://twitter.com/ApocalixDeLuque",
                ChatColor.of("#3ac1cb") + "Animations: " + ChatColor.WHITE + "Yulh https://twitter.com/yulhfx").build();

        menu.setItem(47, discord, action -> {
            var player = (Player) action.getWhoClicked();
            player.sendMessage(ChatColor.of("#4a4eba") + "Discord: https://discord.gg/qSEhmpaEdX" );

            sound(Sound.ENTITY_CHICKEN_EGG);

        });

        menu.setItem(49, spigot, action -> {
            var player = (Player) action.getWhoClicked();
            player.sendMessage(ChatColor.of("#dfa126") + "Spigot: https://www.spigotmc.org/members/aleiv.374689/" );

            sound(Sound.ENTITY_CHICKEN_EGG);
        });

        menu.setItem(51, twitter, action -> {
            var player = (Player) action.getWhoClicked();
            player.sendMessage(ChatColor.of("#3ac1cb") + "Development: " + ChatColor.WHITE + "AleIV https://twitter.com/AleIVCR");
            player.sendMessage(ChatColor.of("#3ac1cb") + "Art: " + ChatColor.WHITE + "Apocalix https://twitter.com/ApocalixDeLuque");
            player.sendMessage(ChatColor.of("#3ac1cb") + "Animations: " + ChatColor.WHITE + "Yulh https://twitter.com/yulhfx");

            sound(Sound.ENTITY_CHICKEN_EGG);

        });
    }

    public void updateGui(Player player) {
        updateStart();
        updateTeams();
        updatePromo();
    }

    public void sound(Sound sound){
        Bukkit.getOnlinePlayers().forEach(p ->{
            var player = (Player) p;
            player.playSound(player.getLocation(), sound, 1, 0.1f);

        });
    }

    public void generateChallenges(){
        var game = instance.getGame();
        var road = game.getRoad();
        var challenges = game.getChallenges();

        var list = challenges.values().stream().collect(Collectors.toList());
        Collections.shuffle(list);

        road.clear();
        list.forEach(ch ->{
            road.add(ch.getType());
        });

    }

    public boolean startGame(){
        var game = instance.getGame();
        var red = game.getTeams().get(TeamColor.RED);
        var blue = game.getTeams().get(TeamColor.BLUE);

        if(!red.getPlayers().isEmpty() && !blue.getPlayers().isEmpty()){

            generateChallenges();

            Bukkit.getOnlinePlayers().forEach(p ->{
                var player = (Player) p;
                var loc = Bukkit.getWorld("world").getSpawnLocation();
                player.teleport(loc);
                player.setHealth(20);
                player.setSaturation(20);
                player.setGameMode(GameMode.SURVIVAL);

            });

            game.setGameStage(GameStage.STARTING);

            game.animation(1, Frames.getFramesCharsIntegers(108, 176));

            Bukkit.getScheduler().runTaskLater(instance, task->{
                Bukkit.getOnlinePlayers().forEach(p ->{
                    var player = (Player) p;
                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);

                    game.setGameStage(GameStage.INGAME);

                    game.viewChallenge(TeamColor.BLUE);
                    game.viewChallenge(TeamColor.RED);
    
                });
            }, 20*3);

            return true;
        }else{
            return false;
        }
    }

    public void updateStart() {

        var game = instance.getGame();
        var menu = game.getMenu();

        var start = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(7))
                .name(ChatColor.of("#37e91c") + "Click to start.").build();


        menu.setItem(3, start, action -> {
            var player = (Player) action.getWhoClicked();

            if(startGame()){
                sound(Sound.ENTITY_CHICKEN_EGG);
                player.closeInventory();
            }else{
                sound(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
                player.sendMessage(ChatColor.RED + "The game can't start, the teams are not ready.");
                player.closeInventory();
            }

        });
        menu.setItem(4, start, action -> {
            var player = (Player) action.getWhoClicked();

            if(startGame()){
                sound(Sound.ENTITY_CHICKEN_EGG);
                player.closeInventory();
            }else{
                sound(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
                player.sendMessage(ChatColor.RED + "The game can't start, the teams are not ready.");
                player.closeInventory();
            }

        });
        menu.setItem(5, start, action -> {
            var player = (Player) action.getWhoClicked();

            if(startGame()){
                sound(Sound.ENTITY_CHICKEN_EGG);
                player.closeInventory();
            }else{
                sound(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
                player.sendMessage(ChatColor.RED + "The game can't start, the teams are not ready.");
                player.closeInventory();
            }

        });
    }

    public void updateTeams() {

        var menu = instance.getGame().getMenu();

        var red = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(4))
                .name(ChatColor.of("#e91c1c") + "Join red team.");
        var blue = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(5))
                .name(ChatColor.of("#1c22e9") + "Join blue team.");

        var game = instance.getGame();

        var redTeam = game.getTeams().get(TeamColor.RED);
        var blueTeam = game.getTeams().get(TeamColor.BLUE);

        var redPlayers = redTeam.getPlayers();
        var bluePlayers = blueTeam.getPlayers();

        var redBuilder = new StringBuilder();
        
        redPlayers.forEach(r ->{
            redBuilder.append(ChatColor.WHITE + "" + r + "\n");
        });

        red.lore(redBuilder.toString());


        var blueBuilder = new StringBuilder();

        bluePlayers.forEach(b ->{
            blueBuilder.append(ChatColor.WHITE + "" + b + "\n");
        });

        blue.lore(blueBuilder.toString());

        menu.setItem(18, red.build(), action -> {
            var player = (Player) action.getWhoClicked();
            
            var name = player.getName();
        
            if(bluePlayers.contains(name)){
                bluePlayers.remove(name);
                redPlayers.add(name);

            }else{
                if(!redPlayers.contains(name)){
                    redPlayers.add(name);
                }
            }

            updateTeams();
            sound(Sound.ENTITY_CHICKEN_EGG);
            player.sendMessage(game.getColorRED() + "Joined team red.");

        });

        menu.setItem(26, blue.build(), action -> {
            var player = (Player) action.getWhoClicked();

            var name = player.getName();

            if(redPlayers.contains(name)){
                redPlayers.remove(name);
                bluePlayers.add(name);

            }else{
                if(!bluePlayers.contains(name)){
                    bluePlayers.add(name);
                }
            }
            
            updateTeams();
            sound(Sound.ENTITY_CHICKEN_EGG);
            player.sendMessage(game.getColorBLUE() + "Joined team blue.");
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        var bossBar = instance.getGame().getBossBar();
        bossBar.addPlayer(player);

        var game = instance.getGame();

        var redTeam = game.getTeams().get(TeamColor.RED);
        var blueTeam = game.getTeams().get(TeamColor.BLUE);

        var redPlayers = redTeam.getPlayers();
        var bluePlayers = blueTeam.getPlayers();

        var name = player.getName();
        if(game.getGameStage() == GameStage.INGAME && (!redPlayers.contains(name) || !bluePlayers.contains(name))){
            player.setGameMode(GameMode.SPECTATOR);
        }

    }

    @EventHandler
    public void onPoint(PlayerInteractEvent e) {

        var game = instance.getGame();
        var player = e.getPlayer();

        if (e.getItem() == null){
            return;
        }

        var mat = e.getItem().getType();

        if(player.getGameMode() != GameMode.CREATIVE) return;

        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (mat == Material.BLUE_WOOL) {
                game.removePoint(TeamColor.BLUE);

            } else if (mat == Material.RED_WOOL) {
                game.removePoint(TeamColor.RED);

            }

        } else if (e.getAction() == Action.LEFT_CLICK_AIR) {
            if (mat == Material.BLUE_WOOL) {
                game.addPoint(TeamColor.BLUE);

            } else if (mat == Material.RED_WOOL) {
                game.addPoint(TeamColor.RED);

            }
        }

    }

    @EventHandler
    public void onPoint(addPointsEvent e) {
        var game = instance.getGame();
        var color = e.getTeamColor();
        var team = game.getTeams().get(color);

        if (Math.abs(team.getPoints()) == 114) {
            switch (color) {
                case RED: {

                    game.animation(3, Frames.getFramesCharsIntegers(10, 57));
                    Bukkit.getOnlinePlayers().forEach(p ->{
                        var player = (Player) p;
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
        
                    });

                    game.resetGame();
                    

                }
                    break;

                case BLUE: {

                    game.animation(3, Frames.getFramesCharsIntegers(59, 107));
                    Bukkit.getOnlinePlayers().forEach(p ->{
                        var player = (Player) p;
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
        
                    });

                    game.resetGame();

                }
                    break;

                default:
                    break;
            }
        }

    }

}
