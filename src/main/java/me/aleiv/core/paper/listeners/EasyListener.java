package me.aleiv.core.paper.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffectType;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.ChallengeType;
import me.aleiv.core.paper.events.GameTickEvent;

public class EasyListener implements Listener{
    
    Core instance;

    public EasyListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void onFish(PlayerFishEvent e){

        var game = instance.getGame();
        var fish = e.getCaught();

        var player = e.getPlayer();
        var name = player.getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if(team != null && team.isChallengeEnabled(game, ChallengeType.FISH) && fish instanceof Item){
            var item = (Item) fish;
            var type = item.getItemStack().getType();

            if(type == Material.SALMON || type == Material.TROPICAL_FISH || type == Material.COD || type == Material.PUFFERFISH){


                game.challenge(ChallengeType.FISH, teamColor);
            }
        }
    }

    @EventHandler
    public void onItemDamage (PlayerItemBreakEvent e){

        var game = instance.getGame();

        var item = e.getBrokenItem();

        var player = e.getPlayer();
        var name = player.getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if (team != null && team.isChallengeEnabled(game, ChallengeType.BREAK_HOE) && item.getType().equals(Material.WOODEN_HOE)) {


            game.challenge(ChallengeType.BREAK_HOE, teamColor);

        }

    }

    @EventHandler
    public void onCraft (CraftItemEvent e) {

        var game = instance.getGame();

        var item = e.getRecipe().getResult();

        var player = (Player) e.getWhoClicked();
        var name = player.getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);


