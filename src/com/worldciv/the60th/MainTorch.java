package com.worldciv.the60th;

import com.worldciv.events.player.LightLevelEvent;
import com.worldciv.events.player.scoreboard;
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

import static com.worldciv.events.player.scoreboard.worldciv;

public class MainTorch extends JavaPlugin implements Listener{
    FileConfiguration config = getConfig();
    public static Plugin plugin;

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
        getCommand("toggle").setExecutor(new scoreboard());
        getCommand("news").setExecutor(new scoreboard());

        //Check time of day!
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
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
            scoreboard.setScoreboard(p);


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
        pm.registerEvents(new LightLevelEvent(), this);
        pm.registerEvents(new scoreboard(), this);
    }


    private void registerPermissons(){
        //PluginManager pm = getServer().getPluginManager();
        //Permission p = new Permission("Permisson name");
        //pm.addPermission(p);

    }



    public static Plugin getPlugin() {
        return plugin;
    }
}
//det