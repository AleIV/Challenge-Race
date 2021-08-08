package me.aleiv.core.paper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.TeamColor;
import me.aleiv.core.paper.events.GameTickEvent;

public class GlobalListener implements Listener {

    Core instance;

    public GlobalListener(Core instance) {
        this.instance = instance;
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
    }

    @EventHandler
    public void onPoint(PlayerInteractEvent e) {

        if(e.getItem() == null) return;

        var mat = e.getItem().getType();
        var game = instance.getGame();

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
    
            }
        }

    }

    

}
