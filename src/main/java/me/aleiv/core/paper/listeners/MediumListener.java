package me.aleiv.core.paper.listeners;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Strider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.StructureGrowEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.ChallengeType;

public class MediumListener implements Listener{
    
    Core instance;

    public MediumListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void portalCreate(PortalCreateEvent e) {

        var game = instance.getGame();
        var challenge = game.getChallenges();
        var player = e.getEntity();

        if (challenge.get(ChallengeType.CREATE_NETHER_PORTAL).getEnabled() && player != null) {
    
            var name = player.getName();
            var team = game.getPlayerTeam(name);

            game.challenge(ChallengeType.CREATE_NETHER_PORTAL, team);

        }

    }

    @EventHandler
    public void kill(PlayerDeathEvent e){
        var game = instance.getGame();
        var challenges = game.getChallenges();

        var cause = e.getDeathMessage();

        if(challenges.get(ChallengeType.SLEEP_IN_NETHER).getEnabled() && cause.contains("Intentional")){

            var name = e.getEntity().getName();
            var team = game.getPlayerTeam(name);

            game.challenge(ChallengeType.SLEEP_IN_NETHER, team);

        } 
    }

    @EventHandler
    public void kill(EntityDeathEvent e) {

        var game = instance.getGame();
        var challenge = game.getChallenges();
        var entity = e.getEntity();

        if (challenge.get(ChallengeType.KILL_STRIDER).getEnabled() && entity instanceof Strider && entity.getKiller() != null) {
    
            var player = (Player) entity.getKiller();
            var name = player.getName();
            var team = game.getPlayerTeam(name);

            game.challenge(ChallengeType.KILL_STRIDER, team);

        }else if (challenge.get(ChallengeType.KILL_IRON_GOLEM).getEnabled() && entity instanceof IronGolem && entity.getKiller() != null) {
    
            var player = (Player) entity.getKiller();
            var name = player.getName();
            var team = game.getPlayerTeam(name);

            game.challenge(ChallengeType.KILL_IRON_GOLEM, team);

        }

    }

    @EventHandler
    public void growTree(StructureGrowEvent e){
        var game = instance.getGame();
        var challenge = game.getChallenges();
        var player = e.getPlayer();

        if (challenge.get(ChallengeType.GROW_TREE_IN_NETHER).getEnabled() && player != null && player.getWorld().getEnvironment() == Environment.NETHER){
    
            var name = player.getName();
            var team = game.getPlayerTeam(name);

            game.challenge(ChallengeType.GROW_TREE_IN_NETHER, team);

        }
        
    }

    @EventHandler
    public void onPlayerItemConsume (PlayerItemConsumeEvent e) {

        var player = e.getPlayer();

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var item = e.getItem();

        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if (challenge.get(ChallengeType.EAT_GOLDEN_APPLE).getEnabled() && item.getType().equals(Material.GOLDEN_APPLE)) {

            game.challenge(ChallengeType.EAT_GOLDEN_APPLE, team);

        }

    }


    
}