package com.worldciv.events.player;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static com.worldciv.devteam.main.worldciv;

/*
            This class is used to block commands that would very much like not to display to other players. Such is a command:
            /pl or /help or any of the sort.
 */

public class playerCommandPreprocess implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {


        if (event.getMessage().toLowerCase().startsWith("/tab") && !event.getPlayer().isOp() && !(event.getPlayer() instanceof ConsoleCommandSender)) {
            event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help."); //diguise
            event.setCancelled(true);
        }
        if ((event.getMessage().toLowerCase().startsWith("/pl") || event.getMessage().toLowerCase().startsWith("/help") || event.getMessage().toLowerCase().startsWith("/?")) && !event.getPlayer().hasPermission("worlciv.cmds")) {
            event.getPlayer().sendMessage(worldciv + ChatColor.GRAY + " Not allowed to use this special command.");
            event.setCancelled(true);


        }
    }

}
