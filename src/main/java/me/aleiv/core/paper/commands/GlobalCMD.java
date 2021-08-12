package me.aleiv.core.paper.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Frames;
import me.aleiv.core.paper.Game.ChallengeType;
import me.aleiv.core.paper.Game.TeamColor;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("challenge|global")
@CommandPermission("admin.perm")
public class GlobalCMD extends BaseCommand {

    private @NonNull Core instance;

    public GlobalCMD(Core instance) {
        this.instance = instance;



    }

    @Subcommand("start")
    public void start(CommandSender sender) {
        Bukkit.broadcastMessage(ChatColor.of("color") + "GAME STARTED!");

    }

    @Subcommand("blank")
    public void blank(CommandSender sender, Integer i, TeamColor color) {

        var game = instance.getGame();
        if(color == TeamColor.BLUE){
            game.setBlueBlank(i);
            Bukkit.broadcastMessage(ChatColor.BLUE + "BLANK BLUE " + i);
    
        }else if(color == TeamColor.RED){
            game.setRedBlank(i);
            Bukkit.broadcastMessage(ChatColor.RED + "BLANK RED " + i);
        }

        game.updateBossBar();


    }

    @Subcommand("toggle")
    public void toggle(CommandSender sender, ChallengeType challenge) {

        var game = instance.getGame();
        var chall = game.getChallenges().get(challenge);
        var bool = !chall.getEnabled();
        chall.setEnabled(bool);
        Bukkit.broadcastMessage(bool + "->" + chall.getType().toString());


    }

    @Subcommand("animation")
    public void animation(CommandSender sender, Integer i) {

        var game = instance.getGame();

        game.animation(i, Frames.getFramesCharsIntegers(108, 176));


    }
    

    @Subcommand("title")
    public void title(CommandSender sender, Integer points, TeamColor color){

        var game = instance.getGame();
        var redTeam = game.getTeams().get(TeamColor.RED);
        var blueTeam = game.getTeams().get(TeamColor.BLUE);
        
        if(color == TeamColor.BLUE){
            blueTeam.setPoints(points);
            Bukkit.broadcastMessage(ChatColor.BLUE + "Points added BLUE.");
    
        }else if(color == TeamColor.RED){
            redTeam.setPoints(points);
            Bukkit.broadcastMessage(ChatColor.RED + "Points added RED.");
        }

        game.updateBossBar();

    }
}
