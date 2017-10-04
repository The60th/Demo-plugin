package com.worldciv.events.player;

import com.worldciv.the60th.MainTorch;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static com.worldciv.events.player.scoreboard.toggleblind;
import static com.worldciv.events.player.scoreboard.togglevisionmessage;
import static com.worldciv.events.player.scoreboard.worldciv;

public class LightLevelEvent implements Listener {
    public static ArrayList<Player> holdingLight = new ArrayList<Player>();
    public static ArrayList<Player> currentlyBlinded = new ArrayList<Player>();

    public static void updateLightLevelEvent(Player player){
        Material mainHand = player.getInventory().getItemInMainHand().getType();
        Material offHand = player.getInventory().getItemInOffHand().getType();
        if(mainHand == Material.TORCH || offHand == Material.TORCH){
            //Get lity.
            if(!(holdingLight.contains(player))){
                holdingLight.add(player);
            }
        }else{
            if(holdingLight.contains(player)){
                holdingLight.remove(player);
            }
        }
        //Holding light will now only have the player if they currently have a torch in one of their hands.

        Location location = player.getLocation();
        Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1.62, location.getZ());
        int LightLevel = vision.getBlock().getLightLevel();

        if (toggleblind.contains(player) || player.getGameMode() == GameMode.CREATIVE) {
            if (currentlyBlinded.contains(player)) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                currentlyBlinded.remove(player);
            }
            return;
        }

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
}

