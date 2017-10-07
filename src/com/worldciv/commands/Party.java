package com.worldciv.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Party implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Silly console! You can't join parties, that's unfair!");
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("party") || cmd.getName().equalsIgnoreCase("p")) {
            if(args.length == 0){
                //create help page
                return true;
            }


        }

     return false; //end of command boolean
    }
}
