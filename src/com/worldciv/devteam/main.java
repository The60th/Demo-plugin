package com.worldciv.devteam;

import com.worldciv.commands.Commands;
import com.worldciv.events.player.lightLevelEvent;
import com.worldciv.events.other.weatherChangeEvent;
import com.worldciv.events.player.playerCommandPreprocess;
import com.worldciv.events.player.playerJoinQuit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import static com.worldciv.scoreboardmanager.setScoreboard.*;
import static com.worldciv.commands.Commands.*;

 /*
                                    NOTES [10/2/2017]

     - find new way for prefix, use players instead? too many combinations in groups.

                 Pros for Group Prefix:
                     - You can set permissions for a group in considered "Vision" group.
                  Pros for User Prefix:
                     - LESSS groups and less switching. less pain for pex :(.


     BUG
     - another bug 60 needs to fix or i may need help. player double casts when you are lighting someone? because 2 players in range? test with 3?
     - offhand support critically needed, check how event works ezpz form there

     need-to-do:
    - reset pex/tab configs from teams (not crucially needed, do once decided to move from Torch groups to suffixes)
    - make a sick blankscore underlining name of server??? look at examples online
    - scrolling string instead? releasing top news / patches / events
    - config for news
    - anvil stuff yeahh!

    for future maybe ideas:
    -know who you are lighting! found way somewhere in comments below in updatevisionTeam. perfect for dungeons.
    - rain turn off torches

     */


public class main extends JavaPlugin {

    public static Plugin plugin;

    public static String worldciv = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "World-Civ" + ChatColor.DARK_GRAY + "]";
    public static String bars = ChatColor.GOLD + "▬▬▬" + ChatColor.DARK_GRAY + "▬▬▬";
    public static String maintop = ChatColor.DARK_GRAY + "║ " + bars + bars + bars + bars + worldciv + bars + bars + bars + bars + ChatColor.DARK_GRAY + "║";
    public static String mainbot = ChatColor.DARK_GRAY + "║ " + bars + bars + bars + bars + bars + bars + bars + bars + bars + bars + ChatColor.DARK_GRAY + "║";

    public void onEnable() {

        getConfig().options().copyDefaults(true);
        getConfig().set("World Civilization", "");
        saveConfig();

        plugin = this;
        PluginDescriptionFile pdfFile = this.getDescription();
        Logger logger = Logger.getLogger("Minecraft");

        logger.info(pdfFile.getName()
                + "has successfully enabled. The current version is: "
                + pdfFile.getVersion());


        registerEvents();

        //Register your Commands
        getCommand("toggle").setExecutor(new Commands());
        getCommand("news").setExecutor(new Commands());

        //Check time of day!
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main.plugin, new Runnable() {
            public void run(){
                Server server = getServer();
                long time = server.getWorld("world").getTime();

                if(time >= 13200 && time <=13239 ){
                    Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " It's getting dark...");
                } else if (time >= 22390 && time <= 22429){ //2399 is last tick or 2400? use 2399 for safety
                    Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " It appears morning is arising.");

                }

            }
        }, 0, 40); //every 2s, try not to jam the server!


            //refresh scoreboard for whoever was logged in already
        for (Player p : Bukkit.getOnlinePlayers()){
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); //REMOVES CURRENT SB if at all any.
            setScoreboard(p);
        }

        Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " Refreshing plugin data.");



    }



    public void onDisable() {
        plugin = null;
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = Logger.getLogger("Minecraft");
        logger.info(pdfFile.getName() + "has successfully disabled.");
    }

    public void registerCommands(){
        //this.getCommand("name").setExecutor(new class());
    }

    public void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new playerJoinQuit(), this);
        pm.registerEvents(new playerCommandPreprocess(), this);
        pm.registerEvents(new weatherChangeEvent(), this);
        pm.registerEvents(new lightLevelEvent(), this);
    }


    private void registerPermissons(){
        //PluginManager pm = getServer().getPluginManager();
        //Permission p = new Permission("Permisson name");
        //pm.addPermission(p);

        //no need to register permissions since plugin.yml automatically registers the permissions within it :)
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
