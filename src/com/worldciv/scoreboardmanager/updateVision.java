package com.worldciv.scoreboardmanager;

import com.worldciv.events.player.lightLevelEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

import static com.worldciv.devteam.main.worldciv;
import static com.worldciv.scoreboardmanager.setScoreboard.*;

public class updateVision {

    public static void updateVisionData(Player x) {

        Location location = x.getLocation(); //player loc variables
        Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1.62, location.getZ());
        int LightLevel = vision.getBlock().getLightLevel();

        List<Entity> entitylist = x.getNearbyEntities(5, 5, 5); //getting radius 5
        for (int i = 0; i < entitylist.size(); i++) { // for all that are in vision effect

            if (entitylist.get(i).getType() == EntityType.PLAYER) { //for those that are players

                if (lightLevelEvent.holdingLight.contains(x) && !visionteam.contains(x)) { //if you are being lit and you are already not in vision.

                    if (!togglevisionmessage.contains(entitylist.get(i)))
                        entitylist.get(i).sendMessage(worldciv + ChatColor.GRAY + " You have been provided vision by " + ChatColor.AQUA + x.getDisplayName());

                }

                if (lightLevelEvent.holdingLight.contains((Player) entitylist.get(i))) { //BELOW THIS (PLAYER) X BECOMES PERSON BEING LIT

                    if (vision.getBlock().getType() != Material.WATER || vision.getBlock().getType() != Material.STATIONARY_WATER) {
                        //    entitylist.get(i).sendMessage(x.getDisplayName()); WILL TELL YOU (ALL) WHO YOU (HOLDER OF TORCH) ARE LIGHTING HYPE example: KotoriXIII (me): You are lighting (all players)

                        if (!visionteam.contains(x)) {

                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + x.getName() + " group add Torch"); //<3 the tab
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                            visionteam.add(x); //ADDS TO TEAM AND VISION FROM AOE
                        }
                        return; //CANCELS UNNECESSARY SPAM FROM BELOW
                    }

                }
            }
        }


        if (lightLevelEvent.currentlyBlinded.contains(x)) { //if ur light level is low with no light

            if (visionteam.contains(x)) { //if u r in torch visionteam

                visionteam.remove(x); // remove from visionteam
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + x.getName() + " group remove Torch"); //<3 the tab
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                //YOU HAVE BEEN BLINDED

            }

        }


        if (!lightLevelEvent.currentlyBlinded.contains(x)) { //if ur light level is high, u can see
            if (!visionteam.contains(x)) { //if ur not on torch team now u r
                if (LightLevel > 5 || lightLevelEvent.holdingLight.contains(x)) {

                    visionteam.add(x);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + x.getName() + " group add Torch");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                    //YOU HAVE EYES NOW, YAY YOU CAN SEE

                }
            }
        }


    }
}
