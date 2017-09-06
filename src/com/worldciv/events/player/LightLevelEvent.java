package com.worldciv.events.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class LightLevelEvent implements Listener{

    public static ArrayList<Player> currentlyBlinded = new ArrayList<Player>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Location location = player.getLocation();
        Location vision =  new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
        int LightLevel = vision.getBlock().getLightLevel();


        if(LightLevel <= 5){
            if(TorchEvent.currentlyLit.contains(player)){
               // Bukkit.broadcastMessage(player.getDisplayName() + " returning becuase current lit.");
                return;
            }
            if (!currentlyBlinded.contains(player)) {
                player.sendMessage(ChatColor.GOLD + "Your vision becomes unclear.");
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 1));
                currentlyBlinded.add(player);
            }
        }

        else if (LightLevel > 5) {
            if (currentlyBlinded.contains(player)) {
                //You are currently blinded. Stop spamming me.
                player.sendMessage(ChatColor.GOLD + "Your vision begins to clear up.");
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                currentlyBlinded.remove(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5, 1));
            }

        }

    }
}
