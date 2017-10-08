package com.worldciv.parties;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

import static com.worldciv.utility.utilityMultimaps.partyid;
import static com.worldciv.utility.utilityStrings.worldciv;

public class Party {

    public Party() {

    }

    public void create(final Player p) {

        if (partyid.containsValue(p.getName())) {
            p.sendMessage(worldciv + ChatColor.GRAY + " You're already in a party!");
            return;
        }


        UUID uuid = UUID.randomUUID();
        String stringuuid = String.valueOf(uuid);

        p.sendMessage(worldciv + ChatColor.GRAY + " You have successfully created a party! Be sure to invite other players with" + ChatColor.YELLOW + " /party invite <player>");
        partyid.put(p.getName(), stringuuid);


    }

    public void add(final Player sender, final Player leader) {


        sender.sendMessage(worldciv + ChatColor.GRAY + " You have joined " + ChatColor.AQUA + leader.getName() + "'s" + ChatColor.GRAY
                + " party!");

        leader.sendMessage(worldciv + ChatColor.GRAY + " A new player by the name of, " + ChatColor.AQUA + sender.getName()
                + ChatColor.GRAY + ", has joined the party!");


        Collection collectionuuid = partyid.get(leader.getName());
        String stringuuid = collectionuuid.toString();


        partyid.put(sender.getName(), stringuuid);

        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String , String>create());


        System.out.print(partyid.entries().toString()); // for testing purposes
        System.out.print(inversepartyid.entries().toString());



    }

    public boolean invite(Player sender){
        Multimap<String, String> inversepartyid = Multimaps.invertFrom(partyid, ArrayListMultimap.<String , String>create());

        Collection collectionuuid = partyid.get(sender.getName());
        String stringuuid = collectionuuid.toString();

        sender.sendMessage("invite fun passed");

        if(inversepartyid.get(stringuuid).size() >= 1){ //people other than leader!!!
            sender.sendMessage("this is bigger than one");
            return true;
        }
        return false;
    }



}
