package me.aleiv.core.paper.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.ChallengeType;

public class HardListener implements Listener{
    
    Core instance;

    public HardListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        var game = instance.getGame();
        var block = e.getBlock();

        var name = e.getPlayer().getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if(team != null && team.isChallengeEnabled(game, ChallengeType.BREAK_BEE_NEST) && block.getType() == Material.BEE_NEST){

            game.challenge(ChallengeType.BREAK_BEE_NEST, teamColor);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        var game = instance.getGame();
        var item = e.getCurrentItem();

        var name = e.getWhoClicked().getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if(team != null && team.isChallengeEnabled(game, ChallengeType.CRAFT_RABBIT_STEW) && item != null && item.getType() == Material.RABBIT_STEW){

            game.challenge(ChallengeType.CRAFT_RABBIT_STEW, teamColor);

        }else if(team != null && team.isChallengeEnabled(game, ChallengeType.CRAFT_END_CRYSTAL) && item != null && item.getType() == Material.END_CRYSTAL){


            game.challenge(ChallengeType.CRAFT_END_CRYSTAL, teamColor);
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e){
        var game = instance.getGame();
        var item = e.getItem();

        var name = e.getPlayer().getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if(team != null && team.isChallengeEnabled(game, ChallengeType.EAT_BEETROOT_ON_PIG)){
            var player = e.getPlayer();
            var check = player.getVehicle();

            if(check != null && check instanceof Pig){
                if(item != null && item.getType() == Material.BEETROOT){
        
                    game.challenge(ChallengeType.EAT_BEETROOT_ON_PIG, teamColor);
                }
            }
            
            

        } 
    }

    @EventHandler
    public void kill(PlayerDeathEvent e){
        var game = instance.getGame();

        var killer = (Player) e.getEntity().getKiller();


        if(killer == null) return;

        var name = killer.getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if(team != null && team.isChallengeEnabled(game, ChallengeType.KILL_PLAYER) && killer != null){

            game.challenge(ChallengeType.KILL_PLAYER, teamColor);

        } 
    }


}