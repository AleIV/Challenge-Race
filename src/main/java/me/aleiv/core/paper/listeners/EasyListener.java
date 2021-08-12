package me.aleiv.core.paper.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.ChallengeType;
import me.aleiv.core.paper.events.GameTickEvent;

import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        var player = e.getPlayer();
        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if(challenges.get(ChallengeType.FISH).getEnabled() && fish instanceof Item){
            var item = (Item) fish;
            var type = item.getItemStack().getType();

            if(type == Material.SALMON || type == Material.TROPICAL_FISH || type == Material.COD || type == Material.PUFFERFISH){


                game.challenge(ChallengeType.FISH, team);
            }
        }
    }

    @EventHandler
    public void onItemDamage (PlayerItemBreakEvent e){

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var item = e.getBrokenItem();

        var player = e.getPlayer();
        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if (challenge.get(ChallengeType.BREAK_HOE).getEnabled() && item.getType().equals(Material.WOODEN_HOE)) {


            game.challenge(ChallengeType.BREAK_HOE, team);

        }

    }

    @EventHandler
    public void onCraft (CraftItemEvent e) {

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var item = e.getRecipe().getResult();

        var player = (Player) e.getWhoClicked();
        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if (challenge.get(ChallengeType.CRAFT_PAINTING).getEnabled() && item.getType().equals(Material.PAINTING)) {

            game.challenge(ChallengeType.CRAFT_PAINTING, team);

        } else if (challenge.get(ChallengeType.CRAFT_POT).getEnabled() && item.getType().equals(Material.FLOWER_POT)) {

            game.challenge(ChallengeType.CRAFT_POT, team);

        } else if (challenge.get(ChallengeType.CRAFT_DIAMOND_SHOVEL).getEnabled() && item.getType().equals(Material.DIAMOND_SHOVEL)) {

            game.challenge(ChallengeType.CRAFT_DIAMOND_SHOVEL, team);

        }

    }

    @EventHandler
    public void onJump (PlayerJumpEvent e) {

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var block = e.getPlayer().getLocation().getBlock();

        var player = e.getPlayer();
        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if (challenge.get(ChallengeType.JUMP_BED).getEnabled() && block.getType().toString().contains("BED")) {


            game.challenge(ChallengeType.JUMP_BED, team);

        }

    }

    @EventHandler
    public void onInteract (PlayerInteractEntityEvent e) {

        var game = instance.getGame();
        var challenge = game.getChallenges();
        var player = e.getPlayer();

        var entity = e.getRightClicked();

        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if (challenge.get(ChallengeType.PAINT_SHEEP).getEnabled() && entity instanceof Sheep
                && player.getInventory().getItemInMainHand().getType().equals(Material.PURPLE_DYE)) {

            game.challenge(ChallengeType.PAINT_SHEEP, team);

        } else if (challenge.get(ChallengeType.PUT_CHEST_DONKEY).getEnabled() && entity instanceof Donkey
                && player.getInventory().getItemInMainHand().getType().equals(Material.CHEST)) {
            var donkey = (Donkey) entity;
            if(donkey.isTamed()){
                game.challenge(ChallengeType.PUT_CHEST_DONKEY, team);

            }
    
        }

    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {

        var player = e.getPlayer();
        var block = e.getBlock();

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if (challenge.get(ChallengeType.BREAK_IRON_ORE).getEnabled() && block.getType().equals(Material.IRON_ORE)) {


            game.challenge(ChallengeType.BREAK_IRON_ORE, team);

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

        if (challenge.get(ChallengeType.EAT_APPLE).getEnabled() && item.getType().equals(Material.APPLE)) {

            game.challenge(ChallengeType.EAT_APPLE, team);

        } else if (challenge.get(ChallengeType.EAT_DRY_KELP).getEnabled() && item.getType().equals(Material.DRIED_KELP)) {

            game.challenge(ChallengeType.EAT_DRY_KELP, team);

        } else if (challenge.get(ChallengeType.EAT_ROTTEN_FLESH).getEnabled() && item.getType().equals(Material.ROTTEN_FLESH)) {

            game.challenge(ChallengeType.EAT_ROTTEN_FLESH, team);

        }

    }

    @EventHandler
    public void onDamage (EntityDamageEvent e) {

        var entity = e.getEntity();

        var game = instance.getGame();
        var chalenge = game.getChallenges();

        if (entity instanceof Player) {

            var player = (Player) e.getEntity();

            var name = player.getName();
            var team = game.getPlayerTeam(name);

            if (chalenge.get(ChallengeType.CACTUS_DAMAGE).getEnabled() && e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)) {

                if (entityTouchesCactus(entity)) {

                    game.challenge(ChallengeType.CACTUS_DAMAGE, team);

                }

            }

        }

    }

    @EventHandler
    public void onBreed (EntityBreedEvent e) {

        var player = e.getBreeder();
        var game = instance.getGame();

        var challenge = game.getChallenges();

        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if (challenge.get(ChallengeType.BREED_SHEEPS).getEnabled() && e.getEntity() instanceof Sheep) {

            game.challenge(ChallengeType.BREED_SHEEPS, team);

        }

    }

    @EventHandler
    public void onSkyHigh(GameTickEvent e){
        var game = instance.getGame();
        var challenge = game.getChallenges();

        Bukkit.getScheduler().runTask(instance, task ->{
            Bukkit.getOnlinePlayers().forEach(player ->{
                var loc = player.getLocation();
                if(challenge.get(ChallengeType.HIGH_LIMIT).getEnabled() && loc.getY() >= 255){

                    var name = player.getName();
                    var team = game.getPlayerTeam(name);

                    game.challenge(ChallengeType.HIGH_LIMIT, team);
                }
            });
        });
    }

    @EventHandler
    public void onEntityPotionEffect (EntityPotionEffectEvent e) {

        var game = instance.getGame();
        var challenge = game.getChallenges();

        if (e.getEntity() instanceof Player) {

            if (challenge.get(ChallengeType.SWIMM_DOLPHIN).getEnabled() && e.getNewEffect() != null && e.getNewEffect().getType().equals(PotionEffectType.DOLPHINS_GRACE)) {

                if (e.getCause().equals(EntityPotionEffectEvent.Cause.DOLPHIN)) {

                    var player = (Player) e.getEntity();

                    var name = player.getName();
                    var team = game.getPlayerTeam(name);

                    game.challenge(ChallengeType.SWIMM_DOLPHIN, team);

                }

            }

        }

    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent e) {

        var player = e.getWhoClicked();

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if (challenge.get(ChallengeType.TRADE_VILLAGER).getEnabled() && e.getView().getTopInventory() instanceof MerchantInventory) {

            MerchantInventory inv = (MerchantInventory) e.getView().getTopInventory();
            if (!inv.equals(e.getClickedInventory())) return;
            if (e.getSlotType() != InventoryType.SlotType.RESULT) return;
            MerchantRecipe recipe = inv.getSelectedRecipe();
            if (recipe == null) return;

            game.challenge(ChallengeType.TRADE_VILLAGER, team);

        }

    }

    //TODO Testear esto.
    @EventHandler
    public void onEggThrow (PlayerEggThrowEvent e) {

        var player = e.getPlayer();

        var game = instance.getGame();
        var challenge = game.getChallenges();

        var name = player.getName();
        var team = game.getPlayerTeam(name);

        if (challenge.get(ChallengeType.THROW_EGG).getEnabled()) {

            game.challenge(ChallengeType.THROW_EGG, team);

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
