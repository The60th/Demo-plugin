package com.worldciv.the60th;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener{
    FileConfiguration config = getConfig();
    private static Plugin plugin;

    public void onEnable() {

        PluginDescriptionFile pdfFile = this.getDescription();
        Logger logger = Logger.getLogger("Minecraft");

        logger.info(pdfFile.getName()
                + "has successfully enabled. The current version is: "
                + pdfFile.getVersion());
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = Logger.getLogger("Minecraft");
        logger.info(pdfFile.getName() + "has successfully disabled.");
    }

    public void registerCommands(){
        //this.getCommand("name").setExecutor(new class());
    }
    public void registerEvents(){
        //PluginManager pm = getServer().getPluginManager();
        //pm.registerEvents(class , this);
    }
    private void registerPermissons(){
        //PluginManager pm = getServer().getPluginManager();
        // Permission p = new Permission("Permisson name");
        //pm.addPermission(p);

    }



    public static Plugin getPlugin() {
        return plugin;
    }
}