        if (team != null && team.isChallengeEnabled(game, ChallengeType.CRAFT_PAINTING) && item.getType().equals(Material.PAINTING)) {

            game.challenge(ChallengeType.CRAFT_PAINTING, teamColor);

        } else if (team != null && team.isChallengeEnabled(game, ChallengeType.CRAFT_POT) && item.getType().equals(Material.FLOWER_POT)) {

            game.challenge(ChallengeType.CRAFT_POT, teamColor);

        } else if (team != null && team.isChallengeEnabled(game, ChallengeType.CRAFT_DIAMOND_SHOVEL) && item.getType().equals(Material.DIAMOND_SHOVEL)) {

            game.challenge(ChallengeType.CRAFT_DIAMOND_SHOVEL, teamColor);

        }

    }

    @EventHandler
    public void onJump (PlayerJumpEvent e) {

        var game = instance.getGame();

        var block = e.getPlayer().getLocation().getBlock();

        var player = e.getPlayer();
        var name = player.getName();

        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if (team != null && team.isChallengeEnabled(game, ChallengeType.JUMP_BED) && block.getType().toString().contains("BED")) {


            game.challenge(ChallengeType.JUMP_BED, teamColor);

        }

    }

    @EventHandler
    public void onInteract (PlayerInteractEntityEvent e) {

        var game = instance.getGame();
        var player = e.getPlayer();

        var entity = e.getRightClicked();

        var name = player.getName();

        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if (team != null && team.isChallengeEnabled(game, ChallengeType.PAINT_SHEEP) && entity instanceof Sheep
                && player.getInventory().getItemInMainHand().getType().equals(Material.PURPLE_DYE)) {

            game.challenge(ChallengeType.PAINT_SHEEP, teamColor);

        } else if (team != null && team.isChallengeEnabled(game, ChallengeType.PUT_CHEST_DONKEY) && entity instanceof Donkey
                && player.getInventory().getItemInMainHand().getType().equals(Material.CHEST)) {
            var donkey = (Donkey) entity;
            if(donkey.isTamed()){
                game.challenge(ChallengeType.PUT_CHEST_DONKEY, teamColor);

            }
    
        }

    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {

        var player = e.getPlayer();
        var block = e.getBlock();

        var game = instance.getGame();

        var name = player.getName();

        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);


        if (team != null && team.isChallengeEnabled(game, ChallengeType.BREAK_IRON_ORE) && block.getType().equals(Material.IRON_ORE)) {


            game.challenge(ChallengeType.BREAK_IRON_ORE, teamColor);

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

        if (team != null && team.isChallengeEnabled(game, ChallengeType.EAT_APPLE) && item.getType().equals(Material.APPLE)) {

            game.challenge(ChallengeType.EAT_APPLE, teamColor);

        } else if (team != null && team.isChallengeEnabled(game, ChallengeType.EAT_DRY_KELP) && item.getType().equals(Material.DRIED_KELP)) {

            game.challenge(ChallengeType.EAT_DRY_KELP, teamColor);

        } else if (team != null && team.isChallengeEnabled(game, ChallengeType.EAT_ROTTEN_FLESH) && item.getType().equals(Material.ROTTEN_FLESH)) {

            game.challenge(ChallengeType.EAT_ROTTEN_FLESH, teamColor);

        }

    }

    @EventHandler
    public void onDamage (EntityDamageEvent e) {

        var entity = e.getEntity();

        var game = instance.getGame();

        if (entity instanceof Player) {

            var player = (Player) e.getEntity();

            var name = player.getName();

            var teamColor = game.getPlayerTeam(name);
            var team = game.getTeams().get(teamColor);

            if (team != null && team.isChallengeEnabled(game, ChallengeType.CACTUS_DAMAGE) && e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)) {

                if (entityTouchesCactus(entity)) {

                    game.challenge(ChallengeType.CACTUS_DAMAGE, teamColor);

                }

            }

        }

    }

    @EventHandler
    public void onBreed (EntityBreedEvent e) {

        var player = e.getBreeder();
        var game = instance.getGame();
        
        var name = player.getName();

        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if (team != null && team.isChallengeEnabled(game, ChallengeType.BREED_SHEEPS) && e.getEntity() instanceof Sheep) {

            game.challenge(ChallengeType.BREED_SHEEPS, teamColor);

        }

    }

    @EventHandler
    public void onSkyHigh(GameTickEvent e){
        var game = instance.getGame();

        Bukkit.getScheduler().runTask(instance, task ->{
            Bukkit.getOnlinePlayers().forEach(player ->{
                var loc = player.getLocation();

                var name = player.getName();
                var teamColor = game.getPlayerTeam(name);
                var team = game.getTeams().get(teamColor);

                if(team != null && team.isChallengeEnabled(game, ChallengeType.HIGH_LIMIT) && loc.getY() >= 255){

                    game.challenge(ChallengeType.HIGH_LIMIT, teamColor);
                }
            });
        });
    }

    @EventHandler
    public void onEntityPotionEffect (EntityPotionEffectEvent e) {

        if (e.getEntity() instanceof Player) {

            var game = instance.getGame();

            var player = (Player) e.getEntity();
            var name = player.getName();
            var teamColor = game.getPlayerTeam(name);
            var team = game.getTeams().get(teamColor);

            if (team != null && team.isChallengeEnabled(game, ChallengeType.SWIMM_DOLPHIN) && e.getNewEffect() != null && e.getNewEffect().getType().equals(PotionEffectType.DOLPHINS_GRACE)) {

                if (e.getCause().equals(EntityPotionEffectEvent.Cause.DOLPHIN)) {

                    game.challenge(ChallengeType.SWIMM_DOLPHIN, teamColor);

                }

            }

        }

    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent e) {

        var player = e.getWhoClicked();

        var game = instance.getGame();

        var name = player.getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if (team != null && team.isChallengeEnabled(game, ChallengeType.TRADE_VILLAGER) && e.getView().getTopInventory() instanceof MerchantInventory) {

            MerchantInventory inv = (MerchantInventory) e.getView().getTopInventory();
            if (!inv.equals(e.getClickedInventory())) return;
            if (e.getSlotType() != InventoryType.SlotType.RESULT) return;
            MerchantRecipe recipe = inv.getSelectedRecipe();
            if (recipe == null) return;

            game.challenge(ChallengeType.TRADE_VILLAGER, teamColor);

        }

    }

    @EventHandler
    public void onEggThrow (PlayerEggThrowEvent e) {

        var player = e.getPlayer();

        var game = instance.getGame();

        var name = player.getName();
        var teamColor = game.getPlayerTeam(name);
        var team = game.getTeams().get(teamColor);

        if (team != null && team.isChallengeEnabled(game, ChallengeType.THROW_EGG)) {

            game.challenge(ChallengeType.THROW_EGG, teamColor);

        }

    }

    public static boolean entityTouchesCactus (Entity entity) {

        for (int i = 0; i <= 1; i++) {

            Block block = entity.getLocation().add(0, i, 0).getBlock();

            for (BlockFace blockFace : RELATIVE_DIRECTIONS) {
                Block relative = block.getRelative(blockFace);

                if (Material.CACTUS == relative.getType()) return true;
            }

        }

        return false;

    }

    private static final transient List<BlockFace> RELATIVE_DIRECTIONS = new ArrayList<>(
            Arrays.asList(
                    BlockFace.NORTH, BlockFace.EAST,
                    BlockFace.SOUTH, BlockFace.WEST,
                    BlockFace.DOWN, BlockFace.UP
            )
    );

}
