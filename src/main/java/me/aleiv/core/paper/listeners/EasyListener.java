package me.aleiv.core.paper.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.ChallengeType;

public class EasyListener implements Listener{
    
    Core instance;

    public EasyListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void onFish(PlayerFishEvent e){
        var game = instance.getGame();
        var challenges = game.getChallenges();
        var fish = e.getCaught();
        if(challenges.get(ChallengeType.FISH).getEnabled() && fish instanceof Item){
            var item = (Item) fish;
            var type = item.getItemStack().getType();
            
            if(type == Material.SALMON || type == Material.TROPICAL_FISH || type == Material.COD || type == Material.PUFFERFISH){
                var player = e.getPlayer();
                var uuid = player.getUniqueId().toString();
                var team = game.getPlayerTeam(uuid);


                game.challenge(ChallengeType.FISH, team);
            }
        }
    }

    
}
