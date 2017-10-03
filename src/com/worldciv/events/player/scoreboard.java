package com.worldciv.events.player;

import com.worldciv.the60th.MainTorch;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class scoreboard implements Listener, CommandExecutor { //Do I have to explain?

    /*          NOTES [10/2/2017]

            - find new way for prefix, use players instead? too many combinations in groups.
                  Pros for Group Prefix:
                        - You can set permissions for a group in considered "Vision" group.
                  Pros for User Prefix:
                        - LESSS groups and less switching. less pain for pex :(.
            - offhand support ++!
            -  //postponed-for-now// know who you are lighting! found way somewhere in comments below in updatedummyteams
            - another bug 60 needs to fix or i may need help. player double casts when you are lighting someone? because 2 players in range?
            - fix /pl etc. need substring methods. ask 60. atm as of now "/? 4" goes through whereas /? does not.
            - TAB plugin feature was updated. hopefully fixed bug.

    */

    //empty scoreboard
    ScoreboardManager emptymanager = Bukkit.getScoreboardManager();
    Scoreboard emptyboard = emptymanager.getNewScoreboard();

    public static ArrayList<Player> toggleblind = new ArrayList<Player>();
    public static ArrayList<Player> visionteam = new ArrayList<Player>();
    public static ArrayList<Player> dummytoggleboard = new ArrayList<Player>();


    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        if (event.getMessage().substring(0).equals("/tab") && !event.getPlayer().isOp() && !(event.getPlayer() instanceof ConsoleCommandSender)) {
            event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
            event.setCancelled(true);
        }
        if ((event.getMessage().substring(0).equals("/pl") || event.getMessage().substring(0).equals("/help") || event.getMessage().substring(0).equals("/?")) && !event.getPlayer().isOp()) {
            event.getPlayer().sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " Not allowed to use this special command.");
            event.setCancelled(true);

        }
    }


    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to access this command!");

        } else {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("toggle")) {

                if (dummytoggleboard.contains(p)) {
                    dummytoggleboard.remove(p);
                    p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " The scoreboard has been enabled!");
                    setScoreboard(p);

                } else if (!dummytoggleboard.contains(p)) {
                    dummytoggleboard.add(p);
                    p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " The scoreboard has been disabled!");
                    p.setScoreboard(emptyboard);
                }
            } else if (cmd.getName().equalsIgnoreCase("toggleblind")) {
                if (!sender.hasPermission("worldciv.toggleblind")) {
                    p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " This command is only allowed for staff. If you believe this is an error, ask staff to provide you the" + ChatColor.AQUA + " worldciv.toggleblind" + ChatColor.GRAY + " permission.");
                    return true;
                }
                if (!toggleblind.contains(p)) {
                    toggleblind.add(p);
                    p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " You have enabled " + ChatColor.YELLOW + "vision bypass.");
                    if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                        p.removePotionEffect(PotionEffectType.BLINDNESS);
                    }
                    return true;
                }
                if (toggleblind.contains(p)) {
                    toggleblind.remove(p);
                    p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " You have disabled " + ChatColor.YELLOW + "vision bypass.");
                    return true;
                }
            }
        }

        return true;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer(); //player triggering event

        // SCOREBOARD CREATION //

        setScoreboard(p);

    }

    public void updateVisionTeam(Player x) {

        Location location = x.getLocation(); //player loc variables
        Location vision = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
        int LightLevel = vision.getBlock().getLightLevel();

        List<Entity> entitylist = x.getNearbyEntities(5, 5, 5); //getting radius 5
        for (int i = 0; i < entitylist.size(); i++) { // for all that are in vision effect

            if (entitylist.get(i).getType() == EntityType.PLAYER) { //for those that are players

                if (TorchEvent.holdingLight.contains(x) && !visionteam.contains(x)) { //if you are being lit and you are already not in vision.

                    entitylist.get(i).sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " You have been provided vision by " + ChatColor.AQUA + x.getDisplayName());
                }

                if (TorchEvent.holdingLight.contains((Player) entitylist.get(i))) { //BELOW THIS (PLAYER) X BECOMES PERSON BEING LIT

                    //    entitylist.get(i).sendMessage(x.getDisplayName()); WILL TELL YOU (ALL) WHO YOU (HOLDER OF TORCH) ARE LIGHTING HYPE example: KotoriXIII (me): You are lighting (all players)

                    if (!visionteam.contains(x)) {

                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + x.getName() + " group add Torch"); //<3 the tab
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                        visionteam.add(x); //ADDS TO TEAM AND VISION FROM AOE
                    }
                    return; //CANCELS UNNECESSARY SPAM FROM BELOW
                }
            }
        }


        if (LightLevelEvent.currentlyBlinded.contains(x)) { //if ur light level is low with no light

            if (visionteam.contains(x)) { //if u r in torch visionteam

                visionteam.remove(x); // remove from visionteam
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + x.getName() + " group remove Torch"); //<3 the tab
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                //YOU HAVE BEEN BLINDED

            }

        }


        if (!LightLevelEvent.currentlyBlinded.contains(x)) { //if ur light level is high, u can see
            if (!visionteam.contains(x)) { //if ur not on torch team now u r
                if (LightLevel > 5 || TorchEvent.holdingLight.contains(x)) {

                    visionteam.add(x);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + x.getName() + " group add Torch");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");

                    //YOU HAVE EYES NOW, YAY YOU CAN SEE

                }
            }
        }


    }


    public void setScoreboard(Player x) {

        Scoreboard oboard = Bukkit.getScoreboardManager().getNewScoreboard(); //Creates a new scoreboard for every player.

        final Objective obj = oboard.registerNewObjective("WorldCiv", "dummy"); //Uses FINAL for same objective. Different objective every time will cause flickering
        obj.setDisplaySlot(DisplaySlot.SIDEBAR); //self explanatory, this objective is present in the sidebar

        //Complex team variables. These will be only used for CHANGING VALUES. I.E: Health, Torch. DO NOT USE FOR STATIC SCORES LIKE BLANK SCORES, IGN, "HEALTH: "
        final Team healthteam = oboard.registerNewTeam("Health"); //valid team: hp changes result from dmg or heal
        final Team torchteam = oboard.registerNewTeam("Torch"); //valid team: blind or not

        //Create dummyplayers. These are returned by Bukkit. Not real players, creates object player.
        healthteam.addPlayer(Bukkit.getOfflinePlayer(ChatColor.RED.toString()));
        torchteam.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));

        //STATIC BLANKSCORES. DO NOT CHANGE!!! // also creates static placement of scores.
        Score blankscore = obj.getScore("                                ");   //EMPTY LINES OF SCORE // CANNOT USE SAME STRING> MIX IT UP WITH SOME FLASHLIGHTS!
        blankscore.setScore(10);
        Score blankscore2 = obj.getScore("🔦🔦🔦");
        blankscore2.setScore(7);
        Score blankscore3 = obj.getScore("🔦🔦");
        blankscore3.setScore(4);

        //Create static placement of scores. (what line on sidebar)
        obj.getScore(ChatColor.RED.toString()).setScore(5); //changing value of health. not actual "Health: " string.
        obj.getScore(ChatColor.BLUE.toString()).setScore(3); //changing value of whether blind or not. torch check or x.

        //The loop under me plays the animated display title. try not to create too many loops yo.
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(MainTorch.plugin, new Runnable() {
            int rotation = 1;
            @Override
            public void run() {
                //COLOR KEY
                ChatColor defaultcolor = ChatColor.GRAY;
                ChatColor highlight = ChatColor.YELLOW;
                String sin = ChatColor.BLACK + ">";
                String sout = ChatColor.BLACK + "<";

                //this animation is quite a hassle

                //NOTE: don't be dumb like me and actually declare every rotation when rotation++ is a thing

                if (rotation == 1) {
                    obj.setDisplayName(defaultcolor + "World Civilization");
                    rotation = 2;
                } else if (rotation == 2) {
                    obj.setDisplayName(defaultcolor + "World Civilization");
                    rotation = 3;
                } else if (rotation == 3) {
                    obj.setDisplayName(defaultcolor + "World Civilization");
                    rotation = 4;
                } else if (rotation == 4) {
                    obj.setDisplayName(highlight + "W" + defaultcolor + "orld Civilization");
                    rotation = 5;
                } else if (rotation == 5) {
                    obj.setDisplayName(defaultcolor + "W" + highlight + "o" + defaultcolor + "rld Civilization");
                    rotation = 6;
                } else if (rotation == 6) {
                    obj.setDisplayName(defaultcolor + "Wo" + highlight + "r" + defaultcolor + "ld Civilization");
                    rotation = 7;
                } else if (rotation == 7) {
                    obj.setDisplayName(defaultcolor + "Wor" + highlight + "l" + defaultcolor + "d Civilization");
                    rotation = 8;
                } else if (rotation == 8) {
                    obj.setDisplayName(defaultcolor + "Worl" + highlight + "d" + defaultcolor + " Civilization");
                    rotation = 9;
                } else if (rotation == 9) {
                    obj.setDisplayName(defaultcolor + "World" + highlight + " C" + defaultcolor + "ivilization");
                    rotation = 10;
                } else if (rotation == 10) {
                    obj.setDisplayName(defaultcolor + "World C" + highlight + "i" + defaultcolor + "vilization");
                    rotation = 11;
                } else if (rotation == 11) {
                    obj.setDisplayName(defaultcolor + "World Ci" + highlight + "v" + defaultcolor + "ilization");
                    rotation = 12;
                } else if (rotation == 12) {
                    obj.setDisplayName(defaultcolor + "World Civ" + highlight + "i" + defaultcolor + "lization");
                    rotation = 13;
                } else if (rotation == 13) {
                    obj.setDisplayName(defaultcolor + "World Civi" + highlight + "l" + defaultcolor + "ization");
                    rotation = 14;
                } else if (rotation == 14) {
                    obj.setDisplayName(defaultcolor + "World Civil" + highlight + "i" + defaultcolor + "zation");
                    rotation = 15;
                } else if (rotation == 15) {
                    obj.setDisplayName(defaultcolor + "World Civili" + highlight + "z" + defaultcolor + "ation");
                    rotation = 16;
                } else if (rotation == 16) {
                    obj.setDisplayName(defaultcolor + "World Civiliz" + highlight + "a" + defaultcolor + "tion");
                    rotation = 17;
                } else if (rotation == 17) {
                    obj.setDisplayName(defaultcolor + "World Civiliza" + highlight + "t" + defaultcolor + "ion");
                    rotation = 18;
                } else if (rotation == 18) {
                    obj.setDisplayName(defaultcolor + "World Civilizat" + highlight + "i" + defaultcolor + "on");
                    rotation = 19;
                } else if (rotation == 19) {
                    obj.setDisplayName(defaultcolor + "World Civilizati" + highlight + "o" + defaultcolor + "n");
                    rotation = 20;
                } else if (rotation == 20) {
                    obj.setDisplayName(defaultcolor + "World Civilization");
                    rotation = 21;
                } else if (rotation == 21) {
                    obj.setDisplayName(defaultcolor + "World Civilization");
                    rotation = 22;
                } else if (rotation == 22) {
                    obj.setDisplayName(sin + defaultcolor + "World Civilization" + sout);
                    rotation = 23;
                } else if (rotation == 23) {
                    obj.setDisplayName(sin + defaultcolor + " orld Civilizatio " + sout);
                    rotation = 24;
                } else if (rotation == 24) {
                    obj.setDisplayName(sin + defaultcolor + " rld Civilizati " + sout);
                    rotation = 25;
                } else if (rotation == 25) {
                    obj.setDisplayName(sin + defaultcolor + " ld Civilizat " + sout);
                    rotation = 26;
                } else if (rotation == 26) {
                    obj.setDisplayName(sin + defaultcolor + " d Civiliza " + sout);
                    rotation = 27;
                } else if (rotation == 27) {
                    obj.setDisplayName(sin + defaultcolor + " Civiliz " + sout);
                    rotation = 28;
                } else if (rotation == 28) {
                    obj.setDisplayName(sin + defaultcolor + " ivili " + sout);
                    rotation = 29;
                } else if (rotation == 29) {
                    obj.setDisplayName(sin + defaultcolor + " vil " + sout);
                    rotation = 30;
                } else if (rotation == 30) {
                    obj.setDisplayName(sin + defaultcolor + " i " + sout);
                    rotation = 31;
                } else if (rotation == 31) {
                    obj.setDisplayName(sin + defaultcolor + "" + sout);
                    rotation = 32;
                } else if (rotation == 32) {
                    obj.setDisplayName(sout + defaultcolor + "" + sin);
                    rotation = 33;
                } else if (rotation == 33) {
                    obj.setDisplayName(sout + defaultcolor + " i " + sin);
                    rotation = 34;
                } else if (rotation == 34) {
                    obj.setDisplayName(sout + defaultcolor + " vil " + sin);
                    rotation = 35;
                } else if (rotation == 35) {
                    obj.setDisplayName(sout + defaultcolor + " ivili " + sin);
                    rotation = 36;
                } else if (rotation == 36) {
                    obj.setDisplayName(sout + defaultcolor + " Civiliz " + sin);
                    rotation = 37;
                } else if (rotation == 37) {
                    obj.setDisplayName(sout + defaultcolor + " d Civiliza " + sin);
                    rotation = 38;
                } else if (rotation == 38) {
                    obj.setDisplayName(sout + defaultcolor + " ld Civilizat " + sin);
                    rotation = 39;
                } else if (rotation == 39) {
                    obj.setDisplayName(sout + defaultcolor + " rld Civilizati " + sin);
                    rotation = 40;
                } else if (rotation == 40) {
                    obj.setDisplayName(sout + defaultcolor + " orld Civilizatio " + sin);
                    rotation = 41;
                } else if (rotation == 41) {
                    obj.setDisplayName(sout + defaultcolor + "World Civilization" + sin);
                    rotation = 42;
                } else if (rotation == 42) {
                    obj.setDisplayName(defaultcolor + "World Civilization");
                    rotation = 1;
                }
            }

        }, 0, 3);

        //The loop under me updated the scoreboard every tick.
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(MainTorch.plugin, new Runnable() {
            @Override
            public void run() {
                updateScoreboard(x, obj, healthteam, torchteam); //update every tick
            }

        }, 0, 20);

        x.setScoreboard(oboard);

    }

    public void updateScoreboard(Player x, Objective obj, Team healthteam, Team torchteam) {

        //Variables that need to init every time to update.
        long health = Math.round(x.getHealth());

        // obj.setDisplayName("World Civilization"); //idea for future: create animated title (look down for updateSidebarTitle). create a function to make things more neat.

        updateVisionTeam(x); //has to be called before checking if you are blind or not. checkmark or x.
        LightLevelEvent.updateLightLevelEvent(x);

        //blankscore

        Score score = obj.getScore(ChatColor.AQUA + "IGN:");
        score.setScore(9);

        Score score1 = obj.getScore(ChatColor.GRAY + x.getName());
        score1.setScore(8);

        //blankscore2 | line 7

        Score score2 = obj.getScore(ChatColor.AQUA + "Health:");
        score2.setScore(6);

        healthteam.setPrefix(ChatColor.GRAY + String.valueOf(health));  //to change go to top in static line numbers. | line 5.

        //blankscore3 | line 4

        // SCORE TO HAVE ✓ OR ✗ MARK

        if (x.getGameMode() == GameMode.CREATIVE || toggleblind.contains(x)) {
            torchteam.setPrefix(ChatColor.YELLOW + "VISION BYPASS");
            torchteam.setSuffix(ChatColor.RESET + "");
        } else {

            if (visionteam.contains(x)) {
                torchteam.setPrefix(ChatColor.YELLOW + "Vision [V]:");
                torchteam.setSuffix(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + " ✓");

            } else if (!visionteam.contains(x)) {
                torchteam.setPrefix(ChatColor.YELLOW + "Vision [V]:");
                torchteam.setSuffix(ChatColor.RED + " ✗");
            }
        }
    }

}




























