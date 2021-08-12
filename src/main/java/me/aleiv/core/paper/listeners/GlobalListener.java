package me.aleiv.core.paper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.ItemMeta;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Frames;
import me.aleiv.core.paper.Game.TeamColor;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.events.addPointsEvent;
import me.aleiv.core.paper.utilities.fastInv.FastInv;
import me.aleiv.core.paper.utilities.fastInv.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class GlobalListener implements Listener {

    Core instance;

    FastInv menu;



    public GlobalListener(Core instance) {
        this.instance = instance;
        
        updateStart(START_BUTTON.START);
        updateTeams();
        updatePromo();

    }

    public enum START_BUTTON {
        START, START_PRESS, STOP, STOP_PRESS
    }

    public void updatePromo(){
        var discord = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(1)).name(ChatColor.of("#4a4eba") + "Discord").build();
        var spigot = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(3)).name(ChatColor.of("#dfa126") + "Spigot").build();
        var twitter = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(2)).name(ChatColor.of("#26d2df") + "Twitter").build();

        menu.setItem(47, discord, action->{

        });

        menu.setItem(49, spigot, action->{

        });

        menu.setItem(51, twitter, action->{

        });
    }

    public void updateGui(Player player, START_BUTTON button){
        updateStart(button);
        updateTeams();
        updatePromo();
    }

    public void updateStart(START_BUTTON button){

        var game = instance.getGame();
        switch (button) {
            case START:{
                var name = game.getN(-8) + ChatColor.WHITE + Character.toString('\uE004');

                this.menu = new FastInv(6*9, name);

                var start = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(7)).name(ChatColor.of("#37e91c") + "Click to start.").build();

                menu.setItem(3, start, action->{
                    var player = (Player) action.getWhoClicked();
                    player.closeInventory();
                    updateGui(player, START_BUTTON.START_PRESS);
                });
                menu.setItem(4, start, action->{
                    var player = (Player) action.getWhoClicked();
                    player.closeInventory();
                    updateGui(player, START_BUTTON.START_PRESS);
                });
                menu.setItem(5, start, action->{
                    var player = (Player) action.getWhoClicked();
                    player.closeInventory();
                    updateGui(player, START_BUTTON.START_PRESS);
                });

                
            }break;

            case START_PRESS:{
                var name = game.getN(-8) + ChatColor.WHITE + Character.toString('\uE005');

                this.menu = new FastInv(6*9, name);

                var start = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(7)).name(ChatColor.of("#37e91c") + "Click to start.").build();

                menu.setItem(3, start, action->{
        
                });
                menu.setItem(4, start, action->{
        
                });
                menu.setItem(5, start, action->{
        
                });
            }break;

            case STOP:{

                var name = game.getN(-8) + ChatColor.WHITE + Character.toString('\uE006');

                this.menu = new FastInv(6*9, name);

                var start = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(7)).name(ChatColor.of("#37e91c") + "Click to start.").build();

                menu.setItem(3, start, action->{
        
                });
                menu.setItem(4, start, action->{
        
                });
                menu.setItem(5, start, action->{
        
                });
            }break;

            case STOP_PRESS:{

                var name = game.getN(-8) + ChatColor.WHITE + Character.toString('\uE007');

                this.menu = new FastInv(6*9, name);

                var start = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(7)).name(ChatColor.of("#37e91c") + "Click to start.").build();

                menu.setItem(3, start, action->{
        
                });
                menu.setItem(4, start, action->{
        
                });
                menu.setItem(5, start, action->{
        
                });
            }break;
        
            default:
                break;
        }
    }

    public void updateTeams(){
        var red = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(4)).name(ChatColor.of("#e91c1c") + "Join red team.").build();
        var blue = new ItemBuilder(Material.PAPER).meta(ItemMeta.class, meta -> meta.setCustomModelData(5)).name(ChatColor.of("#1c22e9") + "Join blue team.").build();

        menu.setItem(18, red, action->{

        });

        menu.setItem(26, blue, action->{

        });
    }

    @EventHandler
    public void onGameTick(GameTickEvent e) {
        Bukkit.getScheduler().runTask(instance, () -> {

        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        var bossBar = instance.getGame().getBossBar();
        bossBar.addPlayer(player);

        var teams = instance.getGame().getTeams();

        var uuid = player.getUniqueId().toString();
        var players = teams.get(TeamColor.RED).getPlayers();
        if(players.contains(uuid)){
            players.remove(uuid);
            players.add(uuid);  
        }else{
            players.add(uuid);  
        }

    }

    @EventHandler
    public void onPoint(PlayerInteractEvent e) {

        if(e.getItem() == null) return;

        var mat = e.getItem().getType();
        var game = instance.getGame();
        var player = e.getPlayer();

        if(e.getAction() ==  Action.RIGHT_CLICK_AIR){
            if(mat == Material.BLUE_WOOL){
                game.removePoint(TeamColor.BLUE);
                
    
            }else if(mat == Material.RED_WOOL){
                game.removePoint(TeamColor.RED);
    
            }

        }else if(e.getAction() ==  Action.LEFT_CLICK_AIR){
            if(mat == Material.BLUE_WOOL){
                game.addPoint(TeamColor.BLUE);
    
            }else if(mat == Material.RED_WOOL){
                game.addPoint(TeamColor.RED);
    
            }else if(mat == Material.GREEN_WOOL){
                menu.open(player);
    
            }
        }

    }

    @EventHandler
    public void onPoint(addPointsEvent e){
        var game = instance.getGame();
        var color = e.getTeamColor();
        var team = game.getTeams().get(color);

        if(Math.abs(team.getPoints()) == 114){
            switch (color) {
                case RED:{

                     game.animation(4, Frames.getFramesCharsIntegers(10, 57));
                    
                }break;

                case BLUE:{

                    game.animation(4, Frames.getFramesCharsIntegers(59, 107));

                }break;
            
                default:
                    break;
            }
        }
    }

    

}
