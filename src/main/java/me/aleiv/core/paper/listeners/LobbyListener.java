package me.aleiv.core.paper.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameStage;
import net.md_5.bungee.api.ChatColor;

public class LobbyListener implements Listener {

    Core instance;
    String notInGameMSG;

    public LobbyListener(Core instance) {
        this.instance = instance;

        notInGameMSG = ChatColor.of("#14fab9") + "Game must start.";
    }


    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        var player = e.getPlayer();
        var game = instance.getGame();
        if (game.getGameStage() != GameStage.INGAME) {
            e.setCancelled(true);
            player.sendActionBar(notInGameMSG);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        var player = e.getPlayer();
        var game = instance.getGame();
        if (game.getGameStage() != GameStage.INGAME) {
            e.setCancelled(true);
            player.sendActionBar(notInGameMSG);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        var entity = e.getEntity();
        if (entity instanceof Player) {
            var player = (Player) entity;
            var game = instance.getGame();
            if (game.getGameStage() != GameStage.INGAME) {
                e.setCancelled(true);
                player.sendActionBar(notInGameMSG);
            }
        }

    }

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent e) {
        var damager = e.getDamager();

        if (damager instanceof Player) {
            var player = (Player) damager;
            var game = instance.getGame();
            if (game.getGameStage() != GameStage.INGAME) {
                e.setCancelled(true);
                player.sendActionBar(notInGameMSG);
            }
        }

    }


}
