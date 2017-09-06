package com.worldciv.events.player;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LightLevelEvent implements Listener{

    public Player[] currentlyBlinded;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Location location = player.getLocation();
        Location vision =  new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
        int LightLevel = vision.getBlock().getLightLevel();


        if(LightLevel <= 4){

            player.sendMessage(ChatColor.GOLD + "Your vision becomes unclear.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999, 10));
            currentlyBlinded.add(p);


        }

    }
}
