package me.aleiv.core.paper.listeners;

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
import me.aleiv.core.paper.utilities.fastInv.FastInv;
import me.aleiv.core.paper.utilities.fastInv.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class GlobalListener implements Listener {

    Core instance;

    FastInv menu;

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
        var discord = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(1))
                .name(ChatColor.of("#4a4eba") + "Discord").addLore(ChatColor.GREEN + "LOS ADMINS", ChatColor.WHITE + "https://discord.gg/qSEhmpaEdX").build();
        var spigot = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(3))
                .name(ChatColor.of("#dfa126") + "Spigot").addLore(ChatColor.WHITE + "https://www.spigotmc.org/members/aleiv.374689/").build();
        var twitter = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(2))
                .name(ChatColor.of("#26d2df") + "Twitter").addLore(ChatColor.of("#26d2df") + "Development: " + ChatColor.WHITE + "AleIV https://twitter.com/AleIVCR",
                ChatColor.of("#26d2df") + "Art: " + ChatColor.WHITE + "Apocalix https://twitter.com/ApocalixDeLuque",
                ChatColor.of("#26d2df") + "Animations: " + ChatColor.WHITE + "Yulh https://twitter.com/yulhfx").build();

        menu.setItem(47, discord, action -> {
            var player = (Player) action.getWhoClicked();
            player.sendMessage(ChatColor.of("#4a4eba") + "Discord: https://discord.gg/qSEhmpaEdX" );

        });

        menu.setItem(49, spigot, action -> {
            var player = (Player) action.getWhoClicked();
            player.sendMessage(ChatColor.of("#dfa126") + "Spigot: https://www.spigotmc.org/members/aleiv.374689/" );
        });

        menu.setItem(51, twitter, action -> {
            var player = (Player) action.getWhoClicked();
            player.sendMessage(ChatColor.of("#26d2df") + "Development: " + ChatColor.WHITE + "AleIV https://twitter.com/AleIVCR");
            player.sendMessage(ChatColor.of("#26d2df") + "Art: " + ChatColor.WHITE + "Apocalix https://twitter.com/ApocalixDeLuque");
            player.sendMessage(ChatColor.of("#26d2df") + "Animations: " + ChatColor.WHITE + "Yulh https://twitter.com/yulhfx");

        });
    }

    public void updateGui(Player player) {
        updateStart();
        updateTeams();
        updatePromo();
    }

    public boolean startGame(){
        var game = instance.getGame();
        var red = game.getTeams().get(TeamColor.RED);
        var blue = game.getTeams().get(TeamColor.BLUE);
        if(!red.getPlayers().isEmpty() && !blue.getPlayers().isEmpty()){

            Bukkit.getOnlinePlayers().forEach(p ->{
                var player = (Player) p;
                var loc = Bukkit.getWorld("world").getSpawnLocation();
                player.teleport(loc);
                player.setHealth(20);
                player.setSaturation(20);
                player.setGameMode(GameMode.SURVIVAL);

            });

            game.setGameStage(GameStage.INGAME);

            game.animation(1, Frames.getFramesCharsIntegers(108, 176));

            Bukkit.getScheduler().runTaskLater(instance, task->{
                Bukkit.getOnlinePlayers().forEach(p ->{
                    var player = (Player) p;
                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
    
                });
            }, 20*4);

            return true;
        }else{
            return false;
        }
    }

    public void updateStart() {

        var game = instance.getGame();

        var name = game.getN(-8) + ChatColor.WHITE + Character.toString('\uE004');

        this.menu = new FastInv(6 * 9, name);

        var start = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(7))
                .name(ChatColor.of("#37e91c") + "Click to start.").build();


        menu.setItem(3, start, action -> {
            var player = (Player) action.getWhoClicked();

            if(startGame()){

                player.closeInventory();
            }else{
                player.sendMessage(ChatColor.RED + "The game can't start, the teams are not ready.");
                player.closeInventory();
            }

        });
        menu.setItem(4, start, action -> {
            var player = (Player) action.getWhoClicked();

            if(startGame()){

                player.closeInventory();
            }else{
                player.sendMessage(ChatColor.RED + "The game can't start, the teams are not ready.");
                player.closeInventory();
            }

        });
        menu.setItem(5, start, action -> {
            var player = (Player) action.getWhoClicked();

            if(startGame()){

                player.closeInventory();
            }else{
                player.sendMessage(ChatColor.RED + "The game can't start, the teams are not ready.");
                player.closeInventory();
            }

        });
    }

    public void updateTeams() {
        var red = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(4))
                .name(ChatColor.of("#e91c1c") + "Join red team.");
        var blue = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(5))
                .name(ChatColor.of("#1c22e9") + "Join blue team.");

        var game = instance.getGame();

        var redTeam = game.getTeams().get(TeamColor.RED);
        var blueTeam = game.getTeams().get(TeamColor.BLUE);

        var redPlayers = redTeam.getPlayers();
        var bluePlayers = blueTeam.getPlayers();

        redPlayers.forEach(r ->{
            red.addLore(ChatColor.WHITE + "" + r);
        });

        bluePlayers.forEach(b ->{
            red.addLore(ChatColor.WHITE + "" + b);
        });

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
            
            player.sendMessage();
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

        if (e.getItem() == null)
            return;

        var mat = e.getItem().getType();
        var game = instance.getGame();
        var player = e.getPlayer();

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

            } else if (mat == Material.GREEN_WOOL) {
                menu.open(player);

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

                    game.animation(4, Frames.getFramesCharsIntegers(10, 57));
                    Bukkit.getOnlinePlayers().forEach(p ->{
                        var player = (Player) p;
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
        
                    });

                }
                    break;

                case BLUE: {

                    game.animation(4, Frames.getFramesCharsIntegers(59, 107));
                    Bukkit.getOnlinePlayers().forEach(p ->{
                        var player = (Player) p;
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
        
                    });

                }
                    break;

                default:
                    break;
            }
        }
    }

}
