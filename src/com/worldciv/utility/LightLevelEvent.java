package com.worldciv.utility;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

import static com.worldciv.utility.utilityArrays.toggleblind;
import static com.worldciv.utility.utilityArrays.togglevisionmessage;
import static com.worldciv.utility.utilityStrings.worldciv;


public class LightLevelEvent  {

    public static ArrayList<Player> currentlyBlinded = new ArrayList<Player>();
    public static ArrayList<Player> holdingLight = new ArrayList<Player>();

    public static void updateLightLevelEvent(Player player){
        if (toggleblind.contains(player) || player.getGameMode() == GameMode.CREATIVE){
            if (currentlyBlinded.contains(player)) {
                unBlindPlayer(player);
                currentlyBlinded.remove(player);
            }
            return;
        }

        Location location = player.getLocation();
        Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1.62, location.getZ());
        int LightLevel = vision.getBlock().getLightLevel();

        if (LightLevel <= 5) { // IF ITS DARK
            updateHoldingLight(player);
            if (holdingLight.contains(player)) { //IF A TORCH IS ONLY ON A PLAYER HAND
                if (currentlyBlinded.contains(player)) {
                    currentlyBlinded.remove(player); //THIS REMOVES ONLY FOR PLAYER HOLDING
                    unBlindPlayer(player);
                }
                else if(player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                    unBlindPlayer(player);
                }
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
                                    unBlindPlayer(player);
                                    return;
                                }

                                return;
                            }

                        }
                    }
                }
                if (!(player.hasPotionEffect(PotionEffectType.BLINDNESS))) {
                    currentlyBlinded.add(player);
                    blindPlayer(player);
                }
            }
        } else if (LightLevel > 5) {
            if(currentlyBlinded.contains(player)){
                currentlyBlinded.remove(player);
                unBlindPlayer(player);
                Bukkit.broadcastMessage("What1");
            }
            else if(player.hasPotionEffect(PotionEffectType.BLINDNESS)){
                unBlindPlayer(player);
                Bukkit.broadcastMessage("What2");

            }else{

            }
        }
    }


    //Make this function check the players light level, handle all checks here. //TODO
    private static void updateHoldingLight(Player player) {
        Material currentItem = player.getInventory().getItemInMainHand().getType();
        Material offHandItem = player.getInventory().getItemInOffHand().getType();

        if(currentItem == Material.TORCH || offHandItem == Material.TORCH){
            //Holding torch.
            Location location = player.getLocation();
            Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1.62, location.getZ());

            if (vision.getBlock().getType() == Material.WATER || vision.getBlock().getType() == Material.STATIONARY_WATER) {
                //in water no torch --no light so remove people
                if(holdingLight.contains(player)){
                    holdingLight.remove(player);
                }
                return;
            }
            if(!holdingLight.contains(player)){
                holdingLight.add(player);
                if(currentlyBlinded.contains(player)){
                    currentlyBlinded.remove(player);
                }
            }else{
                if(currentlyBlinded.contains(player)){
                    currentlyBlinded.remove(player);
                }
            }
        }else{
            if(holdingLight.contains(player)){
                holdingLight.remove(player);
            }
            if(currentlyBlinded.contains(player)){

            }
            else{
                currentlyBlinded.add(player);
            }
        }
    }

    private static void blindPlayer(Player player){
        if(!togglevisionmessage.contains(player)) player.sendMessage(worldciv + ChatColor.GRAY + " Your vision becomes unclear.");
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,20000,2));
    }
    private static void unBlindPlayer(Player player){
        if(!togglevisionmessage.contains(player)) player.sendMessage(worldciv + ChatColor.GRAY + " Your vision becomes clear.");
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }

}
//We dun broke shit, fixing it.


