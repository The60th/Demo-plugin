package com.worldciv.events.player;

import com.worldciv.the60th.MainTorch;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static com.worldciv.events.player.scoreboard.toggleblind;

public class LightLevelEvent implements Listener {

    public static ArrayList<Player> currentlyBlinded = new ArrayList<Player>();

    public static void updateLightLevelEvent(Player player){

        Location location = player.getLocation();
        Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
        int LightLevel = vision.getBlock().getLightLevel();

        if (toggleblind.contains(player)) {
            if (currentlyBlinded.contains(player)) {
                currentlyBlinded.remove(player);
            }
            return;
        }


        if (player.getGameMode() == GameMode.CREATIVE) {

            if (currentlyBlinded.contains(player)) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                currentlyBlinded.remove(player);
            }
            return;

        }

        if (LightLevel <= 5) { // IF ITS DARK

            if (TorchEvent.holdingLight.contains(player)) { //IF A TORCH IS ONLY ON A PLAYER HAND
                if (currentlyBlinded.contains(player)) {
                    currentlyBlinded.remove(player); //THIS REMOVES ONLY FOR PLAYER HOLDING
                }
                return;

            } else {
                player.getNearbyEntities(5, 5, 5);
                List<Entity> entitylist = player.getNearbyEntities(5, 5, 5);
                for (int i = 0; i < entitylist.size(); i++) {
                    if (entitylist.get(i).getType() == EntityType.PLAYER) {
                        if (TorchEvent.holdingLight.contains((Player) entitylist.get(i))) {
                            player.removePotionEffect(PotionEffectType.BLINDNESS);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3));
                            if (currentlyBlinded.contains(player)) {
                                currentlyBlinded.remove(player);
                            }

                            return;
                        }
                    }
                }
                if (!(player.hasPotionEffect(PotionEffectType.BLINDNESS))) {
                    player.sendMessage(ChatColor.GOLD + "Your vision becomes unclear.");
                    currentlyBlinded.add(player);

                }


                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 1));

            }
        } else if (LightLevel > 5) {
            if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                player.sendMessage(ChatColor.GOLD + "Your vision begins to clear up.");
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3));
                currentlyBlinded.remove(player);
            }
        }
    }
}

