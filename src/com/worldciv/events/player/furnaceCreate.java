package com.worldciv.events.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.worldciv.the60th.MainTorch.plugin;
import static com.worldciv.utility.utilityStrings.worldciv;

public class furnaceCreate implements Listener {

    @EventHandler
    public void onSmeltCompletion(FurnaceSmeltEvent e) {

        Furnace furnace = (Furnace) e.getBlock().getState();
        FurnaceInventory furnaceInv = (FurnaceInventory) furnace.getInventory();

        ItemStack[] itemsInFurnace = furnaceInv.getContents();

        Material source = Material.EGG;
        Material fuel = Material.COAL;

        if (itemsInFurnace[0].getType() == source) {

            if (itemsInFurnace[0].getItemMeta().getLore() == null){
                itemsInFurnace[0].setAmount(-1);
                List<Entity> near = furnace.getWorld().getEntities();
                for (Entity players : near) {

                    if (players instanceof Player) {
                        if (players.getLocation().distance(furnace.getLocation()) <= 10) {

                            ((Player) players).updateInventory(); //it glitches with non-vanilla items, thts why we update
                            players.sendMessage(worldciv + ChatColor.GRAY + " The egg(s) fried!");

                        }

                    }
                }

                Bukkit.getServer().getWorld("world").playSound(furnace.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 5L, 0);

                e.setCancelled(true);

                return;
            }

           else if (itemsInFurnace[0].getItemMeta().getLore().get(0) == "imisspetra") {
                Bukkit.getServer().getWorld("world").playSound(furnace.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5L, 0);
            }
        }

    }


    @EventHandler
    public void onBurn(FurnaceBurnEvent e) {

        Furnace furnace = (Furnace) e.getBlock().getState();
        FurnaceInventory furnaceInv = (FurnaceInventory) furnace.getInventory();

        ItemStack[] itemsInFurnace = furnaceInv.getContents();

        Material source = Material.EGG;
        Material fuel = Material.COAL;


        if (itemsInFurnace[0].getType() == source && (e.getFuel().getType() == fuel)) {

            if (itemsInFurnace[0].getItemMeta().getLore() == null) {
                e.setCancelled(true);
                e.setBurnTime(20);
                return;
            }

            for (HumanEntity players : furnaceInv.getViewers()) {

                ((Player) players).updateInventory(); //it glitches with non-vanilla items, thts why we update
            }

            if (itemsInFurnace[0].getItemMeta().getLore().get(0) == "imisspetra") {

                Bukkit.broadcastMessage("I miss Petra Egg and Coal located!");
                e.setBurnTime(200); //200 is one item
            }
        }
    }

  /*  @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        HumanEntity p = e.getWhoClicked();
        if (p instanceof Player) { //clicker is player

            if (e.getView().getType() == InventoryType.FURNACE) { // detects if its a furnace

                FurnaceInventory furnaceInv = (FurnaceInventory) e.getInventory(); //get anviilinv
                ItemStack[] itemsInFurnace = furnaceInv.getContents();



                int slot = e.getRawSlot(); //slots of anvil

                Material source = Material.EGG;
                Material fuel = Material.COAL;


                if (slot == 0 || slot == 1 || e.isShiftClick() || e.isLeftClick() || e.isRightClick()) {

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {

                            try {

                                if (itemsInFurnace[0] == null || itemsInFurnace[0].getItemMeta().getLore() == null || itemsInFurnace[0].getType() == null) {
                                    if (p.getItemOnCursor().getType() == Material.EGG && furnaceInv.getHolder().getBurnTime() >= ((short) 1)) {
                                        if (p.getItemOnCursor().getItemMeta().getLore().get(0) != "imisspetra") {
                                            p.sendMessage(worldciv + ChatColor.GRAY + " The fuel ran out! I wonder why?");
                                            furnaceInv.getHolder().setBurnTime((short) 20);
                                            return;
                                        }
                                    }
                                }

                                if (p.getItemOnCursor().getType() == Material.EGG && furnaceInv.getHolder().getBurnTime() > 0) {
                                    if (p.getItemOnCursor().getItemMeta().getLore() == null) {
                                        p.sendMessage(worldciv + ChatColor.GRAY + " The fuel ran out! I wonder why?");
                                        furnaceInv.getHolder().setBurnTime((short) 20);
                                        return;
                                    }
                                }

                                if (itemsInFurnace[1] == null) { // itemsInFurnace[1].getItemMeta().getLore() == null

                                    if (itemsInFurnace[0] == null) {
                                        return;
                                    }
                                    if (itemsInFurnace[0].getType() == Material.EGG) {
                                        furnaceInv.getHolder().setBurnTime((short) 20);
                                        return;
                                    }
                                }


                                if (itemsInFurnace[0].getType() == source && (itemsInFurnace[1].getType() == fuel)) {
                                    if (itemsInFurnace[0].getItemMeta().getLore() == null) {
                                        furnaceInv.getHolder().setBurnTime((short) 20);
                                        return;
                                    }
                                } else if (itemsInFurnace[0].getType() == source) {
                                    furnaceInv.getHolder().setBurnTime((short) 20);
                                    return;

                                }

                            } catch (Exception e) {

                            }

                        }
                    }, 3);


                }
            }
        }
    }*/

}
