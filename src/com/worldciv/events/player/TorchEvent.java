package com.worldciv.events.player;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static com.worldciv.events.player.scoreboard.togglevisionmessage;

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
            Location location = player.getLocation();
            Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1.62, location.getZ());


            if(vision.getBlock().getType() == Material.WATER || vision.getBlock().getType() == Material.STATIONARY_WATER ){
                if(!togglevisionmessage.contains(player))
                player.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " Torches don't work underwater, silly!");
                if(holdingLight.contains(player)){
                    holdingLight.remove(player);
                }
                return;
            }

            //The player is holding a torch in main hand or off hand.
            holdingLight.add(player);
            if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                if(!togglevisionmessage.contains(player))
                player.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " Your vision begins to clear up from held light.");
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3));

            }
        }
    }
}
