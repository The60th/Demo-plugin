package com.worldciv.the60th;

import com.worldciv.commands.news;
import com.worldciv.commands.party;
import com.worldciv.commands.toggle;
import com.worldciv.events.player.commandPreprocess;
import com.worldciv.events.player.join;
import com.worldciv.events.player.quit;
import com.worldciv.events.player.weatherChangeEvent;
import com.worldciv.scoreboard.scoreboardManager;
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

import static com.worldciv.utility.utilityStrings.worldciv;

public class MainTorch extends JavaPlugin implements Listener{
    FileConfiguration config = getConfig();
    public static scoreboardManager scoreboardManager;
    public static Plugin plugin;

        /*
                                    NOTES [6 October 2017: 6:41am PST]

    for future maybe ideas:
    -know who you are lighting! found way somewhere in comments below in updatevisionTeam. perfect for dungeons.
    - rain turn off torches

     */

    public void onEnable() {

        getConfig().options().copyDefaults(true);
        getConfig().set("World Civilization", "");

        if (getConfig().getString("newsmessage") == null) {
            getConfig().set("newsmessage", "          " + ChatColor.GRAY + "This must be a new server. Set a news message with /news set <message>");
        }
        saveConfig();

        scoreboardManager = new scoreboardManager();
        plugin = this;
        PluginDescriptionFile pdfFile = this.getDescription();
        Logger logger = Logger.getLogger("Minecraft");

        logger.info(pdfFile.getName()
                + "has successfully enabled. The current version is: "
                + pdfFile.getVersion());

        registerEvents();
        registerCommands();

        //Check time of day!
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
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

        for (Player p : Bukkit.getOnlinePlayers()){
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); //REMOVES CURRENT SB if at all any.
            scoreboardManager.setScoreboard(p);


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
        getCommand("toggle").setExecutor(new toggle());
        getCommand("news").setExecutor(new news());
        getCommand("party").setExecutor(new party());
    }
    public void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new quit(), this);
        pm.registerEvents(new join(), this);
        pm.registerEvents(new commandPreprocess(), this);
        pm.registerEvents(new weatherChangeEvent(), this);

    }


    private void registerPermissons(){
        //PluginManager pm = getServer().getPluginManager();
        //Permission p = new Permission("Permisson name");
        //pm.addPermission(p);

    }



    public static Plugin getPlugin() {
        return plugin;
    }
    public static scoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

}
