package com.worldciv.parties;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.worldciv.the60th.MainTorch.plugin;
import static com.worldciv.utility.utilityMultimaps.*;
import static com.worldciv.utility.utilityStrings.worldciv;

public class Party {


    public Party() {

    }

    public void create(final Player p) {

        if (partyid.containsKey(p.getName())) {
            p.sendMessage(worldciv + ChatColor.GRAY + " You're already in a party!");
            return;
        }


        UUID uuid = UUID.randomUUID();
        String stringuuid = String.valueOf(uuid);

        p.sendMessage(worldciv + ChatColor.GRAY + " You have successfully created a party! Be sure to invite other players with" + ChatColor.YELLOW + " /party invite <player>");
        partyid.put(p.getName(), stringuuid);
        partyleaderid.put(p.getName(), stringuuid);

    }

    public void leave (final Player sender){

        if (!partyid.containsKey(sender.getName())) {
            sender.sendMessage(worldciv + ChatColor.GRAY + " You're not in a party!");
            return;
        }

        if (partyleaderid.containsKey(sender.getName())){
            sender.sendMessage(worldciv + ChatColor.GRAY + " You are the leader of this party! You must assign leadership to someone else with "
                    + ChatColor.YELLOW + " /party leader <player>" + ChatColor.GRAY + " or disband the party with " + ChatColor.YELLOW + " /party disband <player>");
            return;
        }

        //we need to get sender's uuid, use that uuid to get the collection of players, get the collection of players thru iteration//

        Collection leaderuuidcol = partyid.get(sender.getName()); //gets party uuid of the player in parameter
        Collection membersuuidcol = partyid.get(sender.getName()); //gets party uuid of the player in parameter


        String leaderuuid = leaderuuidcol.toString(); //gets the collection uuid and turns it into string for us to work with!

        leaderuuid = leaderuuid.replace("[" , "");
        leaderuuid = leaderuuid.replace("]", "");

        String membersuuid = membersuuidcol.toString();

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection<String> collectionleader = inversepartyid.get(leaderuuid); //this gets the uuid we converted and returns all the players in the party in a collection
        Collection<String> collectionmembers = inversepartyid.get(membersuuid); //this gets the uuid we converted and returns all the players in the party in a collection


        System.out.print(partyid.entries());

        for (String collection : collectionleader) {   //MEMBERS

            Bukkit.broadcastMessage("testing");
            Player members = Bukkit.getServer().getPlayer(collection);
            members.sendMessage(worldciv + ChatColor.GRAY + " A companion, " + ChatColor.AQUA + sender.getName() + ChatColor.GRAY + ", has bid his farewell.");

        }

        for (String collection : collectionmembers) {   //MEMBERS

            Bukkit.broadcastMessage("testing2");
            Player members = Bukkit.getServer().getPlayer(collection);
            members.sendMessage(worldciv + ChatColor.GRAY + " A companion, " + ChatColor.AQUA + sender.getName() + ChatColor.GRAY + ", has bid his farewell.");

        }


        sender.sendMessage(worldciv + ChatColor.GRAY + " You have bid farewell to your companions!");
        partyid.removeAll(sender.getName());
        return;
    }

    public void disband(final Player sender){ //100% WORKS. DO NOT TOUCH THIS <3

        if (!partyid.containsKey(sender.getName())) {
            sender.sendMessage(worldciv + ChatColor.GRAY + " You're not in a party!");
            return;
        }

            if (!partyleaderid.containsKey(sender.getName())){
            sender.sendMessage(worldciv + ChatColor.GRAY + " You are the not the leader of this party.");
            return;
        }

            Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

            Collection collectionuuid = partyid.get(sender.getName()); //gets uuid

            String listuuid = collectionuuid.toString();

            Collection<String> collectionplayers = inversepartyid.get(listuuid); //this returns all the players in the party in a collection

            for (String collection : collectionplayers) {   //MEMBERS NOT LEADER HYPE!

                Player members = Bukkit.getServer().getPlayer(collection);
                members.sendMessage(worldciv + ChatColor.GRAY + " The party you were in was disbanded!");

                partyid.removeAll(collection);

            }

            partyid.removeAll(sender.getName());
            partyleaderid.removeAll(sender.getName());
            sender.sendMessage(worldciv + ChatColor.GRAY + " You have disbanded the party!");
            System.out.print(partyleaderid.entries().toString());

            return;

        }



    public void add(final Player sender, final Player leader) {


        sender.sendMessage(worldciv + ChatColor.GRAY + " You have joined " + ChatColor.AQUA + leader.getName() + "'s" + ChatColor.GRAY
                + " party!");

        leader.sendMessage(worldciv + ChatColor.GRAY + " A new challenger, " + ChatColor.AQUA + sender.getName()
                + ChatColor.GRAY + ", has joined the party!");


        Collection collectionuuid = partyid.get(leader.getName());
        String stringuuid = collectionuuid.toString();

        partyid.put(sender.getName(), stringuuid);

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());


        System.out.print(partyid.entries().toString()); // for testing purposes
        System.out.print(inversepartyid.entries().toString());


    }

    public void block(Player sender, Player leader){
        if(blockedplayers.containsEntry(sender.getName(), leader.getName())){//contains entry of blocked player

            sender.sendMessage(worldciv + ChatColor.GRAY + " You have unblocked " + ChatColor.AQUA  + leader.getName() + ChatColor.GRAY + " from sending you party invites." );
            blockedplayers.remove(sender.getName(), leader.getName());
            return;
        } else {
            sender.sendMessage(worldciv + ChatColor.GRAY + " You have blocked " + ChatColor.AQUA  + leader.getName() + ChatColor.GRAY + " from sending you party invites." );
            blockedplayers.put(sender.getName(), leader.getName());
        }
    }

    public void invite(Player sender, Player receiver) {

        if(blockedplayers.containsEntry(receiver.getName(), sender.getName())){//contains entry of blocked player

            sender.sendMessage(worldciv + ChatColor.AQUA + " " + receiver.getName() + ChatColor.GRAY + " has blocked you from inviting him.");
            return;
        }

        if (!partyleaderid.containsKey(sender.getName())) {
            sender.sendMessage(worldciv + ChatColor.GRAY + " You are the not the leader of this party.");

            return;
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


    }

    public void accept(Player sender, Player leader){

    }

    public boolean hasParty(Player sender) {
        if (partyid.containsKey(sender.getName())) {
            System.out.print("found in partyid multimap");
            return true;
        } else {
            System.out.print("not found in partyid multimap");
            return false;

        }
    }

    public boolean hasSameParty(Player sender, Player receiver) {

        Collection collectionuuid = partyid.get(sender.getName());
        String stringuuid = collectionuuid.toString();

        System.out.print(partyid.entries().toString());

        if (partyid.containsEntry(receiver.getName(), stringuuid)) {
            System.out.print("found in partyid multimap");
            return true;
        } else {
            System.out.print("not found in partyid multimap");
            return false;

        }
    }

    public int size(Player sender) {

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String, String>create());

        Collection collectionuuid = partyid.get(sender.getName());
        String stringuuid = collectionuuid.toString();


        return inversepartyid.get(stringuuid).size();
    }

    public boolean isInvited(Player sender, Player receiver) {

        if (partyrequest.containsEntry(sender.getName(), receiver.getName())) {
            return true;

        }
    return false;

    }
}






