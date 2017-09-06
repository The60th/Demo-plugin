package com.worldciv.events.player;

import com.worldciv.the60th.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Torch;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TorchEvent implements Listener {
    public static ArrayList<Player> holdingLight = new ArrayList<Player>();
    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event){
        Player player = event.getPlayer(); //Player from the event.
        ItemStack currentItem = player.getInventory().getItem(event.getNewSlot()); //The itemstack in the slot the player just moved to may be null
        ItemStack offHandItem =  player.getInventory().getItemInOffHand();
        if(currentItem == null) currentItem = new ItemStack(Material.AIR);
        if(offHandItem == null) offHandItem = new ItemStack(Material.AIR);
        if(currentItem.getType() != Material.TORCH && offHandItem.getType() != Material.TORCH){
            //The player is not holding a torch.
            holdingLight.remove(player);
        }
        else{
            //The player is holding a torch in main hand or off hand.
            holdingLight.add(player);
            if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                player.sendMessage(ChatColor.GOLD + "Your vision begins to clear up.");
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3));

            }
        }
    }
}
