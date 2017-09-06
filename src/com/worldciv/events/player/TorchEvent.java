package com.worldciv.events.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Torch;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Array;

public class TorchEvent implements Listener {
    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
        if(itemStack == new ItemStack(Material.TORCH)){
            Bukkit.broadcastMessage(player.getDisplayName() + " is holding a torch");
            if(player.hasPotionEffect(PotionEffectType.BLINDNESS)){
                Bukkit.broadcastMessage(player.getDisplayName() + " player is currently blindly -- removing");
                player.removePotionEffect(PotionEffectType.BLINDNESS);
            }
            if(LightLevelEvent.currentlyBlinded.contains(player)){
                Bukkit.broadcastMessage(player.getDisplayName() + " is in currently blinded -- removing");
                LightLevelEvent.currentlyBlinded.remove(player);
            }
        } else if(itemStack == new ItemStack(Material.REDSTONE_TORCH_ON) || itemStack == new ItemStack(Material.REDSTONE_TORCH_OFF)){
            Bukkit.broadcastMessage(player.getDisplayName() + " is holding a red stone torch");
            if(player.hasPotionEffect(PotionEffectType.BLINDNESS)){
                Bukkit.broadcastMessage(player.getDisplayName() + " player is currently blindly -- removing");
                player.removePotionEffect(PotionEffectType.BLINDNESS);
            }
            if(LightLevelEvent.currentlyBlinded.contains(player)){
                Bukkit.broadcastMessage(player.getDisplayName() + " is in currently blinded -- removing");
                LightLevelEvent.currentlyBlinded.remove(player);
            }
        }
    }
}
