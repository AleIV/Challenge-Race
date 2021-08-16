package me.aleiv.core.paper.commands;

import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameStage;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("challenge|race")
public class GlobalCMD extends BaseCommand {

    private @NonNull Core instance;

    public GlobalCMD(Core instance) {
        this.instance = instance;
    }

    @Default
    public void start(Player player) {
        var game = instance.getGame();
        if(game.getGameStage() == GameStage.LOBBY){
            game.getMenu().open(player);

        }else{
            player.sendMessage(ChatColor.RED + "The game is in progress.");
        }
            
    }

}
