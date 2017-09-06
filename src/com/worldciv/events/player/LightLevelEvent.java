package com.worldciv.events.player;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class LightLevelEvent implements Listener{

    public Player[] currentlyBlinded;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();
        Location l = p.getLocation();
        Location vision =  new Location(l.getWorld(), l.getX(), l.getY() + 1, l.getZ());
        int LightLevel = vision.getBlock().getLightLevel();


        if(LightLevel <= 4){
            p.sendMessage(ChatColor.GOLD + "Your vision becomes unclear.");

        }

    }
}
