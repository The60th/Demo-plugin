package com.worldciv.scoreboardmanager;

import com.worldciv.commands.Commands;
import com.worldciv.devteam.main;
import com.worldciv.utility.Scroller;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.util.ArrayList;

import static com.worldciv.scoreboardmanager.updateAnimations.*;
import static com.worldciv.scoreboardmanager.updateScoreboard.*;
import static com.worldciv.commands.Commands.*;

public class setScoreboard {

    //empty scoreboard
    public static ScoreboardManager emptymanager = Bukkit.getScoreboardManager();
    public static Scoreboard emptyboard = emptymanager.getNewScoreboard();

    public static ArrayList<Player> toggleblind = new ArrayList<Player>();
    public static ArrayList<Player> visionteam = new ArrayList<Player>();
    public static ArrayList<Player> dummytoggleboard = new ArrayList<Player>();
    public static ArrayList<Player> toggledisplay = new ArrayList<Player>();
    public static ArrayList<Player> togglevisionmessage = new ArrayList<Player>();


    public static void setScoreboard(Player x) {

        Scoreboard oboard = Bukkit.getScoreboardManager().getNewScoreboard(); //Creates a new scoreboard for every player.

        final Objective obj = oboard.registerNewObjective("WorldCiv", "dummy"); //Uses FINAL for same objective. Different objective every time will cause flickering
        obj.setDisplaySlot(DisplaySlot.SIDEBAR); //self explanatory, this objective is present in the sidebar

        //Complex team variables. These will be only used for CHANGING VALUES. I.E: Health, Torch. DO NOT USE FOR STATIC SCORES LIKE BLANK SCORES, IGN, "HEALTH: "
        final Team healthteam = oboard.registerNewTeam("Health"); //valid team: hp changes result from dmg or heal
        final Team torchteam = oboard.registerNewTeam("Torch"); //valid team: blind or not
        final Team blankscoreofficial = oboard.registerNewTeam("blankscore"); //used to increase size
        final Team newsteam = oboard.registerNewTeam("newsteam"); //used to display news

        //Create dummyplayers. These are returned by Bukkit. Not real players, creates object player.
        healthteam.addPlayer(Bukkit.getOfflinePlayer(ChatColor.RED.toString()));
        torchteam.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        blankscoreofficial.addPlayer(Bukkit.getOfflinePlayer(ChatColor.YELLOW.toString()));
        newsteam.addPlayer(Bukkit.getOfflinePlayer(ChatColor.GOLD.toString()));

        //STATIC BLANKSCORES. DO NOT CHANGE!!! // also creates static placement of scores.
        // Score blankscore = obj.getScore("                                ");   //EMPTY LINES OF SCORE
        Score blankscore2 = obj.getScore("                   "); //static 19 do, not, move, this, string, at, all!
        blankscore2.setScore(7);
        Score blankscore3 = obj.getScore("  ");
        blankscore3.setScore(4);
        Score blankscore4 = obj.getScore("     ");
        blankscore4.setScore(2);

        //Create static placement of scores. (what line on sidebar)
        // obj.getScore(ChatColor.RED.toString()).setScore(5); //changing value of health. not actual "Health: " string.   //TODO if health is to be added, score is conflicting.
        obj.getScore(ChatColor.BLUE.toString()).setScore(3); //changing value of whether blind or not. torch check or x.
        obj.getScore(ChatColor.YELLOW.toString()).setScore(10); //static we need prefix/suffix abuse
        obj.getScore(ChatColor.GOLD.toString()).setScore(5); //news update.



        //The loop under me plays the animated display title. try not to create too many loops yo.
        new BukkitRunnable() {
            int rotation = 1000;
            Scroller scroller = new Scroller(ChatColor.translateAlternateColorCodes('&', Commands.config.getString("newsmessage")), 16, 5, '&');


            @Override
            public void run() {

                if (!x.isOnline()) {
                    cancel();
                }
                if (main.plugin.getConfig().getString("newsmessage") == null || config.getString("newsmessage").equals(ChatColor.YELLOW + "empty") || main.plugin.getConfig().getString("newsmessage").isEmpty()) {
                    newsteam.setPrefix(ChatColor.RED + "No news today!");
                } else {
                    newsteam.setPrefix(ChatColor.translateAlternateColorCodes('&', scroller.next()));  //to change go to top in static line numbers. | line 5.
                }

                //COLOR KEY
                ChatColor defaultcolor = ChatColor.GOLD;   //worldciv default coloring
                ChatColor defaultcolorrcom = ChatColor.LIGHT_PURPLE; //worldciv default coloring
                ChatColor highlightrcom = ChatColor.DARK_PURPLE; //highlight per word rcom
                ChatColor highlight = ChatColor.YELLOW; //highlight default worldciv
                ChatColor rcombar = ChatColor.LIGHT_PURPLE; //default coloring bar for rcom
                String sin = ChatColor.DARK_RED + ">";
                String sout = ChatColor.DARK_RED + "<";

                ChatColor rcomhighlight = ChatColor.DARK_PURPLE;

                //this animation is quite a hassle

                //NOTE: don't be dumb like me and actually declare every rotation when rotation++ is a thing
                if (!toggledisplay.contains(x)) {
                    if (rotation == 1000) {
                        obj.setDisplayName(defaultcolorrcom + "RCommunityMC");
                        blankscoreofficial.setPrefix(ChatColor.GRAY + "          "); //use this format or setDisplay for future updateAnimations with title
                        blankscoreofficial.setSuffix(ChatColor.GRAY + "           ");
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
                        obj.setDisplayName(sout + defaultcolor + " d " + sin);
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
                } else if (toggledisplay.contains(x)) {
                    obj.setDisplayName(defaultcolor + "World-Civ");
                    blankscoreofficial.setPrefix(ChatColor.GRAY + "          ");
                    blankscoreofficial.setSuffix(ChatColor.GRAY + "           "); //DO NOT DELET!
                }
            }


    }.runTaskTimer(main.plugin,0,5);

        //The loop under me updated the scoreboard every tick.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!x.isOnline()) {
                    cancel();
                }
                updateScoreboard(x, obj, healthteam, torchteam, blankscoreofficial, newsteam);

            }
        }.runTaskTimer(main.plugin, 0, 1);

        x.setScoreboard(oboard);

    }
}
