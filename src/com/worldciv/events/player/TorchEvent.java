package com.worldciv.events.player;

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
import org.bukkit.util.io.BukkitObjectInputStream;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TorchEvent implements Listener {
    public static ArrayList<Player> currentlyLit = new ArrayList<Player>();

    //6 block range for hand held torchs
    //Done ^^

    //BUGS:
    //When you swap into your off hand, the game thinks your are still lite.
    //
    //Add client side light for all effected players
    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
        if ((itemStack == null || itemStack.getType() != Material.TORCH)) {
            List<Entity> entitylist = player.getNearbyEntities(5, 5, 5);
            entitylist.add(player);
            for(int i=0;i<entitylist.size();i++){
                if(entitylist.get(i).getType()==EntityType.PLAYER){
                    player = (Player)entitylist.get(i);
                    //its a player in the radius so do whatever
                    // you need to with each
                    if (currentlyLit.contains(player)) {
                        currentlyLit.remove(player);
                    }

                }
            }
            return;
        }
        Bukkit.broadcastMessage(player.getDisplayName() + " trigger torch event. Item in hand is " + itemStack);
        if (itemStack.getType() == Material.TORCH) {
            Bukkit.broadcastMessage(player.getDisplayName() + " is holding a torch");

            //start of aoe light test.
            List<Entity> entitylist = player.getNearbyEntities(5, 5, 5);
            entitylist.add(player);
            for(int i=0;i<entitylist.size();i++){
                if(entitylist.get(i).getType()== EntityType.PLAYER){
                    Player playerOld = player;
                    player = (Player) entitylist.get(i);
                    Bukkit.broadcastMessage(player.getDisplayName() + " is near " + playerOld.getDisplayName() + " giving them light.");
                    if (LightLevelEvent.currentlyBlinded.contains(player)) {
                        Bukkit.broadcastMessage(player.getDisplayName() + " is in currently blinded -- removing");
                        LightLevelEvent.currentlyBlinded.remove(player);
                        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                            player.sendMessage(ChatColor.GOLD + "Your vision begins to clear up.");
                            currentlyLit.add(player);
                            Bukkit.broadcastMessage(player.getDisplayName() + " player is currently blindly -- removing");
                            player.removePotionEffect(PotionEffectType.BLINDNESS);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 1));

                        }
                    }

                }
            }
            //End of aoe light test

            //Uncomment to return to old version.
           /*if (LightLevelEvent.currentlyBlinded.contains(player)) {
                Bukkit.broadcastMessage(player.getDisplayName() + " is in currently blinded -- removing");
                LightLevelEvent.currentlyBlinded.remove(player);
                if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                    player.sendMessage(ChatColor.GOLD + "Your vision begins to clear up.");
                    currentlyLit.add(player);
                    Bukkit.broadcastMessage(player.getDisplayName() + " player is currently blindly -- removing");
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));

                }
            }
            */
        }
    }

//    @EventHandler
//    public void onPlayerSwapHandItemEvent(PlayerSwapHandItemsEvent event){
//        Player player = event.getPlayer();
//        ItemStack itemStackMain = event.getMainHandItem();
//        ItemStack itemStackOff = event.getOffHandItem();
//
//        if ((itemStackMain == null && itemStackOff == null) || (itemStackMain.getType() != Material.TORCH && itemStackOff.getType() != Material.TORCH  )) {
//            if (currentlyLit.contains(player)) {
//                currentlyLit.remove(player);
//                Bukkit.broadcastMessage("Offhand removing lit");
//            }
//            return;
//        }
//        Bukkit.broadcastMessage(player.getDisplayName() + " trigger torch event. Item in hand is " + itemStackMain);
//        if (itemStackMain.getType() == Material.TORCH || itemStackOff.getType() == Material.TORCH  ) {
//            Bukkit.broadcastMessage(player.getDisplayName() + " is holding a torch");
//            if (LightLevelEvent.currentlyBlinded.contains(player)) {
//                Bukkit.broadcastMessage(player.getDisplayName() + " is in currently blinded -- removing");
//                LightLevelEvent.currentlyBlinded.remove(player);
//                if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
//                    player.sendMessage(ChatColor.GOLD + "Your vision begins to clear up.");
//                    currentlyLit.add(player);
//                    Bukkit.broadcastMessage(player.getDisplayName() + " player is currently blindly -- removing");
//                    player.removePotionEffect(PotionEffectType.BLINDNESS);
//                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5, 1));
//                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5, 1));
//
//                }
//            }
//        }
//    }
}
