package com.worldciv.the60th;

import com.worldciv.events.player.LightLevelEvent;
import com.worldciv.events.player.TorchEvent;
import com.worldciv.events.player.scoreboard;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class MainTorch extends JavaPlugin implements Listener{
    FileConfiguration config = getConfig();
    public static Plugin plugin;

    //public static ScoreboardManager manager = Bukkit.getScoreboardManager();
    //public static Scoreboard board = manager.getNewScoreboard();
    //public static Team team = board.registerNewTeam("holdingLight");
    public void onEnable() {
        plugin = this;
        PluginDescriptionFile pdfFile = this.getDescription();
        Logger logger = Logger.getLogger("Minecraft");

        logger.info(pdfFile.getName()
                + "has successfully enabled. The current version is: "
                + pdfFile.getVersion());
        registerEvents();
        getCommand("toggle").setExecutor(new scoreboard());
        getCommand("toggleblind").setExecutor(new scoreboard());

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
        pm.registerEvents(new TorchEvent(), this);
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
