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

public class scoreboard implements Listener, CommandExecutor {

    /*
                                    NOTES [10/2/2017]

     - find new way for prefix, use players instead? too many combinations in groups.

                 Pros for Group Prefix:
                     - You can set permissions for a group in considered "Vision" group.
                  Pros for User Prefix:
                     - LESSS groups and less switching. less pain for pex :(.

     -  //postponed-for-now// know who you are lighting! found way somewhere in comments below in updatevisionTeam. perfect for dungeons.



     BUG
     - another bug 60 needs to fix or i may need help. player double casts when you are lighting someone? because 2 players in range? test with 3?
     -offhand support critically needed



     FIXES
     - TAB plugin feature was updated. hopefully fixed bug.


     need-to-do:
    -reset pex/tab configs from teams (not crucially needed, do once decided to move from Torch groups to suffixes)
    */


    //empty scoreboard
    ScoreboardManager emptymanager = Bukkit.getScoreboardManager();
    Scoreboard emptyboard = emptymanager.getNewScoreboard();

    public static ArrayList<Player> toggleblind = new ArrayList<Player>();
    public static ArrayList<Player> visionteam = new ArrayList<Player>();
    public static ArrayList<Player> dummytoggleboard = new ArrayList<Player>();
    public static ArrayList<Player> toggledisplay = new ArrayList<Player>();


    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {


        if (event.getMessage().toLowerCase().startsWith("/tab") && !event.getPlayer().isOp() && !(event.getPlayer() instanceof ConsoleCommandSender)) {
            event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help."); //diguise
            event.setCancelled(true);
        }
        if ((event.getMessage().toLowerCase().startsWith("/pl") || event.getMessage().toLowerCase().startsWith("/help") || event.getMessage().toLowerCase().startsWith("/?")) && !event.getPlayer().hasPermission("worlciv.cmds")) {
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

                if (args.length == 0) {
                    p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.RED + " Invalid arguments! Use /toggle help. Example: /toggle sb");
                    return true;
                }

                else if (args[0].equalsIgnoreCase("help")) {
                    p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " The toggle commands are:" + ChatColor.AQUA + " scoreboard (sb), display (d)");

                    if(sender.hasPermission("worldciv.blind")){
                        p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " The staff toggle commands are (only staff can see this):" + ChatColor.AQUA + " blind (b)");
                    }
                }

                else if (args[0].equalsIgnoreCase("sb") || args[0].equalsIgnoreCase("scoreboard")) {
                    if (dummytoggleboard.contains(p)) {
                        dummytoggleboard.remove(p);
                        p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " The scoreboard has been enabled!");
                        setScoreboard(p);
                        return true;

                    } else if (!dummytoggleboard.contains(p)) {
                        dummytoggleboard.add(p);
                        p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " The scoreboard has been disabled!");
                        p.setScoreboard(emptyboard);
                        return true;
                    }
                }

                else if (args[0].equalsIgnoreCase("blind") || args[0].equalsIgnoreCase("b")) {
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

                else if (args[0].equalsIgnoreCase("display") || args[0].equalsIgnoreCase("d")) {
                    if (toggledisplay.contains(p)) {
                        toggledisplay.remove(p);
                        p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " The display title's animation has been enabled!");
                        return true;

                    } else if (!toggledisplay.contains(p)) {
                        toggledisplay.add(p);
                        p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " The display title's animation has been disabled!");

                        return true;
                    }
                }

                else {
                    p.sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.RED + " Too many arguments! Use /toggle help. Example: /toggle sb");
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
        final Team blankscoreofficial = oboard.registerNewTeam("blankscore"); //used to increase size

        //Create dummyplayers. These are returned by Bukkit. Not real players, creates object player.
        healthteam.addPlayer(Bukkit.getOfflinePlayer(ChatColor.RED.toString()));
        torchteam.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        blankscoreofficial.addPlayer(Bukkit.getOfflinePlayer(ChatColor.YELLOW.toString()));

        //STATIC BLANKSCORES. DO NOT CHANGE!!! // also creates static placement of scores.
       // Score blankscore = obj.getScore("                                ");   //EMPTY LINES OF SCORE
        Score blankscore2 = obj.getScore("   ");
        blankscore2.setScore(7);
        Score blankscore3 = obj.getScore("  ");
        blankscore3.setScore(4);

        //Create static placement of scores. (what line on sidebar)
        obj.getScore(ChatColor.RED.toString()).setScore(5); //changing value of health. not actual "Health: " string.
        obj.getScore(ChatColor.BLUE.toString()).setScore(3); //changing value of whether blind or not. torch check or x.
        obj.getScore(ChatColor.YELLOW.toString()).setScore(10); //static we need prefix/suffix abuse

        //The loop under me plays the animated display title. try not to create too many loops yo.
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(MainTorch.plugin, new Runnable() {
            int rotation = 1000;

            @Override
            public void run() {
                //COLOR KEY
                ChatColor defaultcolor = ChatColor.GRAY;
                ChatColor defaultcolorrcom = ChatColor.GRAY;
                ChatColor highlightrcom = ChatColor.YELLOW;
                ChatColor highlight = ChatColor.YELLOW;
                String sin = ChatColor.BLACK + ">";
                String sout = ChatColor.BLACK + "<";

                //this animation is quite a hassle

                //NOTE: don't be dumb like me and actually declare every rotation when rotation++ is a thing
                if (!toggledisplay.contains(x)) {
                    if (rotation == 1000) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 999) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 998) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 997) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 996) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 995) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 994) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 993) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC"); // this is the eighth slide.
                        rotation--;
                    } else if (rotation == 992) {
                        obj.setDisplayName(highlightrcom + "R" + defaultcolorrcom + "CommunityMC"); //R (beginning of highlight)
                        rotation--;
                    } else if (rotation == 991) {
                        obj.setDisplayName(defaultcolorrcom + "R" + highlightrcom + "C" + defaultcolorrcom + "ommunityMC"); //C
                        rotation--;
                    } else if (rotation == 990) {
                        obj.setDisplayName(defaultcolorrcom + "RC" + highlightrcom + "o" + defaultcolorrcom + "mmunityMC");  // o
                        rotation--;
                    } else if (rotation == 989) {
                        obj.setDisplayName(defaultcolorrcom + "RCo" + highlightrcom + "m" + defaultcolorrcom + "munityMC"); //m
                        rotation--;
                    } else if (rotation == 988) {
                        obj.setDisplayName(defaultcolorrcom + "RCom" + highlightrcom + "m" + defaultcolorrcom + "unityMC"); //m
                        rotation--;
                    } else if (rotation == 987) {
                        obj.setDisplayName(defaultcolorrcom + "RComm" + highlightrcom + "u" + defaultcolorrcom + "nityMC"); //u
                        rotation--;
                    } else if (rotation == 986) {
                        obj.setDisplayName(defaultcolorrcom + "RCommu" + highlightrcom + "n" + defaultcolorrcom + "ityMC"); //n
                        rotation--;
                    } else if (rotation == 985) {
                        obj.setDisplayName(defaultcolorrcom + "RCommun" + highlightrcom + "i" + defaultcolorrcom + "tyMC"); //i
                        rotation--;
                    } else if (rotation == 984) {
                        obj.setDisplayName(defaultcolorrcom + "RCommuni" + highlightrcom + "t" + defaultcolorrcom + "yMC"); //t
                        rotation--;
                    } else if (rotation == 983) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunit" + highlightrcom + "y" + defaultcolorrcom + "MC"); //y
                        rotation--;
                    } else if (rotation == 982) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunity" + highlightrcom + "M" + defaultcolorrcom + "C"); //M
                        rotation--;
                    } else if (rotation == 981) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityM" + highlightrcom + "C"); //C
                        rotation--;
                    } else if (rotation == 980) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 979) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 978) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 977) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 976) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 975) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 974) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        rotation--;
                    } else if (rotation == 973) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC"); //eight slide
                        rotation--;
                    } else if (rotation == 972) {
                        obj.setDisplayName(sin + defaultcolorrcom + "  RCommunityMC  " + sout); // double eat rcom
                        rotation--;
                    } else if (rotation == 971) {
                        obj.setDisplayName(sin + defaultcolorrcom + "  RCommunityMC  " + sout);
                        rotation--;
                    } else if (rotation == 970) {
                        obj.setDisplayName(sin + defaultcolorrcom + " RCommunityMC " + sout);
                        rotation--;
                    } else if (rotation == 969) {
                        obj.setDisplayName(sin + defaultcolorrcom + " CommunityM " + sout);
                        rotation--;
                    } else if (rotation == 968) {
                        obj.setDisplayName(sin + defaultcolorrcom + " ommunity " + sout);
                        rotation--;
                    } else if (rotation == 967) {
                        obj.setDisplayName(sin + defaultcolorrcom + " mmunit " + sout);
                        rotation--;
                    } else if (rotation == 966) {
                        obj.setDisplayName(sin + defaultcolorrcom + " muni " + sout);
                        rotation--;
                    } else if (rotation == 965) {
                        obj.setDisplayName(sin + defaultcolorrcom + " un " + sout);
                        rotation--;
                    } else if (rotation == 964) {
                        obj.setDisplayName(sin + defaultcolorrcom + "" + sout);
                        rotation--;
                    } else if (rotation == 963) {
                        obj.setDisplayName(sout + defaultcolor + "" + sin);
                        rotation = 961;                                                 //SKIP 962, mistakes were made
                    } else if (rotation == 961) {
                        obj.setDisplayName(sout + defaultcolor + " d- " + sin); //throw up world civ
                        rotation--;
                    } else if (rotation == 960) {
                        obj.setDisplayName(sout + defaultcolor + " ld- " + sin);
                        rotation--;
                    } else if (rotation == 959) {
                        obj.setDisplayName(sout + defaultcolor + " rld-C " + sin);
                        rotation--;
                    } else if (rotation == 958) {
                        obj.setDisplayName(sout + defaultcolor + " orld-Ci " + sin);
                        rotation--;
                    } else if (rotation == 957) {
                        obj.setDisplayName(sout + defaultcolor + " World-Civ " + sin);
                        rotation--;
                    } else if (rotation == 956) {
                        obj.setDisplayName(sout + defaultcolor + "  World-Civ  " + sin);
                        rotation--;
                    } else if (rotation == 955) {
                        obj.setDisplayName(sout + defaultcolor + "  World-Civ  " + sin);
                        rotation--;
                    } else if (rotation == 954) {
                        obj.setDisplayName(defaultcolor + "World-Civ"); //static
                        rotation--;
                    } else if (rotation == 953) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation--;
                    } else if (rotation == 952) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation--;
                    } else if (rotation == 951) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation = 0;
                    } else if (rotation == 0) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation = 1;
                    } else if (rotation == 1) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                    rotation = 2;
                } else if (rotation == 2) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                    rotation = 3;
                } else if (rotation == 3) {
                        obj.setDisplayName(defaultcolor + "World-Civ"); //TO BE EIGHTH SLIDE
                    rotation = 4;
                } else if (rotation == 4) {
                        obj.setDisplayName(highlight + "W" + defaultcolor + "orld-Civ"); //W //begin highlight animation
                    rotation = 5;
                } else if (rotation == 5) {
                        obj.setDisplayName(defaultcolor + "W" + highlight + "o" + defaultcolor + "rld-Civ"); //o
                    rotation = 6;
                } else if (rotation == 6) {
                        obj.setDisplayName(defaultcolor + "Wo" + highlight + "r" + defaultcolor + "ld-Civ"); //r
                    rotation = 7;
                } else if (rotation == 7) {
                        obj.setDisplayName(defaultcolor + "Wor" + highlight + "l" + defaultcolor + "d-Civ"); //l
                    rotation = 8;
                } else if (rotation == 8) {
                        obj.setDisplayName(defaultcolor + "Worl" + highlight + "d" + defaultcolor + "-Civ"); //d
                    rotation = 9;
                } else if (rotation == 9) {
                        obj.setDisplayName(defaultcolor + "World" + highlight + "-" + defaultcolor + "Civ"); //-
                    rotation = 10;
                } else if (rotation == 10) {
                        obj.setDisplayName(defaultcolor + "World-" + highlight + "C" + defaultcolor + "iv"); //C
                    rotation = 11;
                } else if (rotation == 11) {
                        obj.setDisplayName(defaultcolor + "World-C" + highlight + "i" + defaultcolor + "v"); //i
                    rotation = 12;
                } else if (rotation == 12) {
                        obj.setDisplayName(defaultcolor + "World-Ci" + highlight + "v"); //v
                    rotation = 13;
                } else if (rotation == 13) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation++;
                } else if (rotation == 14) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation++;
                } else if (rotation == 15) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation++;
                } else if (rotation == 16) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation++;
                } else if (rotation == 17) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation++;
                } else if (rotation == 18) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation++;
                } else if (rotation == 19) {
                        obj.setDisplayName(defaultcolor + "World-Civ");
                        rotation++;
                } else if (rotation == 20) {
                        obj.setDisplayName(defaultcolor + "World-Civ"); //TO BE EIGHTH SLIDE
                        rotation++;
                } else if (rotation == 21) {
                        obj.setDisplayName(sin + defaultcolor + "  World-Civ  " + sout);  //EATING WORLDCIV
                        rotation++;
                } else if (rotation == 22) {
                        obj.setDisplayName(sin + defaultcolor + "  World-Civ  " + sout);
                        rotation++;
                } else if (rotation == 23) {
                        obj.setDisplayName(sin + defaultcolor + " World-Civ " + sout);
                        rotation++;
                } else if (rotation == 24) {
                        obj.setDisplayName(sin + defaultcolor + " orld-Ci " + sout);
                        rotation++;
                } else if (rotation == 25) {
                        obj.setDisplayName(sin + defaultcolor + " rld-C " + sout);
                        rotation++;
                } else if (rotation == 26) {
                        obj.setDisplayName(sin + defaultcolor + " ld- " + sout);
                        rotation++;
                } else if (rotation == 27) {
                        obj.setDisplayName(sin + defaultcolor + " d- " + sout);
                        rotation++;
                } else if (rotation == 28) {
                        obj.setDisplayName(sin + defaultcolor + "" + sout);
                        rotation++;
                } else if (rotation == 29) {
                        obj.setDisplayName(sout + defaultcolorrcom + "" + sin);
                        rotation++;
                } else if (rotation == 30) {
                        obj.setDisplayName(sout + defaultcolorrcom + " un " + sin); //THROWING UP RCOM
                        rotation++;
                } else if (rotation == 31) {
                        obj.setDisplayName(sout + defaultcolorrcom + " muni " + sin);
                        rotation++;
                } else if (rotation == 32) {
                        obj.setDisplayName(sout + defaultcolorrcom + " mmunit " + sin);
                        rotation++;
                } else if (rotation == 33) {
                        obj.setDisplayName(sout + defaultcolorrcom + " ommunity " + sin);
                        rotation++;
                } else if (rotation == 34) {
                        obj.setDisplayName(sout + defaultcolorrcom + " CommunityM " + sin);
                        rotation++;
                } else if (rotation == 35) {
                        obj.setDisplayName(sout + defaultcolorrcom + " RCommunityMC " + sin);
                        rotation++;
                } else if (rotation == 36) {
                        obj.setDisplayName(sout + defaultcolorrcom + "  RCommunityMC  " + sin);
                        rotation++;
                } else if (rotation == 37) {
                        obj.setDisplayName(sout + defaultcolorrcom + "  RCommunityMC  " + sin);
                        rotation = 1000;
                    }
                }

                else if (toggledisplay.contains(x)){
                    obj.setDisplayName(defaultcolor + "World-Civ");
                }
            }

        }, 0, 5);

        //The loop under me updated the scoreboard every tick.
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(MainTorch.plugin, new Runnable() {
            @Override
            public void run() {
                updateScoreboard(x, obj, healthteam, torchteam , blankscoreofficial); //update every tick
            }

        }, 0, 1);

        x.setScoreboard(oboard);

    }

    public void updateScoreboard(Player x, Objective obj, Team healthteam, Team torchteam, Team blankscoreofficial) {

        //Variables that need to init every time to update.
        long health = Math.round(x.getHealth());

        // obj.setDisplayName("World Civilization"); //idea for future: create animated title (look down for updateSidebarTitle). create a function to make things more neat.

        updateVisionTeam(x); //has to be called before checking if you are blind or not. checkmark or x.
        LightLevelEvent.updateLightLevelEvent(x);

        //blankscore

        blankscoreofficial.setPrefix(ChatColor.GRAY + "    ¯¯");
        blankscoreofficial.setSuffix(ChatColor.GRAY + "¯¯¯¯¯¯¯¯¯¯¯¯¯"); //please dont change this i will cry. this was like 12 trial and error tries

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




























