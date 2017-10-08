package com.worldciv.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.worldciv.the60th.MainTorch.plugin;
import static com.worldciv.utility.utilityMultimaps.partyid;
import static com.worldciv.utility.utilityMultimaps.partyrequest;
import static com.worldciv.utility.utilityStrings.*;


public class Party implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender robotsender, Command cmd, String alias, String[] args) {

        if (!(robotsender instanceof Player)) {
            robotsender.sendMessage(ChatColor.RED + "Silly console! You can't join parties, that's unfair!");
            return true;
        }

        Player sender = (Player) robotsender;

        if (cmd.getName().equalsIgnoreCase("party") || cmd.getName().equalsIgnoreCase("p")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                if (true || args[0].equalsIgnoreCase("help")) { //this if statement checks if youre in a party /// !p.hasParty()
                    sender.sendMessage(maintop);
                    sender.sendMessage(ChatColor.YELLOW + "/party create" + ChatColor.GRAY + ": Create a party to challenge a dungeon.");
                    sender.sendMessage(ChatColor.YELLOW + "/party invite <player>" + ChatColor.GRAY + ": Invite a challenger to your party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party kick <player>" + ChatColor.GRAY + ": Kick the coward from your party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party accept <player>" + ChatColor.GRAY + ": Join a dungeon-raiding party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party deny <player>" + ChatColor.GRAY + ": Refuse to join a feeble party.");
                    sender.sendMessage(ChatColor.YELLOW + "/party block <player>" + ChatColor.GRAY + ": Block/Unblock invitations from players.");
                    sender.sendMessage(ChatColor.YELLOW + "/party help" + ChatColor.GRAY + ": Displays this help page.");
                    sender.sendMessage(mainbot);
                    return true;
                } else { //if in a party
                    return true;
                }

            }

            switch (args[0]) {
                case "invite":

                    if (args.length >= 2) { //If /party invite <this argument or more>

                        Player receiver = Bukkit.getServer().getPlayer(args[1]);

                        if (receiver == null || !receiver.isOnline()) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " We tried our hardest... but we couldn't find this player.");
                            return true;
                        }

                        if (receiver == sender) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " There has to be someone else you can invite. You can't invite yourself...");
                            return true;
                        }

                        com.worldciv.parties.Party party = new com.worldciv.parties.Party();

                        if (party.invite(sender)){
                            return true;
                        }

                        if(partyrequest.containsEntry(sender.getName(), receiver.getName())){
                                sender.sendMessage(worldciv + " " + ChatColor.YELLOW + receiver.getName() + ChatColor.GRAY + " still has an open invitation from you.");
                                return true;

                        }

                        sender.sendMessage(worldciv + ChatColor.GRAY + " You have sent an invitation to " + ChatColor.AQUA + receiver.getName() + ChatColor.GRAY
                                + " to join your party! They have 15 seconds to respond to your message.");

                        receiver.sendMessage(worldciv + ChatColor.GRAY + " You have received an invitation from " + ChatColor.AQUA + sender.getName()
                                + ChatColor.GRAY + " to join their party! You have 15 seconds to confirm with " + ChatColor.YELLOW + "/party accept " + sender.getName() + ChatColor.GRAY + " or" + ChatColor.YELLOW + " /party deny " + sender.getName());


                        partyrequest.put(sender.getName(), receiver.getName());

                        new BukkitRunnable() {
                            int x = 0;

                            public void run() {

                                if (!partyrequest.containsKey(sender.getName())) {
                                    if (!partyrequest.containsValue(receiver.getName()))
                                        cancel();
                                    return;
                                }

                                x++;
                                if (x == 15) {
                                    sender.sendMessage(worldciv + ChatColor.GRAY + " The invitation for " + ChatColor.AQUA + receiver.getName() + ChatColor.GRAY + " has expired.");
                                    receiver.sendMessage(worldciv + ChatColor.GRAY + " The invitation from " + ChatColor.AQUA + sender.getName() + ChatColor.GRAY + " has expired.");
                                    partyrequest.remove(sender.getName(), receiver.getName());
                                    cancel();
                                    return;
                                }
                            }
                        }.runTaskTimer(plugin, 0, 20);

                        return true;

                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " We need more arguments! Use" + ChatColor.YELLOW + " /party invite <player>");
                    return true;

                case "accept":

                    if (args.length >= 2) { //If /party accept <this argument or more>

                        Player leader = Bukkit.getServer().getPlayer(args[1]);

                        if (leader == null || !leader.isOnline()) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " We tried our hardest... but we couldn't find this player.");
                            return true;
                        }

                        if (sender == leader) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't join your own party... Join another!");
                            return true;
                        }

                        if (!partyrequest.containsValue(sender.getName()) || partyrequest.containsValue(sender.getName())) { //if person who is doing /party accept is or isnt even have any open invs
                            if (!partyrequest.containsKey(leader.getName())) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " I can't find any open invitations from " + ChatColor.AQUA + leader.getName() + ChatColor.GRAY + ".");
                                return true;
                            }
                        }


                        if (partyrequest.containsEntry(leader.getName(), sender.getName())) {



                            partyrequest.remove(leader.getName(), sender.getName());

                            com.worldciv.parties.Party party = new com.worldciv.parties.Party();
                            party.add(sender, leader); //some party sht

                            return true;
                        }


                        return true;

                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " We need more arguments! Use" + ChatColor.YELLOW + " /party accept <player>");
                    return true;

                case "deny":

                    if (args.length >= 2) { //If /party deny <this argument or more>

                        Player leader = Bukkit.getServer().getPlayer(args[1]);

                        if (leader == null || !leader.isOnline()) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " We tried our hardest... but we couldn't find this player.");
                            return true;
                        }

                        if (sender == leader) {
                            sender.sendMessage(worldciv + ChatColor.GRAY + " You can't deny your own party... Deny another!");
                            return true;
                        }

                        if (!partyrequest.containsValue(sender.getName()) || partyrequest.containsValue(sender.getName())) { //if person who is doing /party accept is or isnt even have any open invs
                            if (!partyrequest.containsKey(leader.getName())) {
                                sender.sendMessage(worldciv + ChatColor.GRAY + " I can't find any open invitations from " + ChatColor.AQUA + leader.getName() + ChatColor.GRAY + ".");
                                return true;
                            }
                        }


                        if (partyrequest.containsEntry(leader.getName(), sender.getName())) {

                            sender.sendMessage(worldciv + ChatColor.GRAY + " You have refused to join " + ChatColor.AQUA + leader.getName() + "'s" + ChatColor.GRAY
                                    + " party!");

                            leader.sendMessage(worldciv + " " + ChatColor.AQUA + sender.getName() + ChatColor.GRAY + " has refused to join the party!");

                            partyrequest.remove(leader.getName(), sender.getName());

                            return true;
                        }


                        return true;

                    }
                    sender.sendMessage(worldciv + ChatColor.GRAY + " We need more arguments! Use" + ChatColor.YELLOW + " /party deny <player>");
                    return true;

                case "create":

                    if (args.length >= 2) { //If /party accept <this argument or more>

                        sender.sendMessage(worldciv + ChatColor.GRAY + " There's too many arguments! Use" + ChatColor.YELLOW + " /party create");

                        return true;

                    }

                    com.worldciv.parties.Party party = new com.worldciv.parties.Party();
                    party.create(sender);

                    return true;

            }


            sender.sendMessage(worldciv + ChatColor.GRAY + " Invalid arguments. Use" + ChatColor.YELLOW + " /party" + ChatColor.GRAY + " for more help");
            return true;

        }

     return false; //end of command boolean
    }
}
