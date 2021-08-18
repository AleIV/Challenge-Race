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
        var player = e.getEntity();

        var name = player.getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if (team != null && team.isChallengeEnabled(game, ChallengeType.CREATE_NETHER_PORTAL) && player != null) {

            game.challenge(ChallengeType.CREATE_NETHER_PORTAL, teamColor);
        }

    }

    @EventHandler
    public void kill(PlayerDeathEvent e){
        var game = instance.getGame();

        var cause = e.getDeathMessage();
        
        var name = e.getEntity().getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if(team != null && team.isChallengeEnabled(game, ChallengeType.SLEEP_IN_NETHER) && cause.contains("Intentional")){

            game.challenge(ChallengeType.SLEEP_IN_NETHER, teamColor);

        } 
    }

    @EventHandler
    public void kill(EntityDeathEvent e) {

        var game = instance.getGame();
        var entity = e.getEntity();

        if(entity.getKiller() != null){

            var player = (Player) entity.getKiller();
            var name = player.getName();
            var teamColor = game.getPlayerTeam(name);
            var team = game.getTeams().get(teamColor);

            if (team != null && team.isChallengeEnabled(game, ChallengeType.KILL_STRIDER) && entity instanceof Strider)  {
    
                game.challenge(ChallengeType.KILL_STRIDER, teamColor);
    
            }else if (team != null && team.isChallengeEnabled(game, ChallengeType.KILL_IRON_GOLEM) && entity instanceof IronGolem ) {
    
    
                game.challenge(ChallengeType.KILL_IRON_GOLEM, teamColor);
    
            }
        }

    }

    @EventHandler
    public void growTree(StructureGrowEvent e){
        var game = instance.getGame();
        var player = e.getPlayer();

        var name = player.getName();

        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if (team != null && team.isChallengeEnabled(game, ChallengeType.GROW_TREE_IN_NETHER) && player != null && player.getWorld().getEnvironment() == Environment.NETHER){

            game.challenge(ChallengeType.GROW_TREE_IN_NETHER, teamColor);

        }
        
    }

    @EventHandler
    public void onPlayerItemConsume (PlayerItemConsumeEvent e) {

        var player = e.getPlayer();

        var game = instance.getGame();

        var item = e.getItem();

        var name = player.getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if (team != null && team.isChallengeEnabled(game, ChallengeType.EAT_GOLDEN_APPLE) && item.getType().equals(Material.GOLDEN_APPLE)) {

            game.challenge(ChallengeType.EAT_GOLDEN_APPLE, teamColor);

        }

    }


    
}