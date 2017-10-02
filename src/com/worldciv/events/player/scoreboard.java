package com.worldciv.events.player;

import com.worldciv.the60th.MainTorch;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;

public class scoreboard  implements Listener, CommandExecutor { //Do I have to explain?

    /*          FUTURE NOTE
          If you're making an animation system for the scoreboard:
            - create a toggle system
            - use same system with if statements
            - do not change setscores. you have 99 values. Use them.
            - think harder

    */

    //Declaring variables for a dummyboard. This board will be used to ONLY STORE DATA on a SECOND BOARD.
    ScoreboardManager dummymanager = Bukkit.getScoreboardManager();
    Scoreboard dummyboard = dummymanager.getNewScoreboard();            //Adjusting thesew values will break the "checkmark" and "x" mark from scoreboards.
    Team dummyteam = dummyboard.registerNewTeam("dummy vision");


    ScoreboardManager dummytogglemanager = Bukkit.getScoreboardManager();
    Scoreboard dummytogglescoreboard = dummytogglemanager.getNewScoreboard();
    Team dummytoggleboard = dummytogglescoreboard.registerNewTeam("dummytoggleb");

    ScoreboardManager emptymanager = Bukkit.getScoreboardManager();
    Scoreboard emptyboard = emptymanager.getNewScoreboard();


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

        if (cmd.getName().equalsIgnoreCase("toggle")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be a player to access this command!");

            } else {
                Player p = (Player) sender;

                if (dummytoggleboard.hasPlayer(p)) {
                    dummytoggleboard.removePlayer(p);
                    p.sendMessage("REMOVED");

                } else if (!dummytoggleboard.hasPlayer(p)) {
                    dummytoggleboard.addPlayer(p);
                    p.sendMessage("ADDED");
                    p.setScoreboard(emptyboard);
                }
            }
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer(); //player triggering event

        // SCOREBOARD CREATION //

        Scoreboard oboard = Bukkit.getScoreboardManager().getNewScoreboard(); //Creates a new scoreboard for every player.
        Scoreboard emptyboard = Bukkit.getScoreboardManager().getNewScoreboard();

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
        obj.getScore(ChatColor.BLUE.toString()).setScore(3); //changin value of whether blind or not. torch check or x.

        //Creates multiple threads for loop. Saves player in here. This way loops for every differ player. also creates updating.
        // The loop presented below is used to update the title every half second.
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
        //The loop presented below is used to update all scores per tick. every 0.05 seconds.
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(MainTorch.plugin, new Runnable() {

            @Override
            public void run() {

                updateScoreboard(p, obj, healthteam, torchteam); //update every tick

            }

        }, 0, 1);

        p.setScoreboard(oboard);

    }

    public void updateDummyTeam(Player x) {

        if (LightLevelEvent.currentlyBlinded.contains(x)) { //if ur light level is low with no light

            if (dummyteam.hasPlayer(x)) { //if u r in torch dummyteam
                dummyteam.removePlayer(x); // remove from dummyteam
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + x.getName() + " group remove Torch");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");
                x.sendMessage("removing" + x.getDisplayName() + "from team");
                x.sendMessage("blinded");
            }
        }

        if (!LightLevelEvent.currentlyBlinded.contains(x)) { //if ur light level is high, u can see
            if (!dummyteam.hasPlayer(x)) { //if ur not on torch team now u r
                dummyteam.addPlayer(x);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + x.getName() + " group add Torch");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");
                x.sendMessage("adding" + x.getDisplayName() + "to team");
                x.sendMessage("vision");
            }
        }


    }

    

    public void updateScoreboard(Player x, Objective obj, Team healthteam, Team torchteam) {

        //Variables that need to init every time to update.
        long health = Math.round(x.getHealth());

        // obj.setDisplayName("World Civilization"); //idea for future: create animated title (look down for updateSidebarTitle). create a function to make things more neat.

        updateDummyTeam(x); //has to be called before checking if you are blind or not. checkmark or x.

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
        if (dummyteam.hasPlayer(x)) {
            torchteam.setPrefix(ChatColor.RED + "Torch [T]:" + ChatColor.BLUE + " ✓");

        } else if (!dummyteam.hasPlayer(x)) {
            torchteam.setPrefix(ChatColor.RED + "Torch [T]:" + ChatColor.BLUE + " ✗");
        }
    }


    public void updateSidebarTitle(Objective obj) {
        //for future animation style :)
    }

}




























