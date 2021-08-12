package me.aleiv.core.paper.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerFishEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.ChallengeType;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

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

    @EventHandler
    public void onItemDamage (PlayerItemBreakEvent e){

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var item = e.getBrokenItem();

        if (challenge.get(ChallengeType.BREAK_HOE).getEnabled() && item.getType().equals(Material.WOODEN_HOE)) {

            var player = e.getPlayer();
            var uuid = player.getUniqueId().toString();
            var team = game.getPlayerTeam(uuid);

            game.challenge(ChallengeType.BREAK_HOE, team);

        }

    }

    @EventHandler
    public void onCraft (CraftItemEvent e) {

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var item = e.getRecipe().getResult();

        if (challenge.get(ChallengeType.CRAFT_PAINTING).getEnabled() && item.getType().equals(Material.PAINTING)) {

            var player = (Player) e.getWhoClicked();
            var uuid = player.getUniqueId().toString();
            var team = game.getPlayerTeam(uuid);

            game.challenge(ChallengeType.CRAFT_PAINTING, team);

        }

    }

    @EventHandler
    public void onJump (PlayerJumpEvent e) {

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var block = e.getPlayer().getLocation().getBlock();

        if (challenge.get(ChallengeType.JUMP_BED).getEnabled() && block.getType().toString().contains("BED")) {

            var player = e.getPlayer();
            var uuid = player.getUniqueId().toString();
            var team = game.getPlayerTeam(uuid);

            game.challenge(ChallengeType.JUMP_BED, team);

        }

    }

}
