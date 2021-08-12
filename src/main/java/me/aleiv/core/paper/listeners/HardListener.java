package me.aleiv.core.paper.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        var challenges = game.getChallenges();
        var block = e.getBlock();
        if(challenges.get(ChallengeType.BREAK_BEE_NEST).getEnabled() && block.getType() == Material.BEE_NEST){
            var player = e.getPlayer();
            var uuid = player.getUniqueId().toString();
            var team = game.getPlayerTeam(uuid);


            game.challenge(ChallengeType.BREAK_BEE_NEST, team);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        var game = instance.getGame();
        var challenges = game.getChallenges();
        var item = e.getCurrentItem();
        if(challenges.get(ChallengeType.CRAFT_RABBIT_STEW).getEnabled() && item != null && item.getType() == Material.RABBIT_STEW){
            var player = (Player) e.getWhoClicked();
            var uuid = player.getUniqueId().toString();
            var team = game.getPlayerTeam(uuid);


            game.challenge(ChallengeType.CRAFT_RABBIT_STEW, team);

        }else if(challenges.get(ChallengeType.CRAFT_END_CRYSTAL).getEnabled() && item != null && item.getType() == Material.END_CRYSTAL){
            var player = (Player) e.getWhoClicked();
            var uuid = player.getUniqueId().toString();
            var team = game.getPlayerTeam(uuid);


            game.challenge(ChallengeType.CRAFT_END_CRYSTAL, team);
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e){
        var game = instance.getGame();
        var challenges = game.getChallenges();
        var item = e.getItem();
        if(challenges.get(ChallengeType.EAT_BEETROOT_ON_PIG).getEnabled()){
            var player = e.getPlayer();
            var check = player.getVehicle();

            if(check != null && check instanceof Pig){
                if(item != null && item.getType() == Material.BEETROOT){
                    var uuid = player.getUniqueId().toString();
                    var team = game.getPlayerTeam(uuid);
        
        
                    game.challenge(ChallengeType.EAT_BEETROOT_ON_PIG, team);
                }
            }
            
            

        } 
    }

    @EventHandler
    public void onShoot(EntityDamageByEntityEvent e){
        var shoot = e.getEntity();
        if(shoot instanceof Snowball){
            
        }
    }


}