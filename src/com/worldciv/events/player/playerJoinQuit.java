package com.worldciv.events.player;

import com.worldciv.devteam.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.worldciv.scoreboardmanager.setScoreboard.*;
import static com.worldciv.commands.Commands.*;

/*
                As the name suggests. This is a player join/quit event. The only usage this scoreboard uses is setting scoreboard.

                //TODO HelloCutiepie has asked to make a first join player spawn.
                //TODO create spawns


 */

public class playerJoinQuit implements Listener{


    @SuppressWarnings("deprecation")
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer(); //player triggering event

        setScoreboard(p); //only set one scoreboard!

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
    }
}
