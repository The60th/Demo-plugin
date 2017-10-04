package com.worldciv.events.player;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static com.worldciv.events.player.scoreboard.toggleblind;
import static com.worldciv.events.player.scoreboard.togglevisionmessage;
import static com.worldciv.events.player.scoreboard.worldciv;

public class LightLevelEvent implements Listener {

    public static ArrayList<Player> currentlyBlinded = new ArrayList<Player>();
    public static ArrayList<Player> holdingLight = new ArrayList<Player>();
    public static ArrayList<Player> holdingTorch = new ArrayList<Player>();


    public static void updateLightLevelEvent(Player player){

        Location location = player.getLocation();
        Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1.62, location.getZ());
        int LightLevel = vision.getBlock().getLightLevel();

        if (toggleblind.contains(player) || player.getGameMode() == GameMode.CREATIVE){
            if (currentlyBlinded.contains(player)) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                currentlyBlinded.remove(player);
            }
            return;
        }

        updateHoldingLight(player);


        if (LightLevel <= 5) { // IF ITS DARK


            if (holdingLight.contains(player)) { //IF A TORCH IS ONLY ON A PLAYER HAND
                if (currentlyBlinded.contains(player)) {
                    currentlyBlinded.remove(player); //THIS REMOVES ONLY FOR PLAYER HOLDING
                }
                return;

            } else {
                player.getNearbyEntities(5, 5, 5);
                List<Entity> entitylist = player.getNearbyEntities(5, 5, 5);
                for (int i = 0; i < entitylist.size(); i++) {
                    if (entitylist.get(i).getType() == EntityType.PLAYER) {
                        if (holdingLight.contains((Player) entitylist.get(i))) {
                            if(vision.getBlock().getType() != Material.WATER || vision.getBlock().getType() != Material.STATIONARY_WATER ){

                                player.removePotionEffect(PotionEffectType.BLINDNESS);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3));
                                if (currentlyBlinded.contains(player)) {
                                    currentlyBlinded.remove(player);
                                }

                                return;
                            }

                        }
                    }
                }
                if (!(player.hasPotionEffect(PotionEffectType.BLINDNESS))) {
                    if(!togglevisionmessage.contains(player))
                    player.sendMessage(worldciv + ChatColor.GRAY + " Your vision becomes unclear");
                    currentlyBlinded.add(player);

                }


                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 1));

            }
        } else if (LightLevel > 5) {
            if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                if(!togglevisionmessage.contains(player))
                player.sendMessage(worldciv + ChatColor.GRAY + " Your vision begins to clear up from nearby light.");
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3));
                currentlyBlinded.remove(player);
            }
        }
    }

    public static void updateHoldingLight(Player x) {
        ItemStack currentItem = x.getInventory().getItemInHand();
        ItemStack offHandItem = x.getInventory().getItemInOffHand();

        if (currentItem.getType() != Material.TORCH && offHandItem.getType() != Material.TORCH) { //NOT HOLDING TORCH

            if (holdingLight.contains(x)) //contains player holding light
                holdingLight.remove(x); //remove player
            holdingTorch.remove(x);
        } else if (currentItem.getType() == Material.TORCH || offHandItem.getType() == Material.TORCH) { //if youre holding a torch

            Location location = x.getLocation();
            Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1.62, location.getZ());

            if (vision.getBlock().getType() == Material.WATER || vision.getBlock().getType() == Material.STATIONARY_WATER) { //if player's head is in water and youre holding a torch
                if (holdingTorch.contains(x)) {
                    if (!togglevisionmessage.contains(x)) {
                        x.sendMessage(worldciv + ChatColor.GRAY + " Torches don't work underwater, silly!");
                    }
                    if (holdingLight.contains(x)) { //if player's head is on water and youre holding a torch and PREVIOUSLY holding light
                        holdingLight.remove(x);
                    }
                    holdingTorch.remove(x);
                }
                return;
            }

            holdingLight.add(x);
            holdingTorch.add(x);

            if (x.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                if (!togglevisionmessage.contains(x))
                    x.sendMessage(worldciv + ChatColor.GRAY + " Your vision begins to clear up from held light.");
                x.removePotionEffect(PotionEffectType.BLINDNESS);
                x.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3));


            }

        }
    }

}


