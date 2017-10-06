package com.worldciv.commands;

import com.worldciv.the60th.MainTorch;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import static com.worldciv.utility.utilityArrays.*;
import static com.worldciv.utility.utilityStrings.mainbot;
import static com.worldciv.utility.utilityStrings.maintop;
import static com.worldciv.utility.utilityStrings.worldciv;

public class commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("news")) {
            if (!sender.hasPermission("worldciv.news") && sender instanceof Player) {

                sender.sendMessage(maintop);
                sender.sendMessage(ChatColor.GRAY + " Today's current news message has been delivered to be:");
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MainTorch.plugin.getConfig().getString("newsmessage")));
                sender.sendMessage(" ");
                sender.sendMessage(mainbot);
                return true;
            }
            else if (args.length == 0) {
                sender.sendMessage(maintop);
                if (MainTorch.plugin.getConfig().getString("newsmessage") == null) {
                    sender.sendMessage(ChatColor.GRAY + "No current news message found for the server. To add one use:" + ChatColor.YELLOW + " /news set <message>" + ChatColor.GRAY + ".");
                    sender.sendMessage(mainbot);
                    return true;
                }
                sender.sendMessage(ChatColor.GRAY + " The current news message is set to:");
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MainTorch.plugin.getConfig().getString("newsmessage")));
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.GRAY + " To replace the current display message:" + ChatColor.YELLOW + " /news set <message>");
                sender.sendMessage(ChatColor.GRAY + " Got no news? A bit dry?:" + ChatColor.YELLOW + " /news set empty");
                sender.sendMessage(mainbot);
                return true;

            } else if (args[0].equalsIgnoreCase("set")) {

                if (args.length >= 2) {

                    StringBuilder str = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        str.append(args[i] + " ");
                    }

                    String newsstring = ChatColor.YELLOW + str.toString().substring(0, str.length() - 1);
                    MainTorch.plugin.getConfig().set("newsmessage", newsstring);
                    MainTorch.plugin.saveConfig();

                    sender.sendMessage(ChatColor.GREEN + "The news message has been set to: ");
                    sender.sendMessage(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', newsstring));

                    for(Player players : Bukkit.getOnlinePlayers()){
                        MainTorch.getScoreboardManager().setScoreboard(players);
                    }
                    return true;
                }

                sender.sendMessage(worldciv + ChatColor.RED + " Specify a message! Example:" + ChatColor.YELLOW + " /news set <message>");
                return true;

            } else {
                sender.sendMessage(worldciv + ChatColor.RED + " Invalid arguments! Example:" + ChatColor.YELLOW + " /news set <message>");
                return true;

            }


        } else if (cmd.getName().equalsIgnoreCase("toggle")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be a player to access this command!");

                return true;
            }

            Player p = (Player) sender; //declare player

            if (args.length == 0) {
                p.sendMessage(worldciv + ChatColor.RED + " Invalid arguments! Use " + ChatColor.YELLOW + "/toggle help" + ChatColor.RED + ". Example: " + ChatColor.YELLOW + "/toggle sb");
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {

                p.sendMessage(maintop);
                p.sendMessage(ChatColor.GRAY + " The toggle commands are:" + ChatColor.AQUA + " scoreboardManager (sb), sbanimation (anim), visionmessages (vm/vms)");

                if (p.hasPermission("worldciv.blind")) {
                    p.sendMessage(ChatColor.GRAY + " The staff toggle commands are (only staff can see this):" + ChatColor.AQUA + " blind (b)");
                }
                p.sendMessage(" " + mainbot);
            } else if (args[0].equalsIgnoreCase("sb") || args[0].equalsIgnoreCase("scoreboardManager")) {
                if (dummytoggleboard.contains(p)) {
                    dummytoggleboard.remove(p);
                    p.sendMessage(worldciv + ChatColor.GRAY + " The scoreboardManager has been enabled!");
                    MainTorch.getScoreboardManager().setScoreboard(p);
                    return true;

                } else if (!dummytoggleboard.contains(p)) {
                    dummytoggleboard.add(p);
                    p.sendMessage(worldciv + ChatColor.GRAY + " The scoreboardManager has been disabled!");
                    p.setScoreboard(MainTorch.getScoreboardManager().emptyboard);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("blind") || args[0].equalsIgnoreCase("b")) {
                if (!p.hasPermission("worldciv.toggleblind")) {
                    p.sendMessage(worldciv + ChatColor.GRAY + " This command is only allowed for staff. If you believe this is an error, ask staff to provide you the" + ChatColor.AQUA + " worldciv.toggleblind" + ChatColor.GRAY + " permission.");
                    return true;
                }
                if (!toggleblind.contains(p)) {
                    toggleblind.add(p);
                    p.sendMessage(worldciv + ChatColor.GRAY + " You have enabled " + ChatColor.YELLOW + "vision bypass.");
                    if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                        p.removePotionEffect(PotionEffectType.BLINDNESS);
                    }
                    return true;
                }
                if (toggleblind.contains(p)) {
                    toggleblind.remove(p);
                    p.sendMessage(worldciv + ChatColor.GRAY + " You have disabled " + ChatColor.YELLOW + "vision bypass.");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("sbanimation") || args[0].equalsIgnoreCase("anim")) {
                if (toggledisplay.contains(p)) {
                    toggledisplay.remove(p);
                    p.sendMessage(worldciv + ChatColor.GRAY + " The display title's animation has been enabled!");
                    return true;

                } else if (!toggledisplay.contains(p)) {
                    toggledisplay.add(p);
                    p.sendMessage(worldciv + ChatColor.GRAY + " The display title's animation has been disabled!");

                    return true;
                }
            } else if (args[0].equalsIgnoreCase("visionmessages") || args[0].equalsIgnoreCase("vm") || args[0].equalsIgnoreCase("vms")) {
                if (togglevisionmessage.contains(p)) {
                    togglevisionmessage.remove(p);
                    p.sendMessage(worldciv + ChatColor.GRAY + " The vision message notifications have been enabled!");
                    return true;

                } else if (!togglevisionmessage.contains(p)) {
                    togglevisionmessage.add(p);
                    p.sendMessage(worldciv + ChatColor.GRAY + " The vision message notifications have been disabled!");

                    return true;
                }
            } else {
                p.sendMessage(worldciv + ChatColor.RED + " Not a valid argument! Use" + ChatColor.YELLOW + " /toggle help" + ChatColor.RED + ". Example: " + ChatColor.YELLOW + "/toggle sb");
                return true;
            }
        }

        return true;
    }
}
